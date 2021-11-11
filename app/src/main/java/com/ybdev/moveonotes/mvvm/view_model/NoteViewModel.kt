package com.ybdev.moveonotes.mvvm.view_model

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ybdev.moveonotes.databinding.FragmentNoteBinding
import com.ybdev.moveonotes.db.RoomDBRepository
import com.ybdev.moveonotes.mvvm.model.Note
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(private val fusedLocationClient: FusedLocationProviderClient) : ViewModel() {

    private val roomDBRepository : RoomDBRepository = RoomDBRepository()
    private var latLng : LatLng

    init {
        latLng = LatLng(0.0, 0.0)//initiate latLng in case of an error when fetching location
        getCurrentLocation()//check user last known location
    }

    /**
     * add/update note in room db
     */
    fun insertNote(note : Note) = viewModelScope.launch { roomDBRepository.insertNote(note) }

    /**
     * delete note from room db
     */
    fun deleteNote(note: Note) = viewModelScope.launch { roomDBRepository.deleteNote(note) }


    /**
     * get last known location and store it in latLng
     *
     * no need to check permissions since the permissions are checked before theis
     * method is called
     */
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                location?.let {
                    latLng = LatLng(it.latitude, it.longitude)
                }
            }
    }

    /**
     * create new Note instance with the user input and location
     */
    fun createNewNoteInstance(binding : FragmentNoteBinding, image : Bitmap?): Note {
        val user = Firebase.auth.currentUser
        return Note(
            UUID.randomUUID().toString(),
            user?.uid.toString(),
            Calendar.getInstance().timeInMillis,
            binding.titleTextBox.text.toString(),
            binding.bodyTextBox.text.toString(),
            latLng.longitude,
            latLng.latitude,
            image)
    }

    fun checkUserInput(title : String) : Boolean{
        if (title.trim().isEmpty())
            return false
        return true
    }
}