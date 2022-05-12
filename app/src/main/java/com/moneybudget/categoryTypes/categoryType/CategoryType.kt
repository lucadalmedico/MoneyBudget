package com.moneybudget.categoryTypes.categoryType

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.ui.composable.ElementPage
import com.moneybudget.ui.composable.Name
import com.moneybudget.ui.composable.Percent

@Composable
fun CategoryType(categoryTypeId: Long = 0L, onBackPressed : () -> Unit = {}) {

    val viewModel: CategoryTypeViewModel = viewModel()

    ElementPage(
        elementId = categoryTypeId,
        viewModel = viewModel,
        onBackPressed = onBackPressed
    ){
        CategoryTypeDescription(viewModel = viewModel)
    }
}

@Composable
fun CategoryTypeDescription(viewModel: CategoryTypeViewModel) {
    val categoryTypeName by viewModel.categoryTypeName.collectAsState()
    val budgetPercent by viewModel.budgetPercent.collectAsState()

    Name(categoryTypeName) { viewModel.onCategoryTypeNameChange(it) }
    Percent(budgetPercent, isLast = true) { viewModel.onBudgetPercentChange(it) }
}
