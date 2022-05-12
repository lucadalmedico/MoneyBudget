package com.moneybudget.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovementsDatabaseDao : BaseApplicationDatabaseDao<Movement> {
    @Query("SELECT * from movement_table WHERE movementId = :key")
    abstract override suspend fun get(key: Long): Movement?

    @Query(
        "SELECT amount, sourceAccountId, destinationAccountId, movementId "
                + " FROM movement_table WHERE isDeleted = 0 ORDER BY movementDate DESC"
    )
    abstract fun getListOfMovements(): Flow<List<MovementMinimal>>
}