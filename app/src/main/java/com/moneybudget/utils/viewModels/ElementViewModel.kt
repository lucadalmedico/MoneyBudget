package com.moneybudget.utils.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneybudget.repository.RepositoryDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class ElementViewModel<T> : ViewModel() {
    protected abstract val repositoryDataSource : RepositoryDataSource<T>

    protected abstract fun initialize(inputObject: T)

    protected abstract fun get(): T

    protected val currentId = MutableStateFlow(0L)

    fun initializeFromId(id: Long?) {
        if (currentId.value == 0L
            && id != null
            && id != currentId.value
        ) {
            currentId.value = id
            CoroutineScope(Dispatchers.IO).launch {
                repositoryDataSource.get(id)?.let { initialize(it) }
            }
        }
    }

    open fun saveElement() {
        viewModelScope.launch {
            if (currentId.value == 0L) {
                currentId.value = repositoryDataSource.insert(get())
            } else {
                repositoryDataSource.update(get())
            }
        }
    }

    fun deleteElement() {
        if (currentId.value != 0L) {
            viewModelScope.launch {
                repositoryDataSource.delete(get())
            }
        }
    }

    fun enableDeleteButton(): Boolean = currentId.value != 0L
}