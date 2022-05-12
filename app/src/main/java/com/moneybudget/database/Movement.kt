package com.moneybudget.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movement_table",
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("sourceAccountId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("destinationAccountId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )],
    indices = [
        androidx.room.Index(value = ["sourceAccountId"]),
        androidx.room.Index(value = ["destinationAccountId"])]
)
data class Movement(
    var movementDate: Long = 0L,
    var amount: Float = 0f,
    var sourceAccountId: Long? = null,
    var destinationAccountId: Long? = null,
    var description: String = "",
    var isDeleted : Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var movementId: Long = 0L
)


data class MovementMinimal(
    var amount: Float?,
    var sourceAccountId: Long,
    var destinationAccountId: Long,
    var movementId: Long
)