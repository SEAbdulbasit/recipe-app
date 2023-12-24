import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

/**
 * Created by abdulbasit on 08/10/2023.
 */

private val cache: MutableMap<String, Font> = mutableMapOf()


@OptIn(ExperimentalResourceApi::class)
actual suspend fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle,
    context: PlatformContext
): Font {
    return cache.getOrPut(res) {
        val byteArray = resource("font/$res.ttf").readBytes()
        androidx.compose.ui.text.platform.Font(res, byteArray, weight, style)
    }
}