package com.moneybudget.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class OperationsDatabaseDao : BaseApplicationDatabaseDao<Operation> {
    @Query("SELECT * from operation_table WHERE operationId = :key")
    abstract override suspend fun get(key: Long): Operation?

    @Query(
        "SELECT operationDate, amount, accountId, categoryId, operationId"
                + " FROM operation_table WHERE (:categoryId = 0 OR categoryId = :categoryId) "
                + "AND (:accountId = 0 OR accountId = :accountId) AND isDeleted = 0 " +
                "ORDER BY operationDate DESC"
    )
    abstract fun getListOfOperationsFiltered(
        categoryId: Long = 0, accountId: Long = 0
    ): Flow<List<OperationMinimal>>

    @Query("SELECT operationDate, amount, accountId, categoryId, operationId " +
            "FROM operation_table WHERE isDeleted = 0")
    abstract fun getAllMinimal(): Flow<List<OperationMinimal>>


    @Query("SELECT amount, operationDate as date, category_table.id as categoryId " +
            "from operation_table" +
            " INNER JOIN category_table" +
            " ON operation_table.categoryId = category_table.id ")
    abstract fun getCategoryAmounts(): Flow<List<AmountForCategory>>

    @Query("SELECT amount, operationDate as date " +
            "from operation_table")
    abstract fun getAmountsAndDate(): Flow<List<AmountsAndDate>>
}