import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import details.RecipeDetails
import model.Recipe
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import recipeappkmp.shared.generated.resources.*
import recipeslist.RecipesListScreen
import sensor.SensorManager
import sharedelementtransaction.*


@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(
    sensorManager: SensorManager, isLarge: Boolean = false, navController: NavHostController = rememberNavController()
) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = RecipeAppScreen.valueOf(
        backStackEntry?.destination?.route ?: RecipeAppScreen.List.name
    )
    val rubikFamily = FontFamily(
        listOf(
            Font(Res.font.rubik_light),
            Font(Res.font.rubik_medium),
            Font(Res.font.rubik_regular),
            Font(Res.font.rubik_bold)
        )
    )

    val typo = Typography(
        h1 = TextStyle(
            fontWeight = FontWeight.Light, fontSize = 45.sp, letterSpacing = (-1.5).sp, fontFamily = rubikFamily
        ), h2 = TextStyle(
            fontWeight = FontWeight.Light, fontSize = 35.sp, letterSpacing = (-0.5).sp, fontFamily = rubikFamily
        ), h3 = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 30.sp, letterSpacing = 0.sp, fontFamily = rubikFamily
        ), h4 = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 25.sp, letterSpacing = 0.25.sp, fontFamily = rubikFamily
        ), h5 = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 20.sp, letterSpacing = 0.sp, fontFamily = rubikFamily
        ), h6 = TextStyle(
            fontWeight = FontWeight.Medium, fontSize = 20.sp, letterSpacing = 0.15.sp, fontFamily = rubikFamily
        ), subtitle1 = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 0.15.sp, fontFamily = rubikFamily
        ), subtitle2 = TextStyle(
            fontWeight = FontWeight.Medium, fontSize = 14.sp, letterSpacing = 0.1.sp, fontFamily = rubikFamily
        ), body1 = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 0.5.sp, fontFamily = rubikFamily
        ), body2 = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 14.sp, letterSpacing = 0.25.sp, fontFamily = rubikFamily
        ), button = TextStyle(
            fontWeight = FontWeight.Medium, fontSize = 14.sp, letterSpacing = 1.25.sp, fontFamily = rubikFamily
        ), caption = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 12.sp, letterSpacing = 0.4.sp, fontFamily = rubikFamily
        ), overline = TextStyle(
            fontWeight = FontWeight.Normal, fontSize = 10.sp, letterSpacing = 1.5.sp, fontFamily = rubikFamily
        )
    )

    MaterialTheme(typography = typo) {
        val items by remember { mutableStateOf(recipesList) }
        var width by remember { mutableStateOf(0) }
        var screenDetails: Recipe? = recipesList.first()

        Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
            width = it.size.width
        })

        SharedElementsRoot {
            val sharedTransaction = this
            NavHost(
                navController = navController,
                startDestination = RecipeAppScreen.List.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = RecipeAppScreen.List.name) {
                    RecipesListScreen(isLarge = isLarge, items = items, width = width, onClick = { recipe ->
                        navController.navigate(RecipeAppScreen.Details.name)
                        screenDetails = recipe
                        prepareTransition(
                            recipe.id, recipe.description, recipe.title, recipe.image
                        )
                    })
                }
                composable(route = RecipeAppScreen.Details.name) {
                    RecipeDetails(isLarge = isLarge, sensorManager = sensorManager, recipe = screenDetails!!, goBack = {
                        navController.popBackStack(RecipeAppScreen.Details.name, true)
                        sharedTransaction.prepareTransition()
                        prepareTransition(
                            screenDetails!!.id,
                            screenDetails!!.description,
                            screenDetails!!.title,
                            screenDetails!!.image
                        )
                    })
                }
            }
        }
    }
}


const val ListScreen = "list"
const val DetailsScreen = "details"

private const val TransitionDurationMillis = 700

val FadeOutTransitionSpec = MaterialContainerTransformSpec(
    durationMillis = TransitionDurationMillis, fadeMode = FadeMode.Out
)
val CrossFadeTransitionSpec = SharedElementsTransitionSpec(
    durationMillis = TransitionDurationMillis,
    fadeMode = FadeMode.Cross,
    fadeProgressThresholds = ProgressThresholds(0.10f, 0.40f)
)
val MaterialFadeInTransitionSpec = MaterialContainerTransformSpec(
    pathMotionFactory = MaterialArcMotionFactory, durationMillis = TransitionDurationMillis, fadeMode = FadeMode.In
)
val MaterialFadeOutTransitionSpec = MaterialContainerTransformSpec(
    pathMotionFactory = MaterialArcMotionFactory, durationMillis = TransitionDurationMillis, fadeMode = FadeMode.Out
)

enum class RecipeAppScreen(title: String) {
    List(title = ListScreen), Details(title = DetailsScreen),
}

