import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import details.RecipeDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import recipeslist.RecipesListScreen
import sensor.SensorManager
import sharedelementtransaction.FadeMode
import sharedelementtransaction.MaterialArcMotionFactory
import sharedelementtransaction.MaterialContainerTransformSpec
import sharedelementtransaction.ProgressThresholds
import sharedelementtransaction.SharedElementsRoot
import sharedelementtransaction.SharedElementsTransitionSpec

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(sensorManager: SensorManager, isLarge: Boolean = false) {

    val rubikRegular = FontFamily(
        font(
            "Rubik", "rubik_regular", FontWeight.Normal, FontStyle.Normal
        )
    )

    val rubikSemiBold = FontFamily(
        font(
            "Rubik", "rubik_medium", FontWeight.SemiBold, FontStyle.Normal
        )
    )
    val rubikBold = FontFamily(
        font(
            "Rubik", "rubik_bold", FontWeight.Bold, FontStyle.Normal
        )
    )
    val typo = Typography(
        h1 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
            letterSpacing = (-1.5).sp,
            fontFamily = rubikBold
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 35.sp,
            letterSpacing = (-0.5).sp,
            fontFamily = rubikBold
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp,
            letterSpacing = 0.sp,
            fontFamily = rubikBold
        ),
        h4 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 25.sp,
            letterSpacing = 0.25.sp,
            fontFamily = rubikBold
        ),
        h5 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            letterSpacing = 0.sp,
            fontFamily = rubikBold
        ),
        h6 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp,
            fontFamily = rubikSemiBold
        ),
        subtitle1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
            fontFamily = rubikSemiBold
        ),
        subtitle2 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp,
            fontFamily = rubikSemiBold
        ),
        body1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp,
            fontFamily = rubikRegular
        ),
        body2 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp,
            fontFamily = rubikRegular
        ),
        button = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp,
            fontFamily = rubikRegular
        ),
        caption = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp,
            fontFamily = rubikRegular
        ),
        overline = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = rubikRegular
        )
    )

    MaterialTheme(typography = typo) {
        val items by remember { mutableStateOf(recipesList) }
        var width by remember { mutableStateOf(0) }
        var currentScreen by remember { mutableStateOf<Screens>(Screens.RecipesList) }
        var updateIds by remember { mutableStateOf("") }

        val chefImage = remember { mutableStateOf<ImageBitmap?>(null) }
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                chefImage.value = resource("chef.png").readBytes().toImageBitmap()
            }
        }

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
                    onClick = { recipe, imageBitmap ->
                        prepareTransition(
                            recipe.id, recipe.description, recipe.title, recipe.image
                        )
                        updateIds = "update_dummy_ids"
                        currentScreen = Screens.RecipeDetails(
                            recipe = recipe,
                            imageBitmap = imageBitmap,
                        )
                    })

                when (val screen = currentScreen) {
                    is Screens.RecipeDetails -> {
                        RecipeDetails(isLarge = isLarge,
                            sensorManager = sensorManager,
                            recipe = screen.recipe,
                            imageBitmap = screen.imageBitmap,
                            chefImage = chefImage.value,
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

