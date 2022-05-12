package com.moneybudget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val DarkColorPalette = darkColors(
    primary = primary_dark,
    primaryVariant = primary_dark,
    secondary = secondary,
    secondaryVariant = secondaryDark,
    onPrimary = primaryText,
    onSecondary = secondaryText
)

private val LightColorPalette = lightColors(
    primary = primary,
    primaryVariant = primary_light,
    secondary = secondary,
    secondaryVariant = secondaryLight,
    onPrimary = primaryText,
    onSecondary = secondaryText,
)

@Composable
fun MoneyBudgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}