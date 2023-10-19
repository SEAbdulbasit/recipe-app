import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Created by abdulbasit on 08/10/2023.
 */
@Composable
actual fun font(
    name: String,
    res: String,
    weight: FontWeight,
    style: FontStyle
) = androidx.compose.ui.text.platform.Font("font/$res.ttf", weight, style)