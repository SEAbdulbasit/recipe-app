import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import recipeappkmp.shared.generated.resources.Res
import recipeappkmp.shared.generated.resources.rubik_bold
import recipeappkmp.shared.generated.resources.rubik_light
import recipeappkmp.shared.generated.resources.rubik_medium
import recipeappkmp.shared.generated.resources.rubik_regular

/**
 * Created by abdulbasit on 04/07/2024.
 */

@Composable
fun getFontFamily(): FontFamily {
    val rubikFamily = FontFamily(
        listOf(
            Font(Res.font.rubik_light),
            Font(Res.font.rubik_medium),
            Font(Res.font.rubik_regular),
            Font(Res.font.rubik_bold)
        )
    )
    return rubikFamily
}

fun getTypography(fontFamily: FontFamily): Typography {
    return Typography(
        h1 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
            letterSpacing = (-1.5).sp,
            fontFamily = fontFamily
        ),
        h2 = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 35.sp,
            letterSpacing = (-0.5).sp,
            fontFamily = fontFamily
        ),
        h3 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp,
            letterSpacing = 0.sp,
            fontFamily = fontFamily
        ),
        h4 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 25.sp,
            letterSpacing = 0.25.sp,
            fontFamily = fontFamily
        ),
        h5 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            letterSpacing = 0.sp,
            fontFamily = fontFamily
        ),
        h6 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp,
            fontFamily = fontFamily
        ),
        subtitle1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp,
            fontFamily = fontFamily
        ),
        subtitle2 = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp,
            fontFamily = fontFamily
        ),
        body1 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp,
            fontFamily = fontFamily
        ),
        body2 = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp,
            fontFamily = fontFamily
        ),
        button = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 1.25.sp,
            fontFamily = fontFamily
        ),
        caption = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp,
            fontFamily = fontFamily
        ),
        overline = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            fontFamily = fontFamily
        )
    )
}