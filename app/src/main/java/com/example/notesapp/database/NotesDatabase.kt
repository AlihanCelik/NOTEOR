package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notesapp.Converters
import com.example.notesapp.dao.NoteDao
import com.example.notesapp.entities.Notes

@Database(entities = [Notes::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    companion object {
        private var notesDatabase: NotesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): NotesDatabase {
            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return notesDatabase!!
        }
    }

    abstract fun noteDao(): NoteDao
}