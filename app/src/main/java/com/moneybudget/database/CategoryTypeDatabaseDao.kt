package com.moneybudget.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryTypeDatabaseDao : BaseApplicationDatabaseDao<CategoryType> {
    @Query("SELECT * from category_type_table WHERE id = :key")
    abstract override suspend fun get(key: Long): CategoryType?

    @Query("SELECT name, id FROM category_type_table WHERE isDeleted = 0 ORDER BY id")
    abstract fun getAllNames(): Flow<List<NameAndId>>

    @Query("SELECT name FROM category_type_table WHERE id = :key")
    abstract suspend fun getName(key: Long): String

    @Query("SELECT " +
            "category_type_table.name, " +
            "category_type_table.isDeleted, " +
            "category_type_table.id, " +
            "category_type_table.budgetPercent FROM category_type_table" +
            " INNER JOIN category_table" +
            " ON category_type_table.id = category_table.categoryTypeId" +
            " WHERE category_table.id = :key")
    abstract suspend fun getFromCategory(key : Long) : CategoryType?

    @Query(
        "SELECT category_table.categoryTypeId AS id " +
                "FROM operation_table INNER JOIN category_table " +
                "ON operation_table.categoryId = category_table.id " +
                "INNER JOIN category_type_table " +
                "ON category_table.categoryTypeId = category_type_table.id " +
                "WHERE (:date = 0 OR operationDate >= :date) " +
                "AND amount < 0  " +
                "AND category_type_table.budgetPercent > 0 " +
                "GROUP BY category_table.categoryTypeId"
    )
    abstract fun getCategoryTypes(date: Long? = 0L): Flow<List<Long>>

    @Query(
        "SELECT SUM(operation_table.amount) AS sum " +
                "FROM operation_table INNER JOIN category_table " +
                "ON operation_table.categoryId = category_table.id " +
                "WHERE (:date = 0 OR operationDate >= :date) " +
                "AND amount < 0  " +
                "AND category_table.categoryTypeId = :categoryTypeId"
    )
    abstract suspend fun getCategoryTypeOperations(
        categoryTypeId: Long,
        date: Long? = 0L
    ): Float?

    @Query(
        "SELECT SUM(amount) AS sum " +
                "FROM operation_table " +
                "WHERE operationDate BETWEEN :fromDate AND :toDate " +
                "AND amount > 0  "
    )
    abstract fun getBudget(
        fromDate: Long? = 0L,
        toDate: Long? = 0L
    ): Flow<Float>

    @Query(
        "SELECT SUM(amount) as sum FROM operation_table " +
                "WHERE (:date = 0 OR operationDate >= :date) AND amount < 0 " +
                "GROUP BY categoryId ORDER BY SUM(amount)"
    )
    abstract fun getCategoryTypesOperationsSum(date: Long? = 0L): Flow<List<Float>>
}