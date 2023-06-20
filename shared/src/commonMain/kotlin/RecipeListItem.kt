/**
 * Created by abdulbasit on 18/06/2023.
 */

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RecipeListItem(
    recipe: Recipe
) {
    Box {
        Box(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
                .background(recipe.bgColor, RoundedCornerShape(16.dp))
                .height(220.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxHeight().padding(16.dp).fillMaxWidth(0.5f),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.align(Alignment.Bottom)) {
                    Text("Headline", style = MaterialTheme.typography.h4)
                    Text(
                        "Sub Headline This is a sub headline",
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        val image = remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(Unit) {
            image.value = resource("01-lemon-cheesecake.png").readBytes().toImageBitmap()
        }

        image.value?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(0.5f).aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
}