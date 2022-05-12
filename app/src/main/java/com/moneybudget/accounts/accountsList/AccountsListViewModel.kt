package com.moneybudget.accounts.accountsList

import androidx.lifecycle.viewModelScope
import com.moneybudget.database.Account
import com.moneybudget.database.NameAndId
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ListViewModel
import kotlinx.coroutines.launch

class AccountsListViewModel: ListViewModel<Account>(Repository.accountsDataSource) {
    val accounts = Repository
        .accountsDataSource
        .getAllNames()

    override fun updateDeletedFlag(element : Any, flag : Boolean){
        if(element is NameAndId){
            viewModelScope.launch {
                val account = repositoryDataSource.get(element.id)
                if(account != null){
                    account.isDeleted = flag
                    repositoryDataSource.update(account)
                }
            }
        }
    }
}