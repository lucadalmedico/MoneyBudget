package com.moneybudget.accounts.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.ui.composable.ElementPage
import com.moneybudget.ui.composable.Name

@Composable
fun Account(accountId: Long = 0L, onBackPressed : () -> Unit = {}) {
    val viewModel: AccountViewModel = viewModel()

    ElementPage(
        elementId = accountId,
        viewModel = viewModel,
        onBackPressed = onBackPressed
    ){
        AccountDescription(viewModel = viewModel)
    }
}

@Composable
fun AccountDescription(viewModel: AccountViewModel) {
    val accountName by viewModel.accountName.collectAsState()

    Name(accountName, isLast = true) { viewModel.onAccountNameChange(it) }
}