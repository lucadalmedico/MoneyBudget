package com.moneybudget.accounts

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.moneybudget.R
import com.moneybudget.database.ApplicationDatabase
import com.moneybudget.repository.Repository
import com.moneybudget.ui.composable.NamesDropDownMenu


@Composable
fun AccountsDropDownMenu(
    accountId: Long? = null,
    onChangeAccount: (Long) -> Unit,
    label: String = stringResource(id = R.string.account)
) {
    val accounts by Repository.accountsDataSource.getAllNames().collectAsState(initial = null)

    var accountNameSelected by remember { mutableStateOf("") }

    if (accountId != null && accountId != 0L) {
        LaunchedEffect(key1 = accountId, block = {
            val account = Repository
                .accountsDataSource
                .get(accountId)
            if (account != null) {
                accountNameSelected = account.name
            }
        })
    }

    NamesDropDownMenu(
        accounts,
        accountNameSelected,
        label,
        onChangeAccount
    )
}

@Composable
fun AccountName(
    accountId: Long,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.caption
) {
    var accountName by remember { mutableStateOf("") }
    val currentContext = LocalContext.current

    if (accountId != 0L) {
        LaunchedEffect(key1 = accountId, block = {
            accountName = ApplicationDatabase.getInstance(currentContext)
                .accountsDao
                .getName(accountId)
        })
    }

    Text(
        text = accountName,
        style = textStyle
    )
}



