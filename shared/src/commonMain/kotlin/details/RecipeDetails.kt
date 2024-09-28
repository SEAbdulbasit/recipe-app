package details

import RecipeDetailsLarge
import RecipeDetailsSmall
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import model.Recipe
import sensor.SensorManager

/**
 * Created by abdulbasit on 29/07/2023.
 */

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeDetails(
    recipe: Recipe,
    goBack: () -> Unit,
    sensorManager: SensorManager?,
    isLarge: Boolean,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransactionScope: SharedTransitionScope
) {
    if (isLarge) RecipeDetailsLarge(
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransactionScope = sharedTransactionScope,
        recipe = recipe,
        goBack = goBack,
        sensorManager = sensorManager
    )
    else RecipeDetailsSmall(
        animatedVisibilityScope = animatedVisibilityScope,
        sharedTransactionScope = sharedTransactionScope,
        recipe = recipe,
        goBack = goBack,
        sensorManager = sensorManager
    )
}