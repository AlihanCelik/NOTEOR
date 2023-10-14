package com.example.notesapp.dao

import androidx.room.*
import com.example.notesapp.entities.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE name_category = :name_category")
    suspend fun getCategoryByName(name_category: String): Category?
    @Query("SELECT * FROM category ORDER BY id_category ASC")
    suspend fun getAllCategory() : List<Category>

    @Query("SELECT * FROM category WHERE id_category =:id_category")
    suspend fun getSpecificCategory(id_category:Int) : Category

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category:Category):Long

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)
}