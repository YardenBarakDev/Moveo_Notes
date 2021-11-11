package com.ybdev.moveonotes.mvvm.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ybdev.moveonotes.databinding.ActivitySignUpBinding
import com.ybdev.moveonotes.util.SignUpAccountRequirements

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        subscribeToListeners()
    }

    private fun subscribeToListeners() {
        binding.signUpButton.setOnClickListener {
            checkUserInput()
        }
        binding.returnButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun checkUserInput() {

        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val signupRequirements= SignUpAccountRequirements()

        if(!signupRequirements.validateEmail(email)){
            binding.emailLayout.error = "Please provide a valid email address"
        }
        else if (!signupRequirements.validatePassword(password)){
            binding.emailLayout.error = ""
            binding.passwordLayout.error = "Password must contain at least 8 characters"
        }else{
            binding.emailLayout.error = ""
            binding.passwordLayout.error = ""
            createNewAccount(email, password)
        }
    }

    private fun createNewAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {// starts note activity
                   val intent = Intent(this, NotesActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "Unable to create account at the moment", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

}