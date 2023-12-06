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
): Font {
    val id = context.androidContext.resources.getIdentifier(res, "font", context.androidContext.packageName)
    return Font(id, weight, style)
}