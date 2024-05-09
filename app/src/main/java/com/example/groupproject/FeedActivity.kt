package com.example.groupproject



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FeedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipeList: MutableList<Recipe>

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var addRecipeButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed)

        // Initialize Firebase components
        databaseReference = FirebaseDatabase.getInstance().reference.child("recipes")
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recipeList = mutableListOf()
        recipeAdapter = RecipeAdapter(recipeList)
        recyclerView.adapter = recipeAdapter

        // Add test data to Firebase Realtime Database
        addTestDataToDatabase()

        // Fetch recipes from Firebase Database
        fetchRecipes()

        addRecipeButton = findViewById(R.id.addRecipeButton)
        addRecipeButton.setOnClickListener { addRecipe() }
    }

    override fun onRestart() {
        if (AddRecipeActivity.flag) {
            databaseReference.child("recipe${AddRecipeActivity.recipe.id}").setValue(AddRecipeActivity.recipe)
            fetchRecipes()
            AddRecipeActivity.flag = false
        }
        super.onRestart()
    }


    private fun addTestDataToDatabase() {
        val recipe1 = Recipe("1", "Spaghetti Carbonara", "Classic Italian pasta dish", listOf("Spaghetti", "Eggs", "Bacon", "Parmesan cheese"))
        val recipe2 = Recipe("2", "Chicken Curry", "Indian-inspired chicken curry", listOf("Chicken", "Curry paste", "Coconut milk", "Vegetables"))

        databaseReference.child("recipe1").setValue(recipe1)
        databaseReference.child("recipe2").setValue(recipe2)
    }

    private fun fetchRecipes() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                for (recipeSnapshot in snapshot.children) {
                    val recipe = recipeSnapshot.getValue(Recipe::class.java)
                    recipe?.let {
                        recipeList.add(it)
                    }
                }
                recipeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun addRecipe() {
        var myIntent : Intent = Intent(this, AddRecipeActivity::class.java)
        startActivity(myIntent)
    }


}

