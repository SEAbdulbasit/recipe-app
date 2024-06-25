package details


/**
 * Created by abdulbasit on 25/06/2023.
 */
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import recipeappkmp.shared.generated.resources.Res
import recipeappkmp.shared.generated.resources.chef

@OptIn(ExperimentalResourceApi::class)
@Composable
fun IngredientItem(recipe: Recipe, ingredient: String) {
    Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .border(
                    width = 2.dp,
                    color = recipe.bgColor,
                    shape = RoundedCornerShape(35.dp)
                )
        ) {
            Text(
                text = ingredient,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 55.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .shadow(elevation = 10.dp, shape = CircleShape)
                    .background(
                        recipe.bgColor,
                        CircleShape
                    ),
            ) {
                Image(
                    painter = painterResource(Res.drawable.chef),
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp).rotate(-30f),
                    colorFilter = ColorFilter.tint(if (recipe.bgColor.luminance() > 0.3) Color.Companion.Black else Color.White)
                )

            }
        }
    }
}
