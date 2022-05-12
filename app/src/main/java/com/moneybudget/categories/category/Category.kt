package com.moneybudget.categories.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.categoryTypes.CategoryTypeDropDownMenu
import com.moneybudget.ui.composable.ElementPage
import com.moneybudget.ui.composable.Name


@Composable
fun Category(categoryId: Long = 0L, onBackPressed : () -> Unit = {}) {

    val viewModel: CategoryViewModel = viewModel()

    ElementPage(
        elementId = categoryId,
        viewModel = viewModel,
        onBackPressed = onBackPressed
    ){
        CategoryDescription(viewModel = viewModel)
    }
}

@Composable
fun CategoryDescription(viewModel: CategoryViewModel) {
    val categoryName by viewModel.categoryName.collectAsState()
    val categoryTypeId by viewModel.categoryTypeId.collectAsState()

    Name(categoryName) { viewModel.onCategoryNameChange(it) }
    CategoryTypeDropDownMenu(categoryTypeId) { viewModel.onCategoryTypeIdChange(it) }
}
