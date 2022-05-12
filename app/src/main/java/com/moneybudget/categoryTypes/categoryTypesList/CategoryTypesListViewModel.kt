package com.moneybudget.categoryTypes.categoryTypesList

import androidx.lifecycle.viewModelScope
import com.moneybudget.database.CategoryType
import com.moneybudget.database.NameAndId
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ListViewModel
import kotlinx.coroutines.launch

class CategoryTypesListViewModel: ListViewModel<CategoryType>(Repository.categoryTypesDataSource) {
    val categoryTypeNames = Repository
        .categoryTypesDataSource
        .getAllNames()

    override fun updateDeletedFlag(element : Any, flag : Boolean) {
        if(element is NameAndId){
            viewModelScope.launch {
                val type = repositoryDataSource.get(element.id)
                if(type != null){
                    type.isDeleted = flag
                    repositoryDataSource.update(type)
                }
            }
        }
    }
}