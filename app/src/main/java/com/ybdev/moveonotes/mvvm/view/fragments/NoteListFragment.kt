package com.ybdev.moveonotes.mvvm.view.fragments

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ybdev.moveonotes.adapters.NotesAdapter
import com.ybdev.moveonotes.R
import com.ybdev.moveonotes.databinding.FragmentNoteListBinding
import com.ybdev.moveonotes.mvvm.view_model.NoteListViewModel

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private lateinit var binding: FragmentNoteListBinding
    private lateinit var noteAdapter: NotesAdapter
    private val noteListViewModel = NoteListViewModel()
    private val user = Firebase.auth.currentUser
    private var showedMessage = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteListBinding.bind(view)

        setRecyclerView()
        navigateToNoteCreation()
        subscribeToObserver()

    }



    /**
     * listen to changes in the database and updates the list in case of changes
     */
    private fun subscribeToObserver() {
        user?.let {
            noteListViewModel.getAllNotes(it.uid)?.observe(viewLifecycleOwner, { articles ->
                noteAdapter.differ.submitList(articles)
                if (articles.isEmpty() && !showedMessage){
                    showDialog()
                    showedMessage = true
                }
            })
        }
    }

    /**
     * Shows a dialog if the note list is empty
     */
    private fun showDialog() {
        val message = "Maybe its a good time to write some notes"
        AlertDialog.Builder(requireContext())
            .setTitle("You don't have any notes")
            .setMessage(message)
            .setIcon(R.drawable.add_note)
            .setPositiveButton(
                "New note"
            ) { _: DialogInterface?, _: Int -> checkLocationPermissions() }
            .setNegativeButton("Later") { _: DialogInterface?, _: Int -> /*do nothing*/ }.show()
    }


    /**
     * initiate recycler view
     */
    private fun setRecyclerView() {
        noteAdapter = NotesAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }

    }

    /**
     * open new note page
     */
    private fun navigateToNoteCreation() {
        binding.floatingActionButton.setOnClickListener {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions(){
        locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private val locationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it)
            findNavController().navigate(R.id.noteFragment)
        else
            Snackbar.make(binding.root, "Please allow location permission first", Snackbar.LENGTH_SHORT).show()
    }

}