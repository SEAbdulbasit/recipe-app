package details


/**
 * Created by abdulbasit on 25/06/2023.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Recipe
import sugar

@Composable
fun InstructionItem(recipe: Recipe, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = if (recipe.bgColor == sugar) Color.Companion.Black else recipe.bgColor,
                shape = RoundedCornerShape(35.dp)
            )
    ) {
        Box(modifier = Modifier.graphicsLayer {
            translationY = -25f
        }) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(elevation = 10.dp, shape = CircleShape)
                    .background(
                        if (recipe.bgColor == sugar) Color.Companion.Black else recipe.bgColor,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.h5.copy(
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    ),
                    color = if (recipe.bgColor == sugar) Color.Companion.White else Color.Black,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(5.dp).rotate(-30f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            text = recipe.instructions[index],
            style = MaterialTheme.typography.body1.copy(
                letterSpacing = 1.2.sp
            ),
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight()
                .padding(20.dp),
        )
    }
}
