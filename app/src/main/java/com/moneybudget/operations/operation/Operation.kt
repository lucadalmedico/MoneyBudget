package com.moneybudget.operations.operation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.accounts.AccountsDropDownMenu
import com.moneybudget.categories.CategoriesDropDownMenu
import com.moneybudget.ui.composable.Amount
import com.moneybudget.ui.composable.Date
import com.moneybudget.ui.composable.Description
import com.moneybudget.ui.composable.ElementPage


@Composable
fun Operation(operationId: Long = 0, onBackPressed : () -> Unit = {}) {
    val viewModel: OperationViewModel = viewModel()

    ElementPage(
        elementId = operationId,
        viewModel = viewModel,
        onBackPressed = onBackPressed
    ){
        OperationDescription(viewModel = viewModel)
    }
}

@Composable
fun OperationDescription(viewModel: OperationViewModel){
    val operationDate by viewModel.operationDate.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val categoryId by viewModel.categoryId.collectAsState()
    val accountId by viewModel.accountId.collectAsState()
    val description by viewModel.description.collectAsState()

    Date(date = operationDate) { viewModel.onOperationDateChange(it) }
    Amount(amount = amount) { viewModel.onAmountChange(it) }
    CategoriesDropDownMenu(categoryId) { viewModel.onCategoryIdChange(it) }
    AccountsDropDownMenu(
        accountId = accountId,
        onChangeAccount = { viewModel.onAccountIdChange(it) })
    Description(description = description) { viewModel.onDescriptionChange(it) }
}