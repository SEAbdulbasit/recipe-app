import androidx.compose.animation.*
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import details.StepsAndDetails
import model.Recipe
import org.jetbrains.compose.resources.painterResource
import sensor.Listener
import sensor.SensorData
import sensor.SensorManager
import kotlin.math.PI


@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RecipeDetailsSmall(
    recipe: Recipe,
    goBack: () -> Unit,
    sensorManager: SensorManager?,
    animatedVisibilityScope: AnimatedContentScope,
    sharedTransactionScope: SharedTransitionScope
) {
    val imageRotation = remember { androidx.compose.animation.core.Animatable(0f) }
    val sensorDataLive = remember { mutableStateOf(SensorData(0.0f, 0.0f)) }
    val roll by derivedStateOf { (sensorDataLive.value.roll).coerceIn(-3f, 3f) }
    val pitch by derivedStateOf { (sensorDataLive.value.pitch).coerceIn(-2f, 2f) }

    val tweenDuration = 300

    androidx.compose.runtime.LaunchedEffect(sensorManager) {
        println("RecipeDetailsSmall: Registering sensor listener, sensorManager = $sensorManager")
        sensorManager?.registerListener(object : Listener {
            override fun onUpdate(sensorData: SensorData) {
                println("RecipeDetailsSmall: Sensor data received - roll: ${sensorData.roll}, pitch: ${sensorData.pitch}")
                sensorDataLive.value = sensorData
            }
        })
    }

    val backgroundShadowOffset = animateIntOffsetAsState(
        targetValue = IntOffset((roll * 12f).toInt(), (pitch * 12f).toInt()),
        animationSpec = tween(tweenDuration)
    )
    val backgroundImageOffset = animateIntOffsetAsState(
        targetValue = IntOffset((-roll * 8f).toInt(), (pitch * 8f).toInt()), animationSpec = tween(tweenDuration)
    )

    val toolbarOffsetHeightPx = remember { mutableStateOf(340f) }
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset, source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(0f, 340f)
                coroutineScope.launch {
                    imageRotation.snapTo(imageRotation.value + (available.y * 0.5f))
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset, available: Offset, source: NestedScrollSource
            ): Offset {
                val delta = available.y
                coroutineScope.launch {
                    imageRotation.snapTo(imageRotation.value + ((delta * PI / 180) * 10).toFloat())
                }
                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                coroutineScope.launch {
                    imageRotation.animateDecay(
                        initialVelocity = available.y * 0.3f,
                        animationSpec = androidx.compose.animation.core.exponentialDecay(
                            frictionMultiplier = 3f,
                            absVelocityThreshold = 0.5f
                        )
                    )
                }
                return super.onPreFling(available)
            }
        }
    }

    val candidateHeight = maxOf(toolbarOffsetHeightPx.value, 300f)
    val listState = rememberLazyListState()
    val (fraction, setFraction) = remember { mutableStateOf(1f) }

    with(sharedTransactionScope) {

        if (sharedTransactionScope.isTransitionActive.not()) {
            setFraction(0f)
        }

        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = if (recipe.bgColor == sugar) yellow else sugar)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().nestedScroll(nestedScrollConnection),
                state = listState
            ) {

                stickyHeader {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(candidateHeight.dp)
                            .sharedElement(
                                rememberSharedContentState(
                                    key = "item-container-${recipe.id}"
                                ),
                                animatedVisibilityScope,
                            )
                            .clip(RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp))
                            .background(
                                recipe.bgColor,
                                RoundedCornerShape(
                                    bottomEnd = 35.dp, bottomStart = 35.dp
                                ),
                            ),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            //bg image and shadow
                            recipe.bgImage?.let {
                                Image(painter = painterResource(it),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.offset {
                                        backgroundShadowOffset.value
                                    }.graphicsLayer {
                                        scaleX = 1.050f
                                        scaleY = 1.050f
                                    }.blur(radius = 8.dp),
                                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                        orangeDark.copy(alpha = 0.3f)
                                    )
                                )
                                Image(painter = painterResource(it),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.background(
                                        Color.Transparent,
                                        RoundedCornerShape(
                                            bottomEnd = 35.dp, bottomStart = 35.dp
                                        ),
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

                            Box(
                                modifier = Modifier.aspectRatio(1f).align(Alignment.Center)
                            ) {
                                Image(
                                    painter = painterResource(recipe.image),
                                    contentDescription = null,
                                    modifier = Modifier.aspectRatio(1f).align(Alignment.Center)
                                        .windowInsetsPadding(WindowInsets.systemBars)
                                        .padding(16.dp).rotate(imageRotation.value.toFloat())
                                        .background(
                                            Color.Transparent,
                                            CircleShape,
                                        ).sharedBounds(
                                            rememberSharedContentState(key = "item-image-${recipe.id}"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            enter = fadeIn(),
                                            exit = fadeOut()
                                        )
                                )
                            }
                        }
                    }
                }

                StepsAndDetails(
                    sharedTransactionScope = sharedTransactionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    recipe = recipe
                )
            }

            Box(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars).size(50.dp)
                .padding(10.dp).background(
                    color = Color.Black, shape = RoundedCornerShape(50)
                ).shadow(elevation = 16.dp).padding(5.dp).clickable {
                    goBack()
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = recipe.bgColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
