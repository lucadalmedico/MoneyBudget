package com.moneybudget.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseApplicationDatabaseDao<T> {
    @Insert
    suspend fun insert(obj: T): Long

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

    suspend fun get(key: Long): T?

}