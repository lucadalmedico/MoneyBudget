package com.moneybudget.operations.operation

import com.moneybudget.database.Operation
import com.moneybudget.repository.Repository
import com.moneybudget.utils.DateUtils
import com.moneybudget.utils.dateToLong
import com.moneybudget.utils.viewModels.ElementViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OperationViewModel : ElementViewModel<Operation>() {
    private val _operationDate = MutableStateFlow(DateUtils.getTodayDate().time)
    val operationDate: StateFlow<Long?> = _operationDate

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _categoryId = MutableStateFlow<Long?>(null)
    val categoryId: StateFlow<Long?> = _categoryId

    private val _accountId = MutableStateFlow<Long?>(null)
    val accountId: StateFlow<Long?> = _accountId

    private val _description = MutableStateFlow<String?>(null)
    val description: StateFlow<String?> = _description

    fun onOperationDateChange(operationDate: String) {
        _operationDate.value = operationDate.dateToLong()
    }

    fun onAmountChange(amount: String) {
        _amount.value = amount
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onCategoryIdChange(categoryId: Long) {
        _categoryId.value = categoryId
    }

    fun onAccountIdChange(accountId: Long) {
        _accountId.value = accountId
    }

    override val repositoryDataSource by lazy {
        Repository.operationsDataSource
    }

    override fun initialize(inputObject: Operation) {
        _operationDate.value = inputObject.operationDate
        _amount.value = inputObject.amount.toString()
        _categoryId.value = inputObject.categoryId
        _accountId.value = inputObject.accountId
        _description.value = inputObject.description
    }

    override fun get(): Operation = Operation(
        _operationDate.value,
        _amount.value.toFloat(),
        _categoryId.value,
        _accountId.value,
        _description.value ?: "",
        operationId = currentId.value ?: 0L
    )
}