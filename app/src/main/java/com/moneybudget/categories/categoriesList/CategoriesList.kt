package com.moneybudget.categories.categoriesList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.database.NameAndId
import com.moneybudget.ui.composable.ListComposable


@Composable
fun CategoriesList(onElementClick: (Long) -> Unit) {
    val viewModel: CategoriesListViewModel = viewModel()

    val categories by viewModel.categories.collectAsState(initial = null)

    ListComposable(
        list = categories,
        addButtonOnClick = { onElementClick(0L) },
        onElementClick = { onElementClick((it as NameAndId).id) },
        elementHorizontalArrangement = Arrangement.Start,
        viewModel = viewModel,
    ){
        CategoriesListElement(it as NameAndId)
    }
}
@Composable
fun CategoriesListElement(categoryNameAndId: NameAndId) {
    Text(
        text = categoryNameAndId.name,
        style = MaterialTheme.typography.body2,
    )
}
