package com.recipeapp.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme
import com.recipeapp.tv.ui.theme.Pink40
import com.recipeapp.tv.ui.theme.Pink80
import com.recipeapp.tv.ui.theme.Purple40
import com.recipeapp.tv.ui.theme.Purple80
import com.recipeapp.tv.ui.theme.PurpleGrey40
import com.recipeapp.tv.ui.theme.PurpleGrey80
import com.recipeapp.tv.ui.theme.Typography

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RecipeAppKMPTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isInDarkTheme) {
        darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80
        )
    } else {
        lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}