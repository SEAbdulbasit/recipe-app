import model.Recipe

/**
 * Created by abdulbasit on 23/06/2023.
 */
sealed interface Screens {
    object RecipesList : Screens
    data class RecipeDetails(
        val recipe: Recipe
    ) : Screens
}