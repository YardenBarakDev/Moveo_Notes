package com.ybdev.moveonotes.mvvm.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ybdev.moveonotes.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        subscribeToListeners()
    }

    /**
     * initiate firebase auth
     */
    private fun initFirebase(){
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
    }

    /**
     * attach listeners
     */
    private fun subscribeToListeners() {
        binding.createNewAccount.setOnClickListener {
            showSignUpPAGE()
        }
        binding.loginButton.setOnClickListener {
            login()
        }
    }

    /**
     * validate credentials with firebase
     */
    private fun login() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty())
            showErrorMessage()
        else
            auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {//open note activity
                    showNotesApp()
                } else {
                    showErrorMessage()
                }
            }
    }

    /**
     * show an error message
     */
    private fun showErrorMessage() {
        Snackbar.make(binding.root,"Invalid email or password",Snackbar.LENGTH_SHORT).show()
    }


    /**
     * open SignUp activity and close current activity
     */
    private fun showSignUpPAGE() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * open note activity and close current activity
     */
    private fun showNotesApp(){
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * check if the user is connected
     */
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
           showNotesApp()
        }
    }
}