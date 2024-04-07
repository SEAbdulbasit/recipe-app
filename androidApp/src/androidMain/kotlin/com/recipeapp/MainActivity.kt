package com.recipeapp

import AnimateInEffect
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.myapplication.R
import details.IngredientItem
import details.InstructionItem
import getPlatformContext
import model.Recipe
import model.recipesList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import recipeslist.RecipeListItemImageWrapper
import sensor.SensorData
import sugar
import yellow
import kotlin.math.PI


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ListToDetailsDemo()
        }
    }
}

sealed class Screen {
    object List : Screen()
    data class Details(val item: Recipe) : Screen()
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalResourceApi::class)
@SuppressLint("PrimitiveInCollection")
@Composable
fun ListToDetailsDemo() {
    var state by remember {
        mutableStateOf<Screen>(Screen.List)
    }

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(state, label = "", contentKey = { it.javaClass }, transitionSpec = {
            if (initialState == Screen.List) {
                slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
            } else {
                slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
            }
        }) {
            when (it) {
                Screen.List -> {
                    LazyColumn {
                        recipesList.subList(0, 3).forEach { recipe ->
                            item {
                                Box(modifier = Modifier) {
                                    Box(modifier = Modifier.padding(
                                        top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp
                                    ).shadow(
                                        elevation = 16.dp,
                                        shape = RoundedCornerShape(35.dp),
                                        clip = true,
                                        ambientColor = Color(0xffCE5A01),
                                        spotColor = Color(0xffCE5A01)
                                    ).fillMaxWidth().aspectRatio(1.5f)
                                        .background(recipe.bgColor, RoundedCornerShape(35.dp))
                                        .fillMaxHeight().clickable {
                                            state = Screen.Details(recipe)
                                        }

                                    ) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier.fillMaxHeight().padding(16.dp)
                                                    .fillMaxWidth(0.55f),
                                                verticalAlignment = Alignment.Bottom
                                            ) {
                                                Column(modifier = Modifier.align(Alignment.Bottom)) {
                                                    Text(
                                                        recipe.title,
                                                        style = MaterialTheme.typography.h4,
                                                        modifier = Modifier.then(
                                                            Modifier.sharedElement(
                                                                rememberSharedContentState(
                                                                    key = "item-title${recipe.title}"
                                                                ),
                                                                this@AnimatedContent,
                                                            )
                                                        )
                                                    )
                                                    Text(
                                                        recipe.description,
                                                        style = MaterialTheme.typography.subtitle1,
                                                        maxLines = 3,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.padding(top = 8.dp)
                                                            .then(
                                                                Modifier.sharedElement(
                                                                    rememberSharedContentState(
                                                                        key = "item-description${recipe.title}"
                                                                    ),
                                                                    this@AnimatedContent,
                                                                )
                                                            )
                                                    )
                                                }
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }

                                    RecipeListItemImageWrapper(modifier = Modifier.align(Alignment.BottomEnd)
                                        .fillMaxWidth(0.45f).aspectRatio(1f), child = {
                                        Box(modifier = Modifier) {
                                            Box(
                                                modifier = Modifier.clip(RoundedCornerShape(50))
                                                    .background(
                                                        color = Color.Transparent,
                                                        shape = RoundedCornerShape(50)
                                                    )
                                            )
                                            Image(
                                                painter = painterResource(recipe.image),
                                                contentDescription = null,
                                                modifier = Modifier.background(
                                                    color = Color(0xffCE5A01).copy(alpha = 0.2f),
                                                    shape = CircleShape,
                                                ).aspectRatio(1f).then(
                                                    Modifier.sharedElement(
                                                        rememberSharedContentState(
                                                            key = "item-image${recipe.title}"
                                                        ),
                                                        this@AnimatedContent,
                                                    )
                                                )
                                            )
                                        }
                                    })
                                }
                            }

                        }
                    }
                }

                is Screen.Details -> {
                    val recipe = it.item

                    val imageRotation = remember { mutableStateOf(0) }
                    val sensorDataLive = remember { mutableStateOf(SensorData(0.0f, 0.0f)) }
                    val roll by derivedStateOf { (sensorDataLive.value.roll).coerceIn(-3f, 3f) }
                    val pitch by derivedStateOf { (sensorDataLive.value.pitch).coerceIn(-2f, 2f) }

                    val tweenDuration = 300

//                    sensorManager.registerListener(object : Listener {
//                        override fun onUpdate(sensorData: SensorData) {
//                            sensorDataLive.value = sensorData
//                        }
//                    })

                    val backgroundShadowOffset = animateIntOffsetAsState(
                        targetValue = IntOffset((roll * 6f).toInt(), (pitch * 6f).toInt()),
                        animationSpec = tween(tweenDuration)
                    )
                    val backgroundImageOffset = animateIntOffsetAsState(
                        targetValue = IntOffset(-roll.toInt(), pitch.toInt()),
                        animationSpec = tween(tweenDuration)
                    )

                    val context = getPlatformContext()

                    val toolbarOffsetHeightPx = remember { mutableStateOf(340f) }
                    val nestedScrollConnection = remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset, source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                val newOffset = toolbarOffsetHeightPx.value + delta
                                toolbarOffsetHeightPx.value = newOffset.coerceIn(0f, 340f)
                                imageRotation.value += (available.y * 0.5).toInt()
                                return Offset.Zero
                            }

                            override fun onPostScroll(
                                consumed: Offset, available: Offset, source: NestedScrollSource
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

                            item {
                                Box(
                                    modifier = Modifier.background(
                                        recipe.bgColor,
                                        RoundedCornerShape(
                                            bottomEnd = 35.dp, bottomStart = 35.dp
                                        ),
                                    ).height(360.dp).fillMaxWidth().clip(
                                        RoundedCornerShape(
                                            bottomEnd = 35.dp, bottomStart = 35.dp
                                        )
                                    ),
                                ) {

                                    Box(
                                        modifier = Modifier.aspectRatio(1f).align(Alignment.Center)
                                    ) {
                                        Box {
                                            Image(
                                                painter = painterResource(recipe.image),
                                                contentDescription = null,
                                                modifier = Modifier.aspectRatio(1f)
                                                    .align(Alignment.Center).padding(16.dp)
                                                    .rotate(imageRotation.value.toFloat())
                                                    .background(
                                                        Color.Transparent,
                                                        CircleShape,
                                                    ).then(
                                                        Modifier.sharedElement(
                                                            rememberSharedContentState(key = "item-image${recipe.title}"),
                                                            this@AnimatedContent,
                                                        )
                                                    ).fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }


                            item {
                                Text(
                                    text = recipe.title,
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.W700,
                                    modifier = Modifier.padding(
                                        top = 16.dp, start = 16.dp, end = 16.dp
                                    ).then(
                                        Modifier.sharedElement(
                                            rememberSharedContentState(key = "item-title${recipe.title}"),
                                            this@AnimatedContent,
                                        )
                                    )
                                )
                                Text(
                                    text = recipe.description,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(
                                        top = 16.dp, start = 16.dp, end = 16.dp
                                    ).then(
                                        Modifier.sharedElement(
                                            rememberSharedContentState(key = "item-description${recipe.title}"),
                                            this@AnimatedContent,
                                        )
                                    )
                                )

                                AnimateInEffect(recipe = recipe,
                                    intervalStart = 0 / (recipe.instructions.size + recipe.ingredients.size + 2).toFloat(),
                                    content = {
                                        Text(
                                            text = "INGREDIENTS",
                                            style = MaterialTheme.typography.h6,
                                            fontWeight = FontWeight.W700,
                                            modifier = Modifier.padding(
                                                top = 16.dp, start = 16.dp, end = 16.dp
                                            )
                                        )
                                    })
                            }

                            itemsIndexed(recipe.ingredients) { index, value ->
                                AnimateInEffect(intervalStart = (index + 1) / (recipe.instructions.size + recipe.ingredients.size + 1).toFloat(),
                                    recipe = recipe,
                                    content = {
                                        IngredientItem(recipe, value)
                                    })
                            }

                            item {
                                AnimateInEffect(recipe = recipe,
                                    intervalStart = (recipe.ingredients.size + 1) / (recipe.instructions.size + recipe.ingredients.size + 2).toFloat(),
                                    content = {
                                        Text(
                                            text = "STEPS",
                                            style = MaterialTheme.typography.h6,
                                            fontWeight = FontWeight.W700,
                                            modifier = Modifier.padding(
                                                top = 16.dp, start = 16.dp, end = 16.dp
                                            )
                                        )
                                    })
                            }

                            itemsIndexed(recipe.instructions) { index, item ->
                                AnimateInEffect(recipe = recipe,
                                    intervalStart = (recipe.ingredients.size + index + 1) / (recipe.instructions.size + recipe.ingredients.size + 1).toFloat(),
                                    content = {
                                        InstructionItem(recipe, index)
                                    })
                            }
                        }

                        Box(modifier = Modifier.padding(10.dp).background(
                            color = Color.Black, shape = RoundedCornerShape(50)
                        ).shadow(elevation = 16.dp).padding(5.dp).clickable {
                            state = Screen.List
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
        }
    }
}
