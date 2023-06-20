/**
 * Created by abdulbasit on 18/06/2023.
 */

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun RecipeListItemWrapper(
    child: @Composable () -> Unit,
    scrollDirection: ScrollDirection = ScrollDirection.Backward
) {
    var visible by remember { mutableStateOf(false) }
    val camera by animateFloatAsState(
        targetValue = if (visible) 8.0f else 7.0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = CubicBezierEasing(0f, 0.8f, 0.2f, 1f)
        )
    )
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = CubicBezierEasing(0f, 0.8f, 0.2f, 1f)
        )
    )
    val perspective by animateFloatAsState(
        targetValue = if (visible) 0f else {
            when (scrollDirection) {
                ScrollDirection.Forward -> 60f
                ScrollDirection.Backward -> 60f
            }
        },
        animationSpec = tween(
            durationMillis = 1000,
            easing = CubicBezierEasing(0f, 0.8f, 0.2f, 1f)
        )
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationX = perspective
                cameraDistance = camera
            }
    ) {
        child()
    }
}

enum class ScrollDirection {
    Forward,
    Backward
}