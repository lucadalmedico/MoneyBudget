package com.moneybudget.operations.movementsList

import androidx.lifecycle.viewModelScope
import com.moneybudget.database.Movement
import com.moneybudget.database.MovementMinimal
import com.moneybudget.repository.Repository
import com.moneybudget.utils.viewModels.ListViewModel
import kotlinx.coroutines.launch

class MovementsListViewModel : ListViewModel<Movement>(Repository.movementsDataSource) {
    val movementsList = Repository
        .movementsDataSource
        .getAllMinimal()

    override fun updateDeletedFlag(element : Any, flag : Boolean) {
        if(element is MovementMinimal){
            viewModelScope.launch {
                val movement = repositoryDataSource.get(element.movementId)
                if(movement != null){
                    movement.isDeleted = flag
                    repositoryDataSource.update(movement)
                }
            }
        }
    }
}