package com.moneybudget.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AccountsDatabaseDao : BaseApplicationDatabaseDao<Account> {
    @Query("SELECT * from account_table WHERE id = :key")
    abstract override suspend fun get(key: Long): Account?

    @Query("SELECT name, id FROM account_table WHERE isDeleted = 0 ORDER BY name")
    abstract fun getAllNames(): Flow<List<NameAndId>>

    @Query("SELECT name FROM account_table WHERE id = :key AND isDeleted = 0")
    abstract suspend fun getName(key: Long): String

    @Query(
        "SELECT SUM(amount) FROM operation_table WHERE accountId = :key " +
                "AND (:date = 0 OR operationDate >= :date)"
    )
    abstract suspend fun getOperationSum(key: Long, date: Long? = 0L): Float?

    @Query(
        "SELECT SUM(amount) FROM movement_table WHERE sourceAccountId = :key " +
                "AND (:date = 0 OR movementDate >= :date)"
    )
    abstract suspend fun getSourceMovementSum(key: Long, date: Long? = 0L): Float?

    @Query(
        "SELECT SUM(amount) FROM movement_table WHERE destinationAccountId = :key " +
                "AND (:date = 0 OR movementDate >= :date)"
    )
    abstract suspend fun getDestinationMovementSum(key: Long, date: Long? = 0L): Float?

    @Query(
        "SELECT operationDate FROM operation_table " +
                "WHERE accountId = :key " +
                "ORDER by operationDate " +
                "LIMIT 1"
    )
    abstract suspend fun getFirstOperationDate(key: Long): Long?

    @Query(
        "SELECT movementDate FROM movement_table" +
                " WHERE sourceAccountId = :key OR destinationAccountId = :key " +
                "ORDER by movementDate " +
                "LIMIT 1"
    )
    abstract suspend fun getMovementDate(key: Long): Long?

    @Query("SELECT id FROM account_table WHERE name = :name LIMIT 1")
    abstract fun getIdFromName(name : String) : Long?
}