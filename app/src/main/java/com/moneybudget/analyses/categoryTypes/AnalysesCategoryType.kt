package com.moneybudget.analyses.categoryTypes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.moneybudget.analyses.AnalysesViewModel
import com.moneybudget.repository.Repository
import com.moneybudget.ui.composable.AmountOnTotalText
import com.moneybudget.ui.composable.LazyColumnList
import kotlin.math.abs


@Composable
fun CategoryTypesAnalyses(viewModel : AnalysesViewModel, onElementClick: (Long) -> Unit = {}) {
    val categoryTypesSum by viewModel.categoryTypesSum().collectAsState(initial = null)
    val totalBudget by viewModel.totalBudget(PreferenceManager
        .getDefaultSharedPreferences(LocalContext.current)
        .getString("howToCalculateBudget", "-1")?.toInt()?:-1)
        .collectAsState(initial = 0f)

    LazyColumnList {
        if(categoryTypesSum != null) {
            items(categoryTypesSum!!) { sumAndId ->
                CategoryTypesOperationListElement(
                    sumAndId.id,
                    sumAndId.sum,
                    totalBudget) { onElementClick(sumAndId.id) }
            }
        }
        else {
            item{
                CircularProgressIndicator()
            }
        }
    }
}



@Composable
fun CategoryTypesOperationListElement(
    id: Long,
    sum : Float,
    totalBudget: Float,
    onClick: () -> Unit
) {
    var categoryTypeName by remember { mutableStateOf("") }
    var total by remember { mutableStateOf(1f) }
    val sumAbs = abs(sum)

    if (id != 0L) {
        LaunchedEffect(key1 = id, block = {
            val categoryType = Repository.categoryTypesDataSource.get(id)

            if (categoryType != null) {
                categoryTypeName = categoryType.name
                total = totalBudget * categoryType.budgetPercent!! / 100
            }
        })
    }

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
            Text(text = categoryTypeName)
            AmountOnTotalText(sumAbs, total)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = sumAbs / (if (total > 0f) total else 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp),
                color = MaterialTheme.colors.primary
            )
        }
    }
}