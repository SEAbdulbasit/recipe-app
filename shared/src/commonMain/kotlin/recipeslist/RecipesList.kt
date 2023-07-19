package recipeslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import model.Recipe
import sugar


/**
 * Created by abdulbasit on 25/06/2023.
 */

@Composable
fun RecipesListScreen(
    items: List<Recipe>, width: Int,
    updateIds: String,
    onClick: (recipe: Recipe, offset: Offset, size: Int, imageBitmap: ImageBitmap) -> Unit,
) {
    val listState = rememberLazyListState()
    LazyColumn(modifier = Modifier.fillMaxSize().background(sugar), state = listState) {
        items(items) { item ->
            RecipeListItemWrapper(
                scrollDirection = listState.isScrollingUp(),
                child = {
                    RecipeListItem(
                        recipe = item, width = width, onClick = onClick, updateIds = updateIds
                    )
                },
            )
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}