import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Created by abdulbasit on 08/10/2023.
 */
actual suspend fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle,
    context: PlatformContext
): Font = androidx.compose.ui.text.platform.Font("font/$res.ttf", weight, style)