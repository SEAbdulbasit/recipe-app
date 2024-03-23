package model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi


/**
 * Created by abdulbasit on 18/06/2023.
 */

data class Recipe @OptIn(ExperimentalResourceApi::class) constructor(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val image: DrawableResource,
    val bgImage: DrawableResource? = null,
    val bgImageLarge: DrawableResource? = null,
    val bgColor: Color
)

