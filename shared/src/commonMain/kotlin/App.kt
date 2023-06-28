import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import recipeslist.RecipesListScreen
import sharedelementtransaction.FadeMode
import sharedelementtransaction.MaterialArcMotionFactory
import sharedelementtransaction.MaterialContainerTransformSpec
import sharedelementtransaction.ProgressThresholds
import sharedelementtransaction.SharedElementsRoot
import sharedelementtransaction.SharedElementsTransitionSpec

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        val items by remember { mutableStateOf(recipesList) }
        var width by remember { mutableStateOf(0) }
        val listState = rememberLazyListState()
        val stableItems = remember(items) { items.toList() }
        val updateIds = remember { mutableStateOf("") }
        val chefImage = remember { mutableStateOf<ImageBitmap?>(null) }
        LaunchedEffect(Unit) {
            try {
                chefImage.value = resource("chef.png").readBytes().toImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val currentScreen = remember { mutableStateOf<Screens>(Screens.RecipesList) }
        Box(modifier = Modifier.fillMaxSize().onGloballyPositioned {
            width = it.size.width
        })

        SharedElementsRoot {
            val sharedTracnaction = this
            Box {
                RecipesListScreen(
                    items = stableItems,
                    width = width,
                    listState = listState,
                    updateIds = updateIds.value,
                    onClick = { recipe, offset, size, imageBitmap ->
                        prepareTransition(
                            recipe.id,
                            recipe.description,
                            recipe.title,
                            recipe.image
                        )
                        updateIds.value = "abc"

                        currentScreen.value = Screens.RecipeDetails(
                            recipe = recipe,
                            imageBitmap = imageBitmap,
                        )
                    })

                when (val screen = currentScreen.value) {
                    is Screens.RecipeDetails -> {
                        RecipeDetails(
                            recipe = screen.recipe,
                            imageBitmap = screen.imageBitmap,
                            chefImage = chefImage.value,
                            goBack = {
                                updateIds.value = ""
                                GlobalScope.launch {
                                    delay(100)
                                    sharedTracnaction.prepareTransition()
                                    currentScreen.value = Screens.RecipesList
                                }
                            }
                        )
                    }

                    Screens.RecipesList -> Unit
                }
            }
        }
    }
}


private const val ListScreen = "list"
private const val DetailsScreen = "details"

private const val TransitionDurationMillis = 1000

val FadeOutTransitionSpec = MaterialContainerTransformSpec(
    durationMillis = TransitionDurationMillis,
    fadeMode = FadeMode.Out
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

