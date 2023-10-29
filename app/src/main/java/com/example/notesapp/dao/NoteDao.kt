package com.example.notesapp.dao

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.room.*
import com.example.notesapp.AlarmReceiver
import com.example.notesapp.entities.Notes


@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAllNotes() : List<Notes>

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun getSpecificNote(id:Int) : Notes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes):Long

    @Delete
    suspend fun deleteNote(note:Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)

    @Transaction
    suspend fun deleteSpecificNoteWithReminder(context : Context, noteId: Int) {
        val reminderTime = getReminderForNote(noteId)
        if (reminderTime != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, noteId, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
        deleteSpecificNote(noteId)
    }

    @Query("SELECT reminder FROM notes WHERE id = :noteId")
    suspend fun getReminderForNote(noteId: Int): Long?

    @Update
    suspend fun updateNote(notes:Notes)

    @Query("SELECT * FROM notes ORDER BY date_time ASC")
    suspend fun getAllNotesSortedByDate(): List<Notes>

    @Query("SELECT * FROM notes ORDER BY create_date_time ASC")
    suspend fun getAllNotesCreatedSortedByDate(): List<Notes>

    @Query("SELECT * FROM notes WHERE reminder IS NOT NULL ORDER BY reminder ASC")
    suspend fun getAllNotesWithAllReminders(): List<Notes>

    @Query("SELECT * FROM notes WHERE reminder IS NOT NULL AND reminder > :currentTimestamp ORDER BY reminder ASC")
    suspend fun getAllNotesWithRemainingeReminders(currentTimestamp: Long): List<Notes>

    @Query("SELECT * FROM notes WHERE reminder IS NOT NULL AND reminder < :currentTimestamp ORDER BY reminder ASC")
    suspend fun getAllNotesWithDoneReminders(currentTimestamp: Long): List<Notes>

    @Query("SELECT * FROM notes WHERE note_category_id = :categoryId ORDER BY CASE WHEN :sortType = 'modifiedTime' THEN date_time ELSE create_date_time END ASC")
    suspend fun getNotesByCategoryIdSorted(categoryId: Int, sortType: String): List<Notes>


}