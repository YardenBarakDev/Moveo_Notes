package com.ybdev.moveonotes.mvvm.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.ybdev.moveonotes.databinding.FragmentMapBinding
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ybdev.moveonotes.R
import com.ybdev.moveonotes.mvvm.view_model.NoteListViewModel


class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val noteListViewModel = NoteListViewModel()
    private lateinit var map: GoogleMap
    private val user = Firebase.auth.currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapBinding.bind(view)

        setUpMap()
    }

    /**
     * initiate map
     */
    private fun setUpMap() {
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * once the map is ready attach marker listener and observe notes from db
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setMapListener()
        observeNotesAndAddMarkers()
    }

    /**
     * listen to when a marker is pressed
     */
    private fun setMapListener() {
        map.setOnMarkerClickListener { marker ->
            showNote(marker.tag.toString())
            true
        }
    }

    /**
     * open the selected note in a new page
     */
    private fun showNote(tag: String) {
        noteListViewModel.getNote(tag)?.observe(viewLifecycleOwner, { note ->
            Bundle().apply {
                putParcelable("note", note)
                findNavController().navigate(R.id.action_global_noteFragment, this)
                }
            })
    }

    /**
     * observes the notes and show markers according to their location
     */
    private fun observeNotesAndAddMarkers() {
        user?.uid?.let {
            noteListViewModel.getAllNotes(it)?.observe(viewLifecycleOwner, { list ->
                for (i in list.indices) {
                    val marker = map.addMarker(
                        MarkerOptions().position(LatLng(list[i].locationLat, list[i].locationLon))
                            .title(list[i].title)
                    )
                    marker?.tag = list[i].key
                }
            })
        }
    }
}

