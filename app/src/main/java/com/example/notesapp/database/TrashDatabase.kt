package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notesapp.Converters
import com.example.notesapp.dao.TrashDao
import com.example.notesapp.entities.Trash


@Database(entities = [Trash::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TrashDatabase : RoomDatabase() {

    companion object {
        var trashDatabase: TrashDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): TrashDatabase {
            if (trashDatabase == null) {
                trashDatabase = Room.databaseBuilder(
                    context
                    , TrashDatabase::class.java
                    , "trash.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return trashDatabase!!
        }

    }

    abstract fun trashDao(): TrashDao
}