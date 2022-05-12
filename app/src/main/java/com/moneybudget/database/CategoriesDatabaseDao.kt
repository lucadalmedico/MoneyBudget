package com.moneybudget.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoriesDatabaseDao : BaseApplicationDatabaseDao<Category> {
    @Query("SELECT * from category_table WHERE id = :key")
    abstract override suspend fun get(key: Long): Category?

    @Query("SELECT name, id FROM category_table WHERE isDeleted = 0 ORDER BY id")
    abstract fun getAllNames(): Flow<List<NameAndId>>

    @Query("SELECT name FROM category_table WHERE id = :key")
    abstract suspend fun getName(key: Long): String

    @Query(
        "SELECT SUM(amount) AS sum, categoryId AS id FROM operation_table " +
                "WHERE (:date = 0 OR operationDate >= :date) AND amount < 0 GROUP BY categoryId " +
                "ORDER BY SUM(amount)"
    )
    abstract fun getCategoriesOperation(date: Long? = 0L): Flow<List<SumAndId>>

    @Query(
        "SELECT SUM(amount) as sum FROM operation_table " +
                "WHERE (:date = 0 OR operationDate >= :date) AND amount < 0 " +
                "GROUP BY categoryId ORDER BY SUM(amount)"
    )
    abstract fun getCategoriesOperationsSum(date: Long? = 0L): Flow<List<Float>>

    @Query("SELECT id FROM category_table WHERE name = :name LIMIT 1")
    abstract fun getIdFromName(name : String) : Long?
}