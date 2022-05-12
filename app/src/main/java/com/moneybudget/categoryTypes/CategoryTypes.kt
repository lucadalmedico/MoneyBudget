package com.moneybudget.categoryTypes

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.moneybudget.R
import com.moneybudget.repository.Repository
import com.moneybudget.ui.composable.NamesDropDownMenu

@Composable
fun CategoryTypeDropDownMenu(categoryTypeId: Long? = null, onChangeType: (Long) -> Unit) {
    val types by Repository.categoryTypesDataSource.getAllNames().collectAsState(initial = null)

    var typeNameSelected by remember { mutableStateOf("") }

    if (categoryTypeId != null && categoryTypeId != 0L) {
        LaunchedEffect(key1 = categoryTypeId, block = {
            typeNameSelected = Repository.categoryTypesDataSource
                .getName(categoryTypeId)
        })
    }

    NamesDropDownMenu(
        types,
        typeNameSelected,
        stringResource(id = R.string.categoryType),
        onChangeType
    )
}