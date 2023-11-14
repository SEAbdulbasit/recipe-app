import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

/**
 * Created by abdulbasit on 08/10/2023.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
): Font {
    val scope = rememberCoroutineScope()
    scope.launch {
        val fontsArray = getFontsArray(name)
        println("Byate array is $fontsArray")
    }

    return androidx.compose.ui.text.platform.Font(
        identity = "font/$res.ttf",
        weight = weight,
        style = style,
        data = byteArrayOf()
    )
}

@OptIn(ExperimentalResourceApi::class)
suspend fun getFontsArray(res: String): ByteArray {
    return resource("font/$res.ttf").readBytes()
}
