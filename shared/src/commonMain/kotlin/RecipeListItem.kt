/**
 * Created by abdulbasit on 18/06/2023.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun RecipeListItem(
    recipe: Recipe,
    width: Int
) {
    Box(modifier = Modifier) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(35.dp),
                    clip = false,
                    ambientColor = Color(0xffCE5A01),
                    spotColor = Color(0xffCE5A01)
                ).width(width.dp).aspectRatio(1.5f)
                .background(recipe.bgColor, RoundedCornerShape(35.dp)).fillMaxHeight(),
        ) {
            Row(
                modifier = Modifier.fillMaxHeight().padding(16.dp).fillMaxWidth(0.55f),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.align(Alignment.Bottom)) {
                    Text(
                        recipe.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W700
                    )
                    Text(
                        recipe.description,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        val image = remember { mutableStateOf<ImageBitmap?>(null) }

        LaunchedEffect(Unit) {
            image.value = resource(recipe.image).readBytes().toImageBitmap()
        }

        image.value?.let {
            RecipeListItemImageWrapper(
                modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth(0.45f).aspectRatio(1f),
                child = {
                    RecipeImage(
                        it,
                        Modifier
                    )
                })
        }
    }
}