package com.ybdev.moveonotes.mvvm.view.fragments

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.ybdev.moveonotes.mvvm.view_model.NoteViewModel
import com.ybdev.moveonotes.R
import com.ybdev.moveonotes.databinding.FragmentNoteBinding
import com.ybdev.moveonotes.mvvm.model.Note
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import androidx.core.graphics.drawable.toBitmap
import kotlin.Exception


class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var binding : FragmentNoteBinding
    private lateinit var noteViewModel : NoteViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val args : NoteFragmentArgs by navArgs()
    private var newNote = false //check if we create a new note or editing an existing one
    private val CAMERA_REQUEST = 101

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteBinding.bind(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        noteViewModel = NoteViewModel(fusedLocationClient)

        showSelectedNote()
        subscribeToListeners()
    }

    /**
     * initiate on click listeners
     */
    private fun subscribeToListeners() {
        addNoteListener()
        deleteNoteListener()
        binding.image.setOnClickListener { showImageDialog() }
    }

    /**
     * open the devices camera
     * for some reason I couldn't open it with the 'registerForActivityResult'
     * so I had to use the deprecated method startActivityForResult
     * I will keep looking for a solution
     */
    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    /**
     * open devices gallery and allow the user to choose an image
     */
    private fun openGallery(){
        galleryContent.launch("image/*")
    }


    /**
     * allow the user to choose from which source to add an image
     */
    private fun showImageDialog() {
        val message = "Would you like to access gallery or camera"
        AlertDialog.Builder(requireContext())
            .setTitle("Add image")
            .setMessage(message)
            .setIcon(R.drawable.add_note)
            .setPositiveButton(
                "Camera"
            ) { _: DialogInterface?, _: Int -> checkCameraPermission()}
            .setNegativeButton("Gallery") { _: DialogInterface?, _: Int -> checkReadStoragePermission() } .show()
    }

    /**
     * delete the note from room db and return to the previous page
     */
    private fun deleteNoteListener() {
        binding.DeleteNoteButton.setOnClickListener {
            if (newNote){
                noteViewModel.deleteNote(args.note)
                Snackbar.make(binding.root, " Note deleted successfully",Snackbar.LENGTH_SHORT).show()
            }
            findNavController().navigate(R.id.action_noteFragment_to_noteListFragment)
        }
    }

    /**
     * add note the room db
     */
    private fun addNoteListener() {
        binding.addNoteButton.setOnClickListener { checkLocationPermissions() }
    }

    /**
     * update an existing note
     */
    private fun updateNote(): Note {
        args.note.title = binding.titleTextBox.text.toString()
        args.note.text = binding.bodyTextBox.text.toString()
        args.note.image = binding.image.drawable.toBitmap()
        return args.note
    }

    /**
     * if the user edits an existing note, the note data will update the view
     */
    private fun showSelectedNote() {
        try {
            val format: Format = SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.US)
            binding.currentDate.text = format.format(Date(args.note.timeStamp))
            binding.titleTextBox.setText(args.note.title)
            binding.bodyTextBox.setText(args.note.text)
            args.note.image?.let { binding.image.setImageBitmap(args.note.image)}
            newNote = true
        }catch (e : Exception){ }
    }

    /**
     * add note to room db, in case the user edited an existing note the old version will be override
     * also won't allow to save an empty note
     */
    private fun addNote() {
        if (noteViewModel.checkUserInput(binding.titleTextBox.text.toString()))
        {
            if (!newNote)
                noteViewModel.insertNote(noteViewModel.createNewNoteInstance(binding, binding.image.drawable.toBitmap()))
            else
                noteViewModel.insertNote(updateNote())
            Snackbar.make(binding.root, "Note saved successfully",Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_noteFragment_to_noteListFragment)
        }
        else
            Snackbar.make(binding.root, "Note must contain a title",Snackbar.LENGTH_SHORT).show()
    }


    /**
     * camera and gallery respond
     */
    private val galleryContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Glide.with(this).load(uri).into(binding.image)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST){
            try {
                if (data != null) {
                    data.extras?.get("data").let {
                        val bitmap = data.extras?.get("data") as Bitmap
                        Glide.with(this).load(bitmap).into(binding.image) }
                }

            }catch (e : Exception){}

        }
    }


    /**
     * Permissions area
     */
    private fun checkLocationPermissions(){
        locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun checkCameraPermission() {
            cameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun checkReadStoragePermission(){
            galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it)
            addNote()
        else
            Snackbar.make(binding.root, "Please allow location permission first", Snackbar.LENGTH_SHORT).show()
    }

    private val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it)
            openCamera()
        else
            Snackbar.make(binding.root, "Please allow camera access", Snackbar.LENGTH_SHORT).show()

    }

    private val galleryPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it)
            openGallery()
        else
            Snackbar.make(binding.root, "Please allow the app to access your files", Snackbar.LENGTH_SHORT).show()
    }

}