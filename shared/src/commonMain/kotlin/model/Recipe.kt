package model


/**
 * Created by abdulbasit on 18/06/2023.
 */

data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val image: String,
    val bgImageName: String,
    val bgColor: androidx.compose.ui.graphics.Color
)