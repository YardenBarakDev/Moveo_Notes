package com.ybdev.moveonotes.mvvm.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.ybdev.moveonotes.db.RoomDBRepository
import com.ybdev.moveonotes.util.Constants

class NoteListViewModel : ViewModel() {

    private val roomDBRepository: RoomDBRepository = RoomDBRepository()


    /**
     * fetch all notes from room db
     */
    fun getAllNotes(userID: String) = roomDBRepository.getAllNotes(userID)

    /**
     * fetch one note (search for key)
     */
    fun getNote(key: String) = roomDBRepository.getNote(key)

}