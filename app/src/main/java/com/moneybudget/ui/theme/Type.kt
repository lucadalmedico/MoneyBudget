package com.moneybudget.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.moneybudget.R

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = FontFamily(
        Font(R.font.lato_regular),
        Font(R.font.lato_bold, weight = FontWeight.Bold),
        Font(R.font.lato_black, weight = FontWeight.Black),
        Font(R.font.lato_thin, weight = FontWeight.Light),
        Font(R.font.lato_thin, weight = FontWeight.Thin),
        Font(R.font.lato_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(R.font.lato_blackitalic, weight = FontWeight.Black, style = FontStyle.Italic),
        Font(R.font.lato_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
        Font(R.font.lato_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic),
    )
)