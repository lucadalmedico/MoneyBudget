package com.moneybudget.repository

import com.moneybudget.database.CategoriesDatabaseDao
import com.moneybudget.database.Category
import com.moneybudget.database.NameAndId

class CategoriesRepositoryDataSource(private val categoryDatabaseDao: CategoriesDatabaseDao)
    : RepositoryDataSource<Category>(categoryDatabaseDao) {

    fun getAllNames() = categoryDatabaseDao.getAllNames()

    suspend fun getName(key: Long) = categoryDatabaseDao.getName(key)

    override fun getFromMinimal(element : Any) : Category {
        return if(element is NameAndId) {
            Category(
                element.name,
                0,
                false,
                element.id
            )
        } else {
            super.getFromMinimal(element)
        }
    }

    fun getIdFromName(name : String) : Long?
        = categoryDatabaseDao.getIdFromName(name)
}