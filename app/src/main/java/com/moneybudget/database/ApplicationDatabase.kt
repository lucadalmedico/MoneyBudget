package com.moneybudget.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Operation::class,
        Account::class,
        Category::class,
        CategoryType::class,
        Movement::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val operationsDao: OperationsDatabaseDao
    abstract val movementsDao: MovementsDatabaseDao
    abstract val categoriesDao: CategoriesDatabaseDao
    abstract val accountsDao: AccountsDatabaseDao
    abstract val categoryTypesDao: CategoryTypeDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationDatabase? = null

        fun getInstance(context: Context): ApplicationDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ApplicationDatabase::class.java,
                "money_budget_database"
            )
                .build()
    }


}