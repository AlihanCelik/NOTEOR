package com.example.notesapp.dao

import androidx.room.*
import com.example.notesapp.entities.Trash


@Dao
interface TrashDao {

    @Query("SELECT * FROM trash ORDER BY id DESC")
    suspend fun getAllTrash() : List<Trash>

    @Query("SELECT * FROM trash WHERE id =:id")
    suspend fun getSpecificTrash(id:Int) : Trash

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrash(trash:Trash)

    @Delete
    suspend fun deleteNote(trash:Trash)

    @Query("DELETE FROM trash WHERE id = :id")
    suspend fun deleteSpecificTrash(id: Int)

    @Update
    suspend fun updateTrash(trash:Trash)

    @Query("SELECT * FROM trash ORDER BY strftime('%d/%m/%Y %H:%M:%S', date_time_t) DESC")
    suspend fun getAllTrashSortedByDate(): List<Trash>
}