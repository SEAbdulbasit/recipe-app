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
import details.RecipeDetails
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import recipeappkmp.shared.generated.resources.*
import recipeslist.RecipesListScreen
import sensor.SensorManager
import sharedelementtransaction.*

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(sensorManager: SensorManager, isLarge: Boolean = false) {
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
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
            letterSpacing = (-1.5).sp,
            fontFamily = rubikFamily
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 35.sp,
            letterSpacing = (-0.5).sp,
            fontFamily = rubikFamily
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp,
            letterSpacing = 0.sp,
            fontFamily = rubikFamily
        ),
        h4 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 25.sp,
            letterSpacing = 0.25.sp,
            fontFamily = rubikFamily
        ),
        h5 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            letterSpacing = 0.sp,
            fontFamily = rubikFamily
        ),
        h6 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp,
            fontFamily = rubikFamily
        ),
        subtitle1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
            fontFamily = rubikFamily
        ),
        subtitle2 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp,
            fontFamily = rubikFamily
        ),
        body1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp,
            fontFamily = rubikFamily
        ),
        body2 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp,
            fontFamily = rubikFamily
        ),
        button = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp,
            fontFamily = rubikFamily
        ),
        caption = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp,
            fontFamily = rubikFamily
        ),
        overline = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = rubikFamily
        )
    )

    MaterialTheme(typography = typo) {
        val items by remember { mutableStateOf(recipesList) }
        var width by remember { mutableStateOf(0) }
        var currentScreen by remember { mutableStateOf<Screens>(Screens.RecipesList) }
        var updateIds by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
            width = it.size.width
        })

        SharedElementsRoot {
            val sharedTransaction = this
            Box {
                RecipesListScreen(isLarge = isLarge,
                    items = items,
                    width = width,
                    updateIds = updateIds,
                    onClick = { recipe ->
                        prepareTransition(
                            recipe.id, recipe.description, recipe.title, recipe.image
                        )
                        updateIds = "update_dummy_ids"
                        currentScreen = Screens.RecipeDetails(
                            recipe = recipe,
                        )
                    })

                when (val screen = currentScreen) {
                    is Screens.RecipeDetails -> {
                        RecipeDetails(isLarge = isLarge,
                            sensorManager = sensorManager,
                            recipe = screen.recipe,
                            goBack = {
                                updateIds = ""
                                sharedTransaction.prepareTransition()
                                prepareTransition(
                                    screen.recipe.id,
                                    screen.recipe.description,
                                    screen.recipe.title,
                                    screen.recipe.image
                                )
                                currentScreen = Screens.RecipesList
                            })

                    }

                    Screens.RecipesList -> {
                        // do nothing
                    }
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
    pathMotionFactory = MaterialArcMotionFactory,
    durationMillis = TransitionDurationMillis,
    fadeMode = FadeMode.In
)
val MaterialFadeOutTransitionSpec = MaterialContainerTransformSpec(
    pathMotionFactory = MaterialArcMotionFactory,
    durationMillis = TransitionDurationMillis,
    fadeMode = FadeMode.Out
)

