/**
 * Created by abdulbasit on 18/06/2023.
 */

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun RecipeListItemWrapper(
    child: @Composable () -> Unit,
    scrollDirection: ScrollDirection = ScrollDirection.Backward
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = tween(
            durationMillis = 20000,
            easing = CubicBezierEasing(0f, 0.8f, 0.2f, 1f)
        )
    )
    val perspective by animateFloatAsState(
        targetValue = if (visible) 0f else perspectiveValue * getMultiplier(scrollDirection),
        animationSpec = tween(
            durationMillis = 2000,
            easing = CubicBezierEasing(0f, 0.8f, 0.2f, 1f)
        )
    )
    val alignment by animateFloatAsState(
        targetValue = if (visible) 0f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = CubicBezierEasing(0f, 0.8f, 0.2f, 1f)
        )
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(abs(perspective).dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = alignment
                cameraDistance = 300f
            }
    ) {
        child()
    }
}

enum class ScrollDirection {
    Forward,
    Backward
}

private const val perspectiveValue = 0.004f

private fun getMultiplier(scrollDirection: ScrollDirection) =
    if (scrollDirection == ScrollDirection.Forward) -1f else 1f

private fun getAlignmentValue(scrollDirection: ScrollDirection) =
    if (scrollDirection == ScrollDirection.Forward) Alignment.Bottom else Alignment.Top
