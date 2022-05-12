package com.moneybudget.categoryTypes.categoryType

import com.moneybudget.database.CategoryType
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ElementViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryTypeViewModel : ElementViewModel<CategoryType>() {
    override val repositoryDataSource by lazy {
        Repository.categoryTypesDataSource
    }

    private val _categoryTypeName = MutableStateFlow("")
    val categoryTypeName: StateFlow<String> = _categoryTypeName

    private val _budgetPercent = MutableStateFlow("")
    val budgetPercent: StateFlow<String> = _budgetPercent

    fun onCategoryTypeNameChange(categoryTypeName: String) {
        _categoryTypeName.value = categoryTypeName
    }

    fun onBudgetPercentChange(budgetPercent: String) {
        _budgetPercent.value = budgetPercent
    }

    override fun initialize(inputObject: CategoryType) {
        _categoryTypeName.value = inputObject.name
        _budgetPercent.value = inputObject.budgetPercent.toString()
    }

    override fun get(): CategoryType {
        val budgetPercent = when {
            _budgetPercent.value != "" -> _budgetPercent.value.toInt()
            else -> 0
        }

        return CategoryType(
            _categoryTypeName.value,
            budgetPercent,
            id = currentId.value ?: 0L
        )
    }
}