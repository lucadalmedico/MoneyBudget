package com.moneybudget.operations.movement

import com.moneybudget.database.Movement
import com.moneybudget.repository.Repository
import com.moneybudget.utils.DateUtils
import com.moneybudget.utils.dateToLong
import com.moneybudget.utils.viewModels.ElementViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovementViewModel: ElementViewModel<Movement>() {

    override val repositoryDataSource by lazy {
        Repository.movementsDataSource
    }

    private val _movementDate = MutableStateFlow(DateUtils.getTodayDate().time)
    val movementDate: StateFlow<Long?> = _movementDate

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _sourceAccountId = MutableStateFlow<Long?>(null)
    val sourceAccountId: StateFlow<Long?> = _sourceAccountId

    private val _destinationAccountId = MutableStateFlow<Long?>(null)
    val destinationAccountId: StateFlow<Long?> = _destinationAccountId

    private val _description = MutableStateFlow<String?>(null)
    val description: StateFlow<String?> = _description

    fun onMovementDateChange(movementDate: String) {
        _movementDate.value = movementDate.dateToLong()
    }

    fun onAmountChange(amount: String) {
        _amount.value = amount
    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onSourceAccountIdChange(accountId: Long) {
        _sourceAccountId.value = accountId
    }

    fun onDestinationAccountIdChange(accountId: Long) {
        _destinationAccountId.value = accountId
    }

    override fun initialize(inputObject: Movement) {
        _movementDate.value = inputObject.movementDate
        _amount.value = inputObject.amount.toString()
        _sourceAccountId.value = inputObject.sourceAccountId
        _destinationAccountId.value = inputObject.destinationAccountId
        _description.value = inputObject.description
    }

    override fun get(): Movement = Movement(
        _movementDate.value,
        _amount.value.toFloat(),
        sourceAccountId.value,
        _destinationAccountId.value,
        _description.value?:"",
        movementId = currentId.value ?: 0L
    )
}