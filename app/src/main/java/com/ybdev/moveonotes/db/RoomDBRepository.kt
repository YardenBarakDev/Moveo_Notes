package com.ybdev.moveonotes.db

import com.ybdev.moveonotes.mvvm.model.Note

class RoomDBRepository {

    suspend fun insertNote(note : Note) = NotesRoomDB.instance?.getNotesDao()?.insertNote(note)

    suspend fun deleteNote(note: Note) = NotesRoomDB.instance?.getNotesDao()?.deleteNote(note)

    fun getAllNotes(userID : String) = NotesRoomDB.instance?.getNotesDao()?.getAllNotes(userID)

    fun getNote(key : String) = NotesRoomDB.instance?.getNotesDao()?.getNote(key)
}