package com.example.groupproject

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class RecipeActivity : AppCompatActivity() {
    private lateinit var uploadImageButton : Button
    private lateinit var recipeImage : ImageView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_activity)

        val recipeName : TextView = findViewById(R.id.recipeName)
        val recipeDescription : TextView = findViewById(R.id.recipeDescription)
        val recipeIngredients : TextView = findViewById(R.id.recipeIngredients)
        val backButton : Button = findViewById(R.id.recipeBackButton)
        backButton.setOnClickListener { finish() }
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val bundle : Bundle? = intent.extras
        recipeName.text = bundle!!.getString("name")
        recipeDescription.text = bundle.getString("description")
        recipeIngredients.text = "Ingredients: " + bundle.getString("ingredients")

        uploadImageButton = findViewById(R.id.recipeUploadButton)
        recipeImage = findViewById(R.id.recipeImage)

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                recipeImage.setImageURI(it)
            })

        uploadImageButton.setOnClickListener {
            galleryImage.launch("image/*")
        }
    }



}