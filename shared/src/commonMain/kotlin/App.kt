import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import recipeslist.RecipesListScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        val items by remember { mutableStateOf(recipesList) }
        var width by remember { mutableStateOf(0) }
        val chefImage = remember { mutableStateOf<ImageBitmap?>(null) }


        val currentScreen = remember { mutableStateOf<Screens>(Screens.RecipesList) }
        Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
            width = it.size.width
        })

        when (val screen = currentScreen.value) {
            is Screens.RecipeDetails -> {
                LaunchedEffect(Unit) {
                    try {
                        chefImage.value = resource("chef.png").readBytes().toImageBitmap()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                RecipeDetails(screen.recipe, screen.imageBitmap, chefImage.value)
            }

            Screens.RecipesList -> RecipesListScreen(
                items = items,
                width = width,
                onClick = { recipe, offset, size, imageBitmap ->
                    currentScreen.value = Screens.RecipeDetails(
                        recipe = recipe,
                        imageBitmap = imageBitmap,
                    )
                })
        }
    }
}
