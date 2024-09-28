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
const val rotateX = 9f


@Composable
fun RecipeListItemWrapper(
    child: @Composable () -> Unit,
    scrollDirection: Boolean
) {
    val scaleAnimatable = remember { Animatable(initialValue = 0.75f) }
    val rotateXAnimatable =
        remember { Animatable(initialValue = if (scrollDirection) rotateX else -rotateX) }

    // Observe changes to scrollDirection and update rotateXAnimatable accordingly
    LaunchedEffect(scrollDirection) {
        // Animate from 0 to either 60 or -60
        rotateXAnimatable.animateTo(
            if (scrollDirection) rotateX else -rotateX,
            animationSpec = tween(
                durationMillis = 100,
                easing = CubicBezierEasing(0f, 0.5f, 0.5f, 1f)
            )
        )
        // Animate from either 60 or -60 to 0
        rotateXAnimatable.animateTo(
            targetValue = 0f,
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
            }
    ) {
        child()
    }
}
