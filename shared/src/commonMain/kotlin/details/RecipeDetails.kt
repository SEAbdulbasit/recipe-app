package details

import RecipeDetailsLarge
import RecipeDetailsSmall
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import model.Recipe
import sensor.SensorManager


/**
 * Created by abdulbasit on 29/07/2023.
 */

@Composable
fun RecipeDetails(
    recipe: Recipe,
    imageBitmap: ImageBitmap,
    chefImage: ImageBitmap?,
    goBack: () -> Unit,
    sensorManager: SensorManager,
    isLarge: Boolean
) {
    if (isLarge)
        RecipeDetailsLarge(
            recipe = recipe,
            imageBitmap = imageBitmap,
            chefImage = chefImage,
            goBack = goBack,
            sensorManager = sensorManager
        )
    else
        RecipeDetailsSmall(
            recipe = recipe,
            imageBitmap = imageBitmap,
            chefImage = chefImage,
            goBack = goBack,
            sensorManager = sensorManager
        )
}