package recipeslist

/**
 * Created by abdulbasit on 18/06/2023.
 */

import CrossFadeTransitionSpec
import FadeOutTransitionSpec
import ListScreen
import MaterialFadeOutTransitionSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import sharedelementtransaction.SharedElement
import sharedelementtransaction.SharedMaterialContainer
import toImageBitmap


@OptIn(ExperimentalResourceApi::class)
@Composable
fun RecipeListItem(
    recipe: Recipe,
    updateIds: String,
    width: Int,
    onClick: (recipe: Recipe, bitmap: ImageBitmap) -> Unit,
) {
    val image = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        image.value = resource(recipe.image).readBytes().toImageBitmap()
    }

    Box(modifier = Modifier) {
        Box(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(35.dp),
                    clip = true,
                    ambientColor = Color(0xffCE5A01),
                    spotColor = Color(0xffCE5A01)
                ).width(width.dp)
                .aspectRatio(1.5f)
                .background(recipe.bgColor, RoundedCornerShape(35.dp))
                .fillMaxHeight()
                .clickable {
                    onClick(recipe, image.value!!)
                }
        ) {
            SharedMaterialContainer(
                key = "$recipe $updateIds",
                screenKey = ListScreen,
                shape = RoundedCornerShape(35.dp),
                color = recipe.bgColor,
                elevation = 0.dp,
                transitionSpec = MaterialFadeOutTransitionSpec
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxHeight().padding(16.dp).fillMaxWidth(0.55f),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(modifier = Modifier.align(Alignment.Bottom)) {
                            SharedElement(
                                key = "${recipe.title}${updateIds}",
                                screenKey = "ListScreen",
                                transitionSpec = CrossFadeTransitionSpec
                            ) {
                                Text(
                                    recipe.title,
                                    style = MaterialTheme.typography.h4,
                                )
                            }
                            SharedElement(
                                key = "${recipe.description}${updateIds}",
                                screenKey = "ListScreen",
                                transitionSpec = CrossFadeTransitionSpec
                            ) {
                                Text(
                                    recipe.description,
                                    style = MaterialTheme.typography.subtitle1,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        image.value?.let {
            RecipeListItemImageWrapper(modifier = Modifier.align(Alignment.BottomEnd)
                .fillMaxWidth(0.45f).aspectRatio(1f),
                child = {
                    SharedMaterialContainer(
                        key = "${recipe.image}${updateIds}",
                        screenKey = "ListScreen",
                        shape = CircleShape,
                        color = Color.Transparent,
                        transitionSpec = FadeOutTransitionSpec
                    ) {
                        RecipeImage(
                            it, Modifier
                        )
                    }
                })
        }
    }
}
