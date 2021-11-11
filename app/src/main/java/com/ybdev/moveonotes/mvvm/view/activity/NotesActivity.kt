package com.ybdev.moveonotes.mvvm.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.ybdev.moveonotes.R
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ybdev.moveonotes.databinding.ActivityNotesBinding

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigation()
        handleViewVisibility()
        subscribeToListeners()
    }

    /**
     * check when the user clicks on the sign out button
     */
    private fun subscribeToListeners() {
        binding.topAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.logout)
                signOut()
            true
        }
    }

    /**
     * sign out from the firebase account and return to the login activity
     */
    private fun signOut() {
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * only shows the top app bar and the bottom navigation bar
     * in the note list fragment and the map fragment
     */
    private fun handleViewVisibility() {
        findNavController(R.id.navHostFragment)
            .addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id != R.id.noteFragment) {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.topBarLayout.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigationView.visibility = View.INVISIBLE
                    binding.topBarLayout.visibility = View.INVISIBLE
                }
            }
    }

    /**
     * handle the connection between the navigation graph to the bottom navigation view
     */
    private fun setBottomNavigation() {
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.navHostFragment))

        //prevent re loading of the same fragment
        binding.bottomNavigationView.setOnItemReselectedListener { /*no op*/ }
    }
}