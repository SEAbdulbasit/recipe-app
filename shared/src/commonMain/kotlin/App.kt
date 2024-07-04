import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import details.RecipeDetails
import model.recipesList
import recipeslist.RecipesListScreen
import sensor.SensorManager

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(sensorManager: SensorManager, isLarge: Boolean = false) {

    val fontFamily = getFontFamily()
    val navController = rememberNavController()

    MaterialTheme(typography = getTypography(fontFamily)) {
        val items by remember { mutableStateOf(recipesList) }
        var width by remember { mutableStateOf(0) }
        var currentRecipe = items.first()

        Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
            width = it.size.width
        })

        SharedTransitionLayout {
            val sharedTransitionScope = this
            NavHost(
                navController = navController,
                startDestination = RecipeAppScreen.List.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = RecipeAppScreen.List.name) {
                    RecipesListScreen(animatedVisibilityScope = this,
                        sharedTransactionScope = sharedTransitionScope,
                        isLarge = isLarge,
                        items = items,
                        width = width,
                        onClick = { recipe ->
                            currentRecipe = recipe
                            navController.navigate(RecipeAppScreen.Details.name)
                        })
                }
                composable(route = RecipeAppScreen.Details.name) {
                    RecipeDetails(animatedVisibilityScope = this,
                        sharedTransactionScope = sharedTransitionScope,
                        isLarge = isLarge,
                        sensorManager = sensorManager,
                        recipe = currentRecipe,
                        goBack = { navController.popBackStack() })
                }
            }
        }
    }
}

enum class RecipeAppScreen(title: String) {
    List(title = "ListScreen"), Details(title = "DetailsScreen"),
}