package com.example.groupproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class AddRecipeActivity : AppCompatActivity() {
    private lateinit var nameET : EditText
    private lateinit var descriptionET : TextInputEditText
    private lateinit var ingredientsET : TextInputEditText
    private lateinit var finishButton : Button
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)

        nameET = findViewById(R.id.nameInput)
        descriptionET = findViewById(R.id.descriptionInput)
        ingredientsET = findViewById(R.id.ingredientsInput)
        finishButton = findViewById(R.id.finishButton)
        finishButton.setOnClickListener {addRecipe()}
        mAuth = FirebaseAuth.getInstance()

    }

    private fun addRecipe() {
        val userEmail = mAuth.currentUser?.email

        val name : String = nameET.text.toString().replace(" ", "").lowercase()
        recipe = Recipe (name, nameET.text.toString(), descriptionET.text.toString(), ingredientsET.text.toString().split(", "), userEmail.toString())
        flag = true
        finish()
    }

    companion object {
        lateinit var recipe : Recipe
        var flag : Boolean = false
    }
}