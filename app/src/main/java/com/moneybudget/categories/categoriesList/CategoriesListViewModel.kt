package com.moneybudget.categories.categoriesList

import androidx.lifecycle.viewModelScope
import com.moneybudget.database.Category
import com.moneybudget.database.NameAndId
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ListViewModel
import kotlinx.coroutines.launch

class CategoriesListViewModel: ListViewModel<Category>(Repository.categoriesDataSource) {
    val categories = Repository
        .categoriesDataSource
        .getAllNames()

    override fun updateDeletedFlag(element : Any, flag : Boolean) {
        if(element is NameAndId){
            viewModelScope.launch {
                val category = repositoryDataSource.get(element.id)
                if(category != null){
                    category.isDeleted = flag
                    repositoryDataSource.update(category)
                }
            }
        }
    }
}