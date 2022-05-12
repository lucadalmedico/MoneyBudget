package com.moneybudget.utils

import android.icu.text.DateFormat
import java.util.*

fun Long.formatDate(): String = DateUtils.formatDate(this)

fun String.dateToLong(): Long = DateUtils.dateToLong(this)

object DateUtils{
    private val dateFormat by lazy {
        DateFormat.getDateInstance(DateFormat.MEDIUM)
    }

    fun dateToLong(date : String) : Long = dateFormat.parse(date).time

    fun formatDate(date : Long) : String = dateFormat.format(date)

    fun getTodayDate(): Date = dateFormat.calendar.time


    fun getFirstAndLastDayOfMonth(startDate: Long, amount : Int = 0): Pair<Long, Long> {
        val cal = dateFormat.calendar
        cal.timeInMillis = startDate

        cal.add(Calendar.MONTH, amount)

        val field = Calendar.DATE
        cal.set(field, cal.getActualMinimum(field))
        val firstDay = cal.timeInMillis
        cal.set(field, cal.getActualMaximum(field))

        return Pair(firstDay, cal.timeInMillis)
    }
}