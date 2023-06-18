package model

import androidx.compose.ui.graphics.Color


/**
 * Created by abdulbasit on 18/06/2023.
 */


val recipesList = listOf(
    Recipe(
        id = 1,
        title = "Lemon Cheesecake",
        description = "Tart Lemon Cheesecake sits atop an almond-graham cracker crust to add a delightful nuttiness to the traditional graham cracker crust. Finish the cheesecake with lemon curd for double the tart pucker!",
        ingredients = listOf(
            "110 g digestive biscuits ",
            "50 g butter",
            "25 g light brown soft sugar ",
            "350g mascarpone",
            "75 g caster sugar ",
            "1 lemon, zested",
            "2 - 3 lemons, juiced(about 90 ml)"
        ),
        instructions = listOf(
            " Crush the digestive biscuits in a food bag with a rolling pin or in the food processor . Melt the butter in a saucepan, take off heat and stir in the brown sugar and biscuit crumbs .",
            " Line the base of a 20 cm loose bottomed cake tin with baking parchment.Press the biscuit into the bottom of the tin and chill in the fridge while making the topping.",
            " Beat together the mascarpone, caster sugar, lemon zest and juice, until smooth and creamy . Spread over the base and chillfor a couple of hours .",
        ),
        image = "assets/images/desserts/01-lemon-cheesecake.png",
        bgImageName = "01-lemon-cheesecake-bg",
        bgColor = Color.Yellow,
    )
)