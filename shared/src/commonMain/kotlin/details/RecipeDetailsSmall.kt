import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import details.StepsAndDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import sensor.Listener
import sensor.SensorData
import sensor.SensorManager
import sharedelementtransaction.SharedMaterialContainer
import kotlin.math.PI


@OptIn(
    ExperimentalResourceApi::class, ExperimentalResourceApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun RecipeDetailsSmall(
    recipe: Recipe,
    imageBitmap: ImageBitmap,
    chefImage: ImageBitmap?,
    goBack: () -> Unit,
    sensorManager: SensorManager
) {
    val backgroundImage = remember { mutableStateOf<ImageBitmap?>(null) }
    val blurBackgroundImage = remember { mutableStateOf<ImageBitmap?>(null) }
    val imageRotation = remember { mutableStateOf(0) }
    val sensorDataLive = remember { mutableStateOf(SensorData(0.0f, 0.0f)) }
    val roll by derivedStateOf { (sensorDataLive.value.roll).coerceIn(-3f, 3f) }
    val pitch by derivedStateOf { (sensorDataLive.value.pitch).coerceIn(-2f, 2f) }

    val tweenDuration = 300

    sensorManager.registerListener(object : Listener {
        override fun onUpdate(sensorData: SensorData) {
            sensorDataLive.value = sensorData
        }
    })

    val backgroundShadowOffset = animateIntOffsetAsState(
        targetValue = IntOffset((roll * 6f).toInt(), (pitch * 6f).toInt()),
        animationSpec = tween(tweenDuration)
    )
    val backgroundImageOffset = animateIntOffsetAsState(
        targetValue = IntOffset(-roll.toInt(), pitch.toInt()),
        animationSpec = tween(tweenDuration)
    )

    val context = getPlatformContext()

    LaunchedEffect(recipe.bgColor) {
        withContext(Dispatchers.IO) {
            if (recipe.bgImageName.isNotEmpty()) {
                val backgroundBitmap = resource(recipe.bgImageName).readBytes().toImageBitmap()
                blurBackgroundImage.value = blurFilter(backgroundBitmap, context)
                backgroundImage.value = backgroundBitmap
            }
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
        modifier = Modifier.fillMaxSize()
            .background(color = if (recipe.bgColor == sugar) yellow else sugar)
        // .nestedScroll(nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection),
            state = listState
        ) {

            stickyHeader {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = if (fraction < 0.05) {
                                ((1 - fraction) * 16).dp
                            } else 0.dp,
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
                                    bitmap = blurBackgroundImage.value!!,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .offset {
                                            backgroundShadowOffset.value
                                        }.graphicsLayer {
                                            scaleX = 1.050f
                                            scaleY = 1.050f
                                        }.blur(radius = 8.dp),
                                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                        orangeDark.copy(alpha = 0.3f)
                                    )
                                )
                                Image(
                                    bitmap = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.background(
                                        Color.Transparent,
                                        RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp),
                                    ).offset {
                                        backgroundImageOffset.value
                                    }.graphicsLayer {
                                        shadowElevation = 8f
                                        scaleX = 1.050f
                                        scaleY = 1.050f
                                    },
                                    alpha = 1 - fraction
                                )
                            }

                            // box and shadow
                            Box(
                                modifier = Modifier.aspectRatio(1f)
                                    .align(Alignment.Center)
                            ) {
                                SharedMaterialContainer(
                                    key = recipe.image,
                                    screenKey = "DetailsScreen",
                                    color = Color.Transparent,
                                    transitionSpec = FadeOutTransitionSpec
                                ) {
                                    Box {
                                        Box(
                                            modifier = Modifier
                                                .offset {
                                                    IntOffset(
                                                        x = (roll * 2).dp.roundToPx(),
                                                        y = -(pitch * 2).dp.roundToPx()
                                                    )
                                                }
                                        ) {

                                            Image(
                                                bitmap = imageBitmap,
                                                contentDescription = null,
                                                modifier = Modifier.aspectRatio(1f)
                                                    .align(Alignment.Center)
                                                    .padding(16.dp)
                                                    .shadow(
                                                        elevation = 16.dp,
                                                        shape = CircleShape,
                                                        clip = false,
                                                        ambientColor = Color.Red,
                                                        spotColor = Color.Red,
                                                    ),
                                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                                    orangeDark.copy(alpha = 0.0f)
                                                )
                                            )
                                        }

                                        Image(
                                            bitmap = imageBitmap,
                                            contentDescription = null,
                                            modifier = Modifier.aspectRatio(1f)
                                                .align(Alignment.Center)
                                                .padding(16.dp)
                                                .rotate(imageRotation.value.toFloat())
                                                .background(
                                                    Color.Transparent,
                                                    CircleShape,
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            StepsAndDetails(recipe, chefImage)
        }

        Box(
            modifier = Modifier.padding(10.dp).alpha(
                alpha = if (fraction <= 0) 1f else 0f,
            ).background(
                color = Color.Black,
                shape = RoundedCornerShape(50)
            )
                .shadow(elevation = 16.dp).padding(5.dp).clickable {
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

