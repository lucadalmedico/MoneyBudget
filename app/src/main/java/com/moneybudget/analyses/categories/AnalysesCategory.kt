package com.moneybudget.analyses.categories

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moneybudget.analyses.AnalysesViewModel
import com.moneybudget.analyses.PieChart
import com.moneybudget.categories.CategoryName
import com.moneybudget.ui.composable.AmountText
import com.moneybudget.ui.composable.LazyColumnList

@Composable
fun CategoriesAnalyses(viewModel : AnalysesViewModel, onElementClick: (Long) -> Unit = {}) {
    val categoriesAndSum by viewModel
        .categoriesAndSum()
        .collectAsState(initial = null)

    LazyColumnList {
        if (categoriesAndSum != null) {
            item {
                PieChart(proportions = categoriesAndSum!!.map { it.sum }, viewModel.getColorList())
            }

            var index = 0
            items(categoriesAndSum!!) {
                CategoriesOperationListElement(it.sum, it.id, viewModel.getColor(index)) {
                    onElementClick(it.id)
                }
                index++
            }
        }
        else {
            item {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun CategoriesOperationListElement(
    sum: Float,
    id: Long,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() }),
        elevation = 2.dp,
    ) {
        Row(
            Modifier
                .padding(PaddingValues(4.dp, 16.dp, 4.dp, 16.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(Modifier.size(30.dp)) {
                    drawCircle(color = color)
                }
                Spacer(modifier = Modifier.size(5.dp))
                CategoryName(id)
            }
            AmountText(sum)
        }
    }
}