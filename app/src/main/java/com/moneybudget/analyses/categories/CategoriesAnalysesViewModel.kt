package com.moneybudget.analyses.categories

import com.moneybudget.analyses.AnalysesViewModel
import com.moneybudget.database.SumAndId
import com.moneybudget.repository.Repository
import com.moneybudget.utils.DateUtils
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


fun AnalysesViewModel.categoriesAndSum() =
    Repository
        .operationsDataSource
        .getCategoryAmounts()
        .map { list ->
            list.filter {
                it.amount < 0
            }
        }
        .combine(startDate) { list, date ->
            val startAndEndDates = DateUtils.getFirstAndLastDayOfMonth(date)
            list.filter {
                it.date >= startAndEndDates.first &&  it.date < startAndEndDates.second
            }
        }
        .map {list ->
            list.filter {
                it.categoryId != 0L
            }
            .groupBy { it.categoryId }
            .map { SumAndId(
                it.value.sumOf {
                        operation -> operation.amount.toDouble()}.toFloat(),
                it.key) }
        }
