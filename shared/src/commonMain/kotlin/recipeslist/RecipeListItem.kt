package recipeslist

/**
 * Created by abdulbasit on 18/06/2023.
 */

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Recipe

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeListItem(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    recipe: Recipe,
    onClick: (recipe: Recipe) -> Unit,
) {
    Box(modifier = Modifier) {
        Box(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth().aspectRatio(1.5f)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(35.dp),
                clip = true,
                ambientColor = Color(0xffCE5A01),
                spotColor = Color(0xffCE5A01)
            )
            .background(recipe.bgColor, RoundedCornerShape(35.dp)).fillMaxHeight().clickable {
                onClick(recipe)
            }) {
            with(sharedTransitionScope) {
                Card(
                    backgroundColor = recipe.bgColor,
                    shape = RoundedCornerShape(35.dp),
                    modifier = Modifier.clip(RoundedCornerShape(35.dp)).sharedElement(
                        state = rememberSharedContentState(
                            key = "item-container-${recipe.id}"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1.5f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxHeight().padding(16.dp).fillMaxWidth(0.55f),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.align(Alignment.Bottom)) {
                                Text(
                                    text = recipe.title,
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier.sharedElement(
                                        state = rememberSharedContentState(
                                            key = "item-title-${recipe.id}"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                )

                                Text(
                                    recipe.description,
                                    style = MaterialTheme.typography.subtitle1,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 8.dp).sharedElement(
                                        state = rememberSharedContentState(
                                            key = "recipe-description-${recipe.id}"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    RecipeListItemImageWrapper(modifier = Modifier.align(Alignment.BottomEnd)
                        .fillMaxWidth(0.45f).aspectRatio(1f), child = {
                        RecipeImage(
                            imageBitmap = recipe.image, modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = "item-image-${recipe.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            )
                        )
                    })
                }
            }
        }
    }
}
