package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.dao.CategoryDao
import com.example.notesapp.entities.Category

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {
    companion object {
        var categoryDatabase: CategoryDatabase? = null

        @Synchronized
        suspend fun getDatabase(context: Context): CategoryDatabase {
            if (categoryDatabase == null) {
                categoryDatabase = Room.databaseBuilder(
                    context
                    , CategoryDatabase::class.java
                    , "category.db"
                ).build()
                categoryDatabase!!.CategoryDao().run {
                    if (getCategoryByName("All Notes") == null) {
                        insertCategory(Category(name_category = "All Notes", order_category = 0))
                    }
                }
            }

            return categoryDatabase!!
        }


    }

    abstract fun CategoryDao(): CategoryDao

}