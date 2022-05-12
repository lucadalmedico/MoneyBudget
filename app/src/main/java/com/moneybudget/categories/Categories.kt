package com.moneybudget.categories

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.moneybudget.R
import com.moneybudget.repository.Repository
import com.moneybudget.ui.composable.NamesDropDownMenu

@Composable
fun CategoriesDropDownMenu(categoryId: Long? = null, onChangeCategory: (Long) -> Unit) {
    val categories by Repository.categoriesDataSource.getAllNames().collectAsState(initial = null)

    var categoryNameSelected by remember { mutableStateOf("") }

    if (categoryId != null && categoryId != 0L) {
        LaunchedEffect(key1 = categoryId, block = {
            categoryNameSelected = Repository.categoriesDataSource
                .getName(categoryId)
        })
    }

    NamesDropDownMenu(
        categories,
        categoryNameSelected,
        stringResource(id = R.string.category),
        onChangeCategory
    )
}


@Composable
fun CategoryName(
    id: Long,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.body2
) {
    var categoryName by remember { mutableStateOf("") }

    if (id != 0L) {
        LaunchedEffect(key1 = id, block = {
            categoryName = Repository.categoriesDataSource
                .getName(id)
        })
    }

    Text(
        text = categoryName,
        style = textStyle
    )
}