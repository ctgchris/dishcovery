package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var userEmail: EditText? = null
    private var userPassword: EditText? = null
    private var loginButton: Button? = null
    private var forgotPasswordButton: Button? = null
    private var progressBar: ProgressBar? = null
    private lateinit var sharedPreferences: SharedPreferences

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        userEmail = findViewById(R.id.emailInput)
        userPassword = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton)
        progressBar = findViewById(R.id.progressBar)

        loginButton!!.setOnClickListener { loginUserAccount() }
        forgotPasswordButton!!.setOnClickListener { forgotPassword() }


    }

    private fun loginUserAccount() {
        progressBar?.visibility = View.VISIBLE

        val email: String = userEmail?.text.toString()
        val password: String = userPassword?.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter an email address!", Toast.LENGTH_LONG).show()
            progressBar?.visibility = View.GONE
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter your password!", Toast.LENGTH_LONG).show()
            progressBar?.visibility = View.GONE
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar?.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()
                    sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                    goToFeed()
                } else {
                    Toast.makeText(applicationContext, "Login failed.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun forgotPassword() {
        val email: String = userEmail?.text.toString()
        userPassword!!.setText("")

        // Send reset password email
        mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Password reset email has been sent to $email.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "No account with the email $email exists.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToFeed() {
        val successfulLoginIntent = Intent(this@LoginActivity, FeedActivity::class.java)
        startActivity(successfulLoginIntent)
        finish() 
    }
}
