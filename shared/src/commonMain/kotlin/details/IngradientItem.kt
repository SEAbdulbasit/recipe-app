package details


/**
 * Created by abdulbasit on 25/06/2023.
 */
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun IngredientItem(recipe: Recipe, ingredient: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 15.dp)
            .border(
                width = 2.dp,
                color = recipe.bgColor,
                shape = RoundedCornerShape(35.dp)
            )

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.graphicsLayer {
                translationY = -25f
            }) {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = CircleShape,
                            clip = false,
                            ambientColor = Color(0xffCE5A01),
                            spotColor = Color(0xffCE5A01)
                        )
                        .size(50.dp)
                        .background(recipe.bgColor, CircleShape)
                ) {
                    Image(
                        painter = painterResource("compose-multiplatform.xml"),
                        contentDescription = null,
                        modifier = Modifier.padding(13.dp).rotate(45f),
                    )
                }
            }

            Text(
                text = ingredient,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
