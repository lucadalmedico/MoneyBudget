package com.moneybudget.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "operation_table",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("accountId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )],
    indices = [
        androidx.room.Index(value = ["categoryId"]),
        androidx.room.Index(value = ["accountId"])
    ]
)
data class Operation(
    var operationDate: Long = 0L,
    var amount: Float? = 0f,
    var accountId: Long? = null,
    var categoryId: Long? = null,

    var description: String = "",
    var isDeleted : Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var operationId: Long = 0L,
)

data class OperationMinimal(
    var operationDate: Long = 0L,
    var amount: Float? = 0f,
    var accountId: Long? = null,
    var categoryId: Long? = null,

    var operationId: Long
)