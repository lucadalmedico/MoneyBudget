package com.moneybudget.repository

import com.moneybudget.database.Account
import com.moneybudget.database.AccountsDatabaseDao
import com.moneybudget.database.NameAndId

class AccountsRepositoryDataSource(private val accountsDatabaseDao: AccountsDatabaseDao)
    : RepositoryDataSource<Account>(accountsDatabaseDao) {

    fun getAllNames() = accountsDatabaseDao.getAllNames()

    suspend fun getName(key: Long) = accountsDatabaseDao.getName(key)

    suspend fun getAmount(key: Long, date: Long? = 0L) : Float? {
        return accountsDatabaseDao.getOperationSum(key, date)
            ?.minus(accountsDatabaseDao.getSourceMovementSum(key, date) ?: 0f)
            ?.plus(accountsDatabaseDao.getDestinationMovementSum(key, date) ?: 0f)
    }

    override fun getFromMinimal(element : Any) : Account {
        return if(element is NameAndId) {
            Account(
                element.name,
                false,
                element.id
            )
        } else {
            super.getFromMinimal(element)
        }
    }

    fun getIdFromName(name : String) : Long?
            = accountsDatabaseDao.getIdFromName(name)
}