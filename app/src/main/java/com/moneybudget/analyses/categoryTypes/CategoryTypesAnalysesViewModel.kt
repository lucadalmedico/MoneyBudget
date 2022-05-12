package com.moneybudget.analyses.categoryTypes

import com.moneybudget.analyses.AnalysesViewModel
import com.moneybudget.database.SumAndId
import com.moneybudget.repository.Repository
import com.moneybudget.utils.DateUtils
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


fun AnalysesViewModel.categoryTypesSum() =
        Repository
            .operationsDataSource
            .getCategoryAmounts()
            .map { list ->
                list.filter {
                    it.amount < 0 && it.categoryId != 0L
                }
            }
            .combine(startDate) { list, date ->
                val startAndEndDates = DateUtils.getFirstAndLastDayOfMonth(date)
                list.filter {
                    it.date >= startAndEndDates.first && it.date < startAndEndDates.second
                }
            }
            .map { list ->
                list.map {
                    SumAndId(
                        it.amount,
                        Repository.categoriesDataSource
                            .get(it.categoryId)?.categoryTypeId ?: 0L
                    )
                }
                .filter {
                    it.id != 0L
                }
                .groupBy { it.id }
                .map {
                    SumAndId(
                        it.value.sumOf { sumAndId -> sumAndId.sum.toDouble() }
                            .toFloat(),
                        it.key)
                }
            }


fun AnalysesViewModel.totalBudget(howToCalculateBudget : Int) =
    Repository
        .operationsDataSource
        .getAmountsAndDate()
        .map { list ->
            list.filter {
                it.amount > 0
            }
        }
        .combine(startDate) { list, dates ->
            val newDates = DateUtils.getFirstAndLastDayOfMonth(dates, howToCalculateBudget)

            list.filter {
                it.date >= newDates.first && it.date < newDates.second
            }
        }
        .map { list ->
            list.sumOf { it.amount.toDouble() }.toFloat()
        }