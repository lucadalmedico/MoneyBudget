package com.moneybudget.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_table")
data class Account(
    var name: String,
    var isDeleted : Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)