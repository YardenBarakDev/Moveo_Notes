package com.ybdev.moveonotes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ybdev.moveonotes.mvvm.model.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes WHERE userID == :userID ORDER BY timeStamp DESC")
    fun getAllNotes(userID : String) : LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE `key` == :key")
    fun getNote(key : String) : LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(vararg note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}