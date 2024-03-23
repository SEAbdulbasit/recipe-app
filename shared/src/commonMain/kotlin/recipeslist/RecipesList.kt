package recipeslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import model.Recipe
import sugar


/**
 * Created by abdulbasit on 25/06/2023.
 */

@Composable
fun RecipesListScreen(
    items: List<Recipe>, width: Int,
    onClick: (recipe: Recipe) -> Unit,
    isLarge: Boolean,
) {
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().background(sugar),
        state = listState,
        columns = GridCells.Fixed(if (isLarge) 3 else 1)
    ) {
        items(items.size) { item ->
            RecipeListItemWrapper(
                scrollDirection = listState.isScrollingUp(),
                child = {
                    RecipeListItem(
                        recipe = items[item],
                        width = width,
                        onClick = onClick,
                    )
                },
            )
        }
    }
}

@Composable
private fun LazyGridState.isScrollingUp(): Boolean {
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