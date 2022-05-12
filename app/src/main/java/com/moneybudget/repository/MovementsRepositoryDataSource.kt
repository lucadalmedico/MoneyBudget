package com.moneybudget.repository

import com.moneybudget.database.Movement
import com.moneybudget.database.MovementMinimal
import com.moneybudget.database.MovementsDatabaseDao

class MovementsRepositoryDataSource(private val movementsDatabaseDao: MovementsDatabaseDao)
    : RepositoryDataSource<Movement>(movementsDatabaseDao) {
        fun getAllMinimal() = movementsDatabaseDao.getListOfMovements()

    override fun getFromMinimal(element : Any) : Movement {
        return if(element is MovementMinimal) {
            Movement(
                0L,
                element.amount?:0f,
                element.sourceAccountId,
                element.destinationAccountId,
                "",
                false,
                element.movementId
            )
        } else {
            super.getFromMinimal(element)
        }
    }
}