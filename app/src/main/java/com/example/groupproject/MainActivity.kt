package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var adView : AdView
    private var registerButton: Button? = null
    private var loginButton: Button? = null
    private var logo: ImageView? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) // Initialize SharedPreferences

        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        registerButton = findViewById(R.id.registerButton)
        loginButton = findViewById(R.id.loginButton)
        logo = findViewById(R.id.logo)

        logo!!.setImageResource(R.drawable.newlogo)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            skipLogin()
        }
        registerButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        loginButton!!.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    private fun skipLogin() {
        val intent = Intent(this@MainActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }
}
