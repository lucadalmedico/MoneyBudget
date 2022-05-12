package com.moneybudget.repository

import com.moneybudget.database.CategoryType
import com.moneybudget.database.CategoryTypeDatabaseDao
import com.moneybudget.database.NameAndId

class CategoryTypesRepositoryDataSource(private val categoryTypeDatabaseDao: CategoryTypeDatabaseDao)
    : RepositoryDataSource<CategoryType>(categoryTypeDatabaseDao) {

    fun getAllNames() = categoryTypeDatabaseDao.getAllNames()

    suspend fun getName(key: Long) = categoryTypeDatabaseDao.getName(key)

    override fun getFromMinimal(element : Any) : CategoryType {
        return if(element is NameAndId) {
            CategoryType(
                element.name,
                0,
                false,
                element.id
            )
        } else {
            super.getFromMinimal(element)
        }
    }
}
