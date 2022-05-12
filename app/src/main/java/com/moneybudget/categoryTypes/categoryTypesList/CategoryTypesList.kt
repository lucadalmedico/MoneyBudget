package com.moneybudget.categoryTypes.categoryTypesList

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
fun CategoryTypesList(onElementClick: (Long) -> Unit) {
    val viewModel: CategoryTypesListViewModel = viewModel()

    val types by viewModel.categoryTypeNames.collectAsState(initial = null)

    ListComposable(
        list = types,
        addButtonOnClick = { onElementClick(0L) },
        onElementClick = { onElementClick((it as NameAndId).id) },
        elementHorizontalArrangement = Arrangement.Start,
        viewModel = viewModel,
    ){
        CategoryTypesListElement(it as NameAndId)
    }
}

@Composable
fun CategoryTypesListElement(categoryTypeNameAndId: NameAndId) {
    Text(
        text = categoryTypeNameAndId.name,
        style = MaterialTheme.typography.body2,
    )
}
