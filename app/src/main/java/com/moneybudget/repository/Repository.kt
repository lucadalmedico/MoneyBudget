package com.moneybudget.repository

import android.content.Context
import com.moneybudget.database.ApplicationDatabase

object Repository {
    private lateinit var applicationDatabase : ApplicationDatabase

    fun initialize(context: Context) {
        applicationDatabase = ApplicationDatabase.getInstance(context = context)
    }

    val operationsDataSource by lazy {
        OperationsRepositoryDataSource(applicationDatabase.operationsDao)
    }

    val movementsDataSource by lazy {
        MovementsRepositoryDataSource(applicationDatabase.movementsDao)
    }

    val categoriesDataSource by lazy {
        CategoriesRepositoryDataSource(applicationDatabase.categoriesDao)
    }

    val categoryTypesDataSource by lazy {
        CategoryTypesRepositoryDataSource(applicationDatabase.categoryTypesDao)
    }

    val accountsDataSource by lazy {
        AccountsRepositoryDataSource(applicationDatabase.accountsDao)
    }
}