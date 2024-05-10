package com.example.groupproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private var emailInput: EditText? = null
    private var passwordInput: EditText? = null
    private var registrationButton: Button? = null
    private var progressBar: ProgressBar? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        mAuth = FirebaseAuth.getInstance()

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        registrationButton = findViewById(R.id.registrationButton)
        progressBar = findViewById(R.id.progressBar)
        registrationButton!!.setOnClickListener { registerProcess() }
    }
    private fun validEmail(email: String?) : Boolean {
        if (email.isNullOrEmpty()) {
            return false
        }

        // General Email Regex (RFC 5322 Official Standard)
        val emailRegex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'" +
                "*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x" +
                "5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z" +
                "0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4" +
                "][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z" +
                "0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
        return emailRegex.matches(email)
    }

    private fun validPassword(password: String?) : Boolean {
        if (password.isNullOrEmpty()) {
            return false
        }

        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")
        // The password must be 6+ characters and must contain at least 1 number and 1 letter
        if (password.matches(passwordRegex)) {
            return true
        }

        return false
    }
    private fun registerProcess() {
        progressBar!!.visibility = View.VISIBLE

        val email: String = emailInput!!.text.toString()
        val password: String = passwordInput!!.text.toString()

        if (!validEmail(email)) {
            Toast.makeText(applicationContext, "The specified email address is not valid!", Toast.LENGTH_LONG).show()
            progressBar!!.visibility = View.GONE
            return
        }
        if (!validPassword(password)) {
            Toast.makeText(applicationContext, "Password must be 6 or more characters and at least one letter and one number.", Toast.LENGTH_LONG).show()
            progressBar!!.visibility = View.GONE
            return
        }

        // Add account credentials to Firebase
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                    progressBar!!.visibility = View.GONE

                    val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Registration failed. Please try again.", Toast.LENGTH_LONG).show()
                    progressBar!!.visibility = View.GONE
                }
            }
    }
}