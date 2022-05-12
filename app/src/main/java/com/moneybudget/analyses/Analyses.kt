package com.moneybudget.analyses

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moneybudget.R
import com.moneybudget.analyses.categories.CategoriesAnalyses
import com.moneybudget.analyses.categoryTypes.CategoryTypesAnalyses
import com.moneybudget.ui.composable.Date
import com.moneybudget.ui.composable.TabsColumn
import com.moneybudget.utils.Screen

@Composable
fun AnalysesContent(showDate: Boolean = true, mainNavController : NavHostController) {
    val viewModel: AnalysesViewModel = viewModel()
    val startDate by viewModel.startDate.collectAsState()

    val navController = rememberNavController()

    val tabsContent = listOf(
        Pair(R.string.budget, Screen.CategoryTypes),
        Pair(R.string.categories, Screen.Categories),
    )

    TabsColumn(
        tabsContent = tabsContent,
        navController = navController
    ){
        if (showDate) {
            Date(date = startDate,
                dateLabel = stringResource(R.string.selectFrom),
                onDateChange = { viewModel.onStartDateChange(it) },
                isLast = true
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        NavHost(navController, startDestination = Screen.CategoryTypes.route) {
            composable(Screen.CategoryTypes.route) { CategoryTypesAnalyses(viewModel) {
                mainNavController.navigate(Screen.CategoryType.route + "/${it}")
            } }
            composable(Screen.Categories.route) { CategoriesAnalyses(viewModel) {
                mainNavController.navigate(Screen.Category.route + "/${it}")
            } }
        }
    }
}



@Composable
fun PieChart(
    proportions: List<Float>,
    colors: List<Color>
) {
    if (!proportions.isNullOrEmpty()) {
        val stroke = with(LocalDensity.current) { Stroke(50.dp.toPx()) }

        val total = proportions.sum()

        Canvas(
            Modifier
                .fillMaxWidth()
                .size(300.dp)
        ) {
            val innerRadius = (size.minDimension - stroke.width) / 2
            val halfSize = size / 2.0f
            val topLeft = Offset(
                halfSize.width - innerRadius,
                halfSize.height - innerRadius
            )
            val size = Size(innerRadius * 2, innerRadius * 2)
            var startAngle = 90f

            proportions.forEachIndexed { index, proportion ->
                val sweep = (proportion * 360 / total * (if (proportion < 0) -1 else 1))
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep - 1f,
                    topLeft = topLeft,
                    size = size,
                    useCenter = false,
                    style = stroke
                )
                startAngle += sweep
            }
        }
    }
}

