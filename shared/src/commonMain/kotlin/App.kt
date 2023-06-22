import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun App() {
    MaterialTheme {
        AnimatedListView()
    }
}

expect fun getPlatformName(): String


@Composable
fun AnimatedListView() {
    val items by remember { mutableStateOf(recipesList) }
    var width by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
        width = it.size.width
    })

    val listState = rememberLazyListState()

    LazyColumn(modifier = Modifier.fillMaxSize().background(sugar), state = listState) {
        items(items) { item ->
            RecipeListItemWrapper(
                child = {
                    RecipeListItem(item, width)
                },
                scrollDirection = if (listState.isScrollingDown()) ScrollDirection.Backward else ScrollDirection.Forward
            )
        }
    }
}

@Composable
fun LazyListState.isScrollingDown(): Boolean {
    val offset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) { derivedStateOf { (firstVisibleItemScrollOffset - offset) > 0 } }.value
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    val offset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) { derivedStateOf { (firstVisibleItemScrollOffset - offset) < 0 } }.value
}