package com.moneybudget.database

data class NameAndId(var name: String, var id: Long)

data class SumAndId(var sum: Float, var id: Long)

data class AmountForCategory(var amount : Float, var date : Long, var categoryId : Long)

data class AmountsAndDate(var amount : Float, var date : Long)

