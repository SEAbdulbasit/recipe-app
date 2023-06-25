package details

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha


/**
 * Created by abdulbasit on 25/06/2023.
 */

@ExperimentalAnimationApi
@Composable
fun FadeInEffect(
    intervalStart: Int = 500,
    content: @Composable () -> Unit
) {

    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = intervalStart, easing = LinearEasing)
        )
    }

    Box(modifier = Modifier.alpha(alpha.value)) {
        content()
    }
}