package com.moneybudget.operations.operationsList

import androidx.lifecycle.viewModelScope
import com.moneybudget.database.Operation
import com.moneybudget.database.OperationMinimal
import com.moneybudget.repository.Repository
import com.moneybudget.repository.Repository.operationsDataSource
import com.moneybudget.utils.viewModels.ListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OperationsListViewModel : ListViewModel<Operation>(operationsDataSource) {

    private val _categoryIdFiltered = MutableStateFlow(0L)
    val categoryIdFiltered: StateFlow<Long> = _categoryIdFiltered

    private val _accountIdFiltered = MutableStateFlow(0L)
    val accountIdFiltered: StateFlow<Long> = _accountIdFiltered

    fun setCategoryIdFiltered(categoryId: Long) {
        if (categoryId == _categoryIdFiltered.value) {
            _categoryIdFiltered.value = 0L
        } else {
            _categoryIdFiltered.value = categoryId
        }
    }

    fun setAccountIdFiltered(accountId: Long) {
        if (accountId == _accountIdFiltered.value) {
            _accountIdFiltered.value = 0L
        } else {
            _accountIdFiltered.value = accountId
        }
    }

    override fun updateDeletedFlag(element : Any, flag : Boolean) {
        if(element is OperationMinimal){
            viewModelScope.launch {
                val operation = repositoryDataSource.get(element.operationId)
                if(operation != null){
                    operation.isDeleted = flag
                    repositoryDataSource.update(operation)
                }
            }
        }
    }

    val operationsList by lazy {
        flow {
            operationsDataSource
                .getAllMinimal()
                .combine(_accountIdFiltered) { list, accountId ->
                    if (accountId != 0L)
                        list.filter { operation ->
                            operation.accountId == accountId
                        }
                    else
                        list
                }
                .combine(_categoryIdFiltered) { list, categoryIdFiltered ->
                    if (categoryIdFiltered != 0L)
                        list.filter { operation ->
                            operation.categoryId == categoryIdFiltered
                        }
                    else
                        list
                }
                .collect {
                    emit(it.sortedByDescending { operation -> operation.operationDate })
                }
        }
    }

    fun getCategoriesList() = Repository.categoriesDataSource.getAllNames()

    fun getAccountsList() = Repository.accountsDataSource.getAllNames()
}