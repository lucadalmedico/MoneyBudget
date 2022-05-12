package com.moneybudget.repository

import com.moneybudget.database.BaseApplicationDatabaseDao

abstract class RepositoryDataSource<T>(private val databaseDao : BaseApplicationDatabaseDao<T>) {
    suspend fun insert(element : T) :Long = databaseDao.insert(element)

    suspend fun update(element : T) = databaseDao.update(element)

    suspend fun delete(element : T) = databaseDao.delete(element)

    suspend fun get(key: Long) = databaseDao.get(key)

    open fun getFromMinimal(element : Any) : T{
        return element as T
    }

    suspend fun deleteMinimal(element : Any) = delete(getFromMinimal(element))
}