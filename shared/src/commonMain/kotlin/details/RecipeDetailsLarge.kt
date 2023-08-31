import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import details.StepsAndDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Recipe
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource
import sensor.Listener
import sensor.SensorData
import sensor.SensorManager
import kotlin.math.PI


@OptIn(ExperimentalResourceApi::class)
@Composable
fun RecipeDetailsLarge(
    recipe: Recipe,
    imageBitmap: ImageBitmap,
    chefImage: ImageBitmap?,
    goBack: () -> Unit,
    sensorManager: SensorManager,
) {
    val backgroundImage = remember { mutableStateOf<ImageBitmap?>(null) }
    val imageRotation = remember { mutableStateOf(0) }
    val sensorDataLive = remember { mutableStateOf(SensorData(0.0f, 0.0f)) }
    val roll by derivedStateOf { (sensorDataLive.value.roll * 20).coerceIn(-2f, 2f) }
    val pitch by derivedStateOf { (sensorDataLive.value.pitch * 20).coerceIn(-2f, 2f) }
    val (fraction, setFraction) = remember { mutableStateOf(1f) }

    val tweenDuration = 300

    sensorManager.registerListener(object : Listener {
        override fun onUpdate(sensorData: SensorData) {
            sensorDataLive.value = sensorData
        }
    })

    val animatedOffset = animateIntOffsetAsState(
        targetValue = IntOffset((roll * 6f).toInt(), -(pitch * 6f).toInt()),
        animationSpec = tween(tweenDuration)
    )
    val animatedOffset2 = animateIntOffsetAsState(
        targetValue = IntOffset(-roll.toInt(), pitch.toInt()),
        animationSpec = tween(tweenDuration)
    )

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            if (recipe.bgImageLargeName.isNotEmpty()) {
                backgroundImage.value =
                    resource(recipe.bgImageLargeName).readBytes().toImageBitmap()
            }
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
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

    Box(
        modifier = Modifier.fillMaxSize().background(if (recipe.bgColor == sugar) yellow else sugar)
    ) {
        val size = mutableStateOf<IntSize>(IntSize(0, 0))
        Row {
            Box(
                modifier = Modifier
                    .fillMaxSize().onGloballyPositioned {
                        size.value = it.size
                    }.background(
                        recipe.bgColor,
                        RoundedCornerShape(topEnd = 35.dp, bottomEnd = 35.dp)
                    ).clip(
                        RoundedCornerShape(topEnd = 35.dp, bottomEnd = 35.dp)
                    )
                    .weight(1f).pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                // on every relayout Compose will send synthetic Move event,
                                // so we skip it to avoid event spam
                                if (event.type == PointerEventType.Move) {
                                    val previousPosition = sensorDataLive.value
                                    val position = event.changes.first().position
                                    sensorDataLive.value =
                                        SensorData(
                                            roll = position.x - size.value.height / 2,
                                            pitch = position.y - size.value.width / 2
                                        )
                                }
                            }

                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    // background image + its shadow
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        backgroundImage.value?.let {
                            Image(
                                bitmap = blurFilter(it, getPlatformContext()),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .offset {
                                        animatedOffset.value
                                    },
                                colorFilter = ColorFilter.tint(
                                    orangeDark.copy(alpha = 0.3f)
                                )
                            )
                            Image(
                                bitmap = it,
                                contentDescription = null,
                                alignment = Alignment.Center,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .offset {
                                        animatedOffset2.value

                                    },
                            )
                        }

                        // image shadows and image
                        Box(
                            modifier = Modifier.aspectRatio(1f).padding(32.dp)
                                .align(Alignment.Center)
                        ) {
                            Box(modifier = Modifier.padding(32.dp)) {
                                Image(
                                    bitmap = imageBitmap,
                                    contentDescription = null,
                                    modifier = Modifier.aspectRatio(1f)
                                        .align(Alignment.Center)
                                        .padding(16.dp)
                                        .rotate(imageRotation.value.toFloat())
                                        .shadow(
                                            elevation = 16.dp,
                                            shape = CircleShape,
                                            clip = false,
                                            ambientColor = orangeDark.copy(alpha = 0.5f),
                                            spotColor = Color.Red,
                                        )
                                )
                            }
                        }
                    }
                }
                BackButton(goBack, fraction)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (recipe.bgColor == sugar) yellow else sugar)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                if (event.type == PointerEventType.Scroll) {
                                    val position = event.changes.first().position
                                    // on every relayout Compose will send synthetic Move event,
                                    // so we skip it to avoid event spam
                                    imageRotation.value =
                                        (imageRotation.value + position.getDistance()
                                            .toInt() * 0.010).toInt()
                                }
                            }
                        }
                    }
                    .weight(1f)
            ) {
                val listState = rememberLazyListState()

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(64.dp),
                        userScrollEnabled = true,
                        verticalArrangement = Arrangement.Absolute.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection),
                        state = listState
                    ) {
                        StepsAndDetails(recipe, chefImage)
                    }
                }
            }
        }
    }
}

@Composable
fun BackButton(goBack: () -> Unit, fraction: Float) {
    Box(
        modifier = Modifier.padding(start = 32.dp, top = 16.dp)
            .clip(
                RoundedCornerShape(50)
            )
            .clickable {
                goBack()
            }.background(
                color = Color.White,
                shape = RoundedCornerShape(50)
            ).padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.padding(start = 8.dp))
            Text(text = "Back to Recipes", style = MaterialTheme.typography.subtitle2)
        }
    }
}
