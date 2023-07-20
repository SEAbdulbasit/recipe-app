package recipeslist

/**
 * Created by abdulbasit on 18/06/2023.
 */

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

const val perspectiveValue = 0.004

@Composable
fun RecipeListItemWrapper(
    child: @Composable () -> Unit,
    scrollDirection: Boolean
) {
    val cameraAnimatable = remember { Animatable(initialValue = 7.0f) }
    val scaleAnimatable = remember { Animatable(initialValue = 0.7f) }
    val rotateXAnimatable = Animatable(initialValue = if (scrollDirection) 45f else -45f)

    // Observe changes to scrollDirection and update rotateXAnimatable accordingly
    LaunchedEffect(scrollDirection) {
        rotateXAnimatable.animateTo(
            0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
    }

    // Other animations (camera and scale) remain the same
    LaunchedEffect(Unit) {
        cameraAnimatable.animateTo(
            8.0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
    }

    LaunchedEffect(Unit) {
        scaleAnimatable.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scaleAnimatable.value
                scaleY = scaleAnimatable.value
                rotationX = rotateXAnimatable.value
                cameraDistance = cameraAnimatable.value
            }
    ) {
        child()
    }
}
