package com.moneybudget.accounts.account

import com.moneybudget.database.Account
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ElementViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccountViewModel : ElementViewModel<Account>() {

    override val repositoryDataSource by lazy {
        Repository.accountsDataSource
    }

    private val _accountName = MutableStateFlow("")
    val accountName: StateFlow<String> = _accountName

    fun onAccountNameChange(accountName: String) {
        _accountName.value = accountName
    }

    override fun initialize(inputObject: Account) {
        _accountName.value = inputObject.name
    }

    override fun get(): Account = Account(
        _accountName.value,
        id = currentId.value ?: 0L
    )
}