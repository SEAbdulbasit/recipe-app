package details


/**
 * Created by abdulbasit on 25/06/2023.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.Recipe

@Composable
fun InstructionItem(recipe: Recipe, index: Int) {
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
                text = recipe.instructions[index],
                style = MaterialTheme.typography.body1.copy(
                    letterSpacing = 1.2.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth().fillMaxHeight()
                    .padding(start = 70.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
            )
        }

        Box(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(elevation = 10.dp, shape = CircleShape)
                    .background(
                        recipe.bgColor,
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
                    color = Color.Black,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(5.dp).rotate(-30f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
