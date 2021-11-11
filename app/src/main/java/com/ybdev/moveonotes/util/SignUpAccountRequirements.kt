package com.ybdev.moveonotes.util

import java.util.regex.Pattern

class SignUpAccountRequirements {



    fun validateEmail(email: String): Boolean {
        val emailValidation = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

        val matcher = emailValidation.matcher(email)
        return matcher.find()
    }

    fun validatePassword(password : String) = password.length in 8..15
}