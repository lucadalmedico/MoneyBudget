package com.moneybudget.accounts.accountsList

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moneybudget.database.NameAndId
import com.moneybudget.repository.Repository
import com.moneybudget.ui.composable.AmountText
import com.moneybudget.ui.composable.ListComposable


@Composable
fun AccountsList(onElementClick: (Long) -> Unit) {
    val viewModel: AccountsListViewModel = viewModel()

    val accounts by viewModel.accounts.collectAsState(initial = null)

    ListComposable(
        list = accounts,
        addButtonOnClick = { onElementClick(0L) },
        onElementClick = { onElementClick((it as NameAndId).id) },
        viewModel = viewModel,
    ){
        AccountsListElement(it as NameAndId)
    }
}

@Composable
private fun AccountsListElement(account: NameAndId) {
    var accountAmount by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = account.id, block = {
        accountAmount = Repository
            .accountsDataSource
            .getAmount(account.id) ?: 0f
    })

    Text(
        text = account.name,
        style = MaterialTheme.typography.body2,
    )
    AmountText(amount = accountAmount)
}
