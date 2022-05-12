package com.moneybudget.utils.viewModels

import androidx.lifecycle.ViewModel
import com.moneybudget.database.NameAndId
import com.moneybudget.repository.RepositoryDataSource

abstract class ListViewModel<T>(protected val repositoryDataSource: RepositoryDataSource<T>) : ViewModel() {
    open suspend fun deleteElement(element : Any)
        = repositoryDataSource.deleteMinimal(element)

    abstract fun updateDeletedFlag(element: Any, flag : Boolean)
}

class Filters(val filtersList : List<NameAndId>,
              val onFilterClick : (Long) -> Unit,
              val filterSelected : Long = 0L)