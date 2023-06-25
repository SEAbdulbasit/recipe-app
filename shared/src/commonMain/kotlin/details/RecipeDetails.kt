package details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import sugar
import toImageBitmap


/**
 * Created by abdulbasit on 25/06/2023.
 */

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RecipeDetails(recipe: Recipe, imageBitmap: ImageBitmap) {
    val backgroundImage = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        try {
            backgroundImage.value = resource(recipe.bgImageName).readBytes().toImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(color = sugar)) {
        Column {
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(35.dp),
                        clip = false,
                        ambientColor = Color(0xffCE5A01),
                        spotColor = Color(0xffCE5A01)
                    ).fillMaxWidth().aspectRatio(1f)
                    .background(
                        recipe.bgColor,
                        RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp)
                    ).fillMaxHeight(),
            ) {
                backgroundImage.value?.let {
                    Image(
                        bitmap = it,
                        contentDescription = null,
                    )
                }

                Image(
                    bitmap = imageBitmap,
                    contentDescription = null,
                    modifier = Modifier.padding(64.dp)
                )
            }
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
//            Text(
//                text = "INGREDIENTS",
//                style = MaterialTheme.typography.h6,
//                fontWeight = FontWeight.W700,
//                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
//            )
//
//            LazyColumn {
//                items(recipe.ingredients) {
//                    IngredientItem(recipe, it)
//                }
//            }
            Text(
                text = "STEPS",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.W700,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            )

            LazyColumn {
                items(recipe.ingredients) {
                    InstructionItem(recipe, recipe.ingredients.indexOf(it))
                }
            }
        }
    }
}
