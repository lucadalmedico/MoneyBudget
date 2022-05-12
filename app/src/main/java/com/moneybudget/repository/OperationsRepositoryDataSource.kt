package com.moneybudget.repository

import com.moneybudget.database.Operation
import com.moneybudget.database.OperationMinimal
import com.moneybudget.database.OperationsDatabaseDao

open class OperationsRepositoryDataSource(private val operationsDatabaseDao: OperationsDatabaseDao)
    : RepositoryDataSource<Operation>(operationsDatabaseDao) {

    fun getAllMinimal() = operationsDatabaseDao.getAllMinimal()

    fun getCategoryAmounts() = operationsDatabaseDao.getCategoryAmounts()

    fun getAmountsAndDate() = operationsDatabaseDao.getAmountsAndDate()

    override fun getFromMinimal(element : Any) : Operation{
        return if(element is OperationMinimal) {
            Operation(
                element.operationDate,
                element.amount,
                element.categoryId,
                element.accountId,
                "",
                false,
                element.operationId
            )
        } else {
            super.getFromMinimal(element)
        }
    }
}