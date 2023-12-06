import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

/**
 * Created by abdulbasit on 08/10/2023.
 */
@OptIn(ExperimentalResourceApi::class)
actual suspend fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle,
    context: PlatformContext
): Font {
    return androidx.compose.ui.text.platform.Font(
        identity = name,
        data = resource("font/$res.ttf").readBytes(),
        weight = weight,
        style = style
    )
}