package com.moneybudget.categories.category

import com.moneybudget.database.Category
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ElementViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel : ElementViewModel<Category>() {

    override val repositoryDataSource by lazy {
        Repository.categoriesDataSource
    }
    
    private val _categoryName = MutableStateFlow("")
    val categoryName: StateFlow<String> = _categoryName

    private val _categoryTypeId = MutableStateFlow<Long?>(null)
    val categoryTypeId: StateFlow<Long?> = _categoryTypeId

    fun onCategoryNameChange(categoryName: String) {
        _categoryName.value = categoryName
    }

    fun onCategoryTypeIdChange(categoryTypeId: Long) {
        _categoryTypeId.value = categoryTypeId
    }

    override fun initialize(inputObject: Category) {
        _categoryName.value = inputObject.name
        _categoryTypeId.value = inputObject.categoryTypeId
    }

    override fun get(): Category = Category(
        categoryName.value,
        categoryTypeId.value,
        id =  currentId.value ?: 0L
    )
}