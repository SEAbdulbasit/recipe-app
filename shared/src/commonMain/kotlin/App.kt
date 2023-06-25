import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import details.RecipeDetails
import model.recipesList
import recipeslist.RecipesListScreen

@Composable
fun App() {
    MaterialTheme {
        AnimatedListView()
    }
}

@Composable
fun AnimatedListView() {
    val items by remember { mutableStateOf(recipesList) }
    var width by remember { mutableStateOf(0) }
    val currentScreen = remember { mutableStateOf<Screens>(Screens.RecipesList) }
    Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
        width = it.size.width
    })

    when (val screen = currentScreen.value) {
        is Screens.RecipeDetails -> {
            RecipeDetails(screen.recipe, screen.imageBitmap)
        }

        Screens.RecipesList -> RecipesListScreen(
            items = items,
            width = width,
            onClick = { recipe, offset, size, imageBitmap ->
                currentScreen.value = Screens.RecipeDetails(
                    recipe = recipe,
                    imageBitmap = imageBitmap
                )
            })
    }
}