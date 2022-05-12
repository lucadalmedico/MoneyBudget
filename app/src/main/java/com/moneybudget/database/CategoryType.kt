package com.moneybudget.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_type_table")
data class CategoryType(
    var name: String,
    var budgetPercent: Int? = 0,
    var isDeleted : Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)