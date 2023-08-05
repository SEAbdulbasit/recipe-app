package recipeslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Created by abdulbasit on 20/06/2023.
 */

@Composable
fun RecipeImage(imageBitmap: ImageBitmap, modifier: Modifier) {
    Box(modifier = modifier) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(50))
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(50)
                )
        )
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier.background(
                color = Color(0xffCE5A01).copy(alpha = 0.2f),
                shape = CircleShape,
            ).aspectRatio(1f)
        )
    }
}