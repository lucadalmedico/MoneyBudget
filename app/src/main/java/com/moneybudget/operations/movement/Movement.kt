package com.moneybudget.operations.movement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.R
import com.moneybudget.accounts.AccountsDropDownMenu
import com.moneybudget.ui.composable.Amount
import com.moneybudget.ui.composable.Date
import com.moneybudget.ui.composable.Description
import com.moneybudget.ui.composable.ElementPage


@Composable
fun Movement(movementId: Long = 0, onBackPressed : () -> Unit = {}) {
    val viewModel: MovementViewModel = viewModel()

    ElementPage(
        elementId = movementId,
        viewModel = viewModel,
        onBackPressed = onBackPressed
    ){
        MovementDescription(viewModel = viewModel)
    }
}

@Composable
fun MovementDescription(viewModel: MovementViewModel) {
    val movementDate by viewModel.movementDate.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val sourceAccountId by viewModel.sourceAccountId.collectAsState()
    val destinationAccountId by viewModel.destinationAccountId.collectAsState()
    val description by viewModel.description.collectAsState()

    Date(movementDate) { viewModel.onMovementDateChange(it) }
    Amount(amount = amount) { viewModel.onAmountChange(it) }
    AccountsDropDownMenu(
        accountId = sourceAccountId,
        onChangeAccount = { viewModel.onSourceAccountIdChange(it) },
        stringResource(id = R.string.sourceAccount)
    )
    AccountsDropDownMenu(
        accountId = destinationAccountId,
        onChangeAccount = { viewModel.onDestinationAccountIdChange(it) },
        stringResource(id = R.string.destinationAccount)
    )
    Description(description = description) { viewModel.onDescriptionChange(it) }
}