package sharedelementtransaction

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import kotlin.math.max
import kotlin.math.min

@Composable
internal fun ElementContainer(
    modifier: Modifier,
    relaxMaxSize: Boolean = false,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        if (measurables.size > 1) {
            throw IllegalStateException("SharedElement can have only one direct measurable child!")
        }
        val placeable = measurables.firstOrNull()?.measure(
            Constraints(
                minWidth = 0,
                minHeight = 0,
                maxWidth = if (relaxMaxSize) Constraints.Infinity else constraints.maxWidth,
                maxHeight = if (relaxMaxSize) Constraints.Infinity else constraints.maxHeight
            )
        )
        val width = min(max(constraints.minWidth, placeable?.width ?: 0), constraints.maxWidth)
        val height = min(max(constraints.minHeight, placeable?.height ?: 0), constraints.maxHeight)
        layout(width, height) {
            placeable?.place(0, 0)
        }
    }
}
