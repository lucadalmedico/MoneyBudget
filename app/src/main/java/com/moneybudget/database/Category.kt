package com.moneybudget.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_table",
    foreignKeys = [
        ForeignKey(
            entity = CategoryType::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryTypeId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_DEFAULT
        )],
    indices = [
        androidx.room.Index(value = ["categoryTypeId"])
    ]
)
data class Category(
    var name: String,
    var categoryTypeId: Long? = null,
    var isDeleted : Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)