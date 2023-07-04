import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import details.IngredientItem
import details.InstructionItem
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import sharedelementtransaction.SharedElement
import sharedelementtransaction.SharedMaterialContainer
import kotlin.math.PI


@OptIn(
    ExperimentalResourceApi::class, ExperimentalResourceApi::class,
    ExperimentalFoundationApi::class, ExperimentalAnimationApi::class
)
@Composable
fun RecipeDetails(
    recipe: Recipe,
    imageBitmap: ImageBitmap,
    chefImage: ImageBitmap?,
    goBack: () -> Unit
) {
    val backgroundImage = remember { mutableStateOf<ImageBitmap?>(null) }
    val imageRotation = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        try {
            backgroundImage.value = resource(recipe.bgImageName).readBytes().toImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val toolbarOffsetHeightPx = remember { mutableStateOf(340f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(0f, 340f)
                imageRotation.value += (available.y * 0.5).toInt()
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                imageRotation.value += ((delta * PI / 180) * 10).toInt()
                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                imageRotation.value += available.y.toInt()

                return super.onPreFling(available)
            }
        }
    }

    val candidateHeight = maxOf(toolbarOffsetHeightPx.value, 200f)
    val listState = rememberLazyListState()
    val (fraction, setFraction) = remember { mutableStateOf(1f) }

    Box(
        modifier = Modifier.fillMaxSize().background(color = sugar)
            .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {

            stickyHeader {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(35.dp),
                            clip = false,
                            ambientColor = Color(0xffCE5A01).copy(if (fraction < 0.1) 1f - fraction else 0f),
                            spotColor = Color(0xffCE5A01).copy(if (fraction < 0.1) 1f - fraction else 0f)
                        ).alpha(if (fraction < 0.2) 1f - fraction else 0f)
                        .fillMaxWidth()
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(
                                bottomEnd = 35.dp, bottomStart = 35.dp
                            ),
                        ).clip(
                            RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp),
                        ).height(candidateHeight.dp),
                ) {
                    SharedMaterialContainer(
                        key = "$recipe ",
                        screenKey = DetailsScreen,
                        color = recipe.bgColor,
                        shape = RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp),
                        onFractionChanged = setFraction,
                        transitionSpec = MaterialFadeInTransitionSpec
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            backgroundImage.value?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.background(
                                        Color.Transparent,
                                        RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp),
                                    ),
                                    alpha = 1 - fraction
                                )
                            }
                            Box(
                                modifier = Modifier.padding(16.dp).aspectRatio(1f)
                                    .align(Alignment.Center)
                            ) {
                                SharedMaterialContainer(
                                    key = recipe.image,
                                    screenKey = "DetailsScreen",
                                    color = Color.Transparent,
                                    transitionSpec = FadeOutTransitionSpec
                                ) {
                                    Image(
                                        bitmap = imageBitmap,
                                        contentDescription = null,
                                        modifier = Modifier.aspectRatio(1f)
                                            .align(Alignment.Center)
                                            .rotate(imageRotation.value.toFloat())
                                    )
                                }
                            }
                        }
                    }
                }
            }


            item {
                SharedElement(
                    key = recipe.title,
                    screenKey = "DetailsScreen",
                    transitionSpec = CrossFadeTransitionSpec,
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    )
                }

                SharedElement(
                    key = recipe.description,
                    screenKey = "DetailsScreen",
                    transitionSpec = CrossFadeTransitionSpec
                ) {

                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    )
                }

                AnimateInEffect(
                    recipe = recipe,
                    intervalStart = 0 / (recipe.instructions.size + recipe.ingredients.size + 2).toFloat(),
                    content = {
                        Text(
                            text = "INGREDIENTS",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.W700,
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        )
                    })
            }

            itemsIndexed(recipe.ingredients) { index, value ->
                AnimateInEffect(
                    intervalStart = (index + 1) / (recipe.instructions.size + recipe.ingredients.size + 1).toFloat(),
                    recipe = recipe,
                    content = {
                        IngredientItem(recipe, value, chefImage)
                    }
                )
            }

            item {
                AnimateInEffect(
                    recipe = recipe,
                    intervalStart = (recipe.ingredients.size + 1) / (recipe.instructions.size + recipe.ingredients.size + 2).toFloat(),
                    content = {
                        Text(
                            text = "STEPS",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.W700,
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        )
                    }
                )
            }

            itemsIndexed(recipe.instructions) { index, item ->
                AnimateInEffect(
                    recipe = recipe,
                    intervalStart = (recipe.ingredients.size + index + 1) / (recipe.instructions.size + recipe.ingredients.size + 1).toFloat(),
                    content = {
                        InstructionItem(recipe, index)
                    })
            }
        }

        Box(
            modifier = Modifier.padding(10.dp).background(
                color = Color.Black,
                shape = RoundedCornerShape(50)
            ).shadow(elevation = 16.dp).padding(5.dp).clickable {
                goBack()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = recipe.bgColor,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


