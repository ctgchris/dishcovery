package com.example.groupproject



import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var addRecipeButton : Button
    private lateinit var logoutButton: Button
    private lateinit var notesButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed)
        databaseReference = FirebaseDatabase.getInstance().reference.child("recipes")
        firebaseAuth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.recipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recipeList = mutableListOf()
        recipeAdapter = RecipeAdapter(recipeList)
        recyclerView.adapter = recipeAdapter

        addTestDataToDatabase()
        fetchRecipes()
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        addRecipeButton = findViewById(R.id.addRecipeButton)
        addRecipeButton.setOnClickListener { addRecipe() }
        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener { logoutUser() }
        notesButton = findViewById(R.id.notesButton)
        notesButton.setOnClickListener { runNotes() }


    }

    override fun onRestart() {
        if (AddRecipeActivity.flag) {
            databaseReference.child("recipe${AddRecipeActivity.recipe.id}").setValue(AddRecipeActivity.recipe)
            AddRecipeActivity.flag = false
        }
        fetchRecipes()
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
                var adapter = RecipeAdapter(recipeList)
                recyclerView.adapter = adapter
                adapter.onItemClickListener(object : RecipeAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@FeedActivity, RecipeActivity::class.java)
                        intent.putExtra("id", recipeList[position].id)
                        intent.putExtra("name", recipeList[position].title)
                        intent.putExtra("description", recipeList[position].description)
                        intent.putExtra("ingredients", recipeList[position].ingredients!!.joinToString(separator = ", "))
                        startActivity(intent)
                    }
                })
                recipeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun addRecipe() {
        var myIntent : Intent = Intent(this, AddRecipeActivity::class.java)
        startActivity(myIntent)
    }

    private fun runNotes() {
        var myIntent : Intent = Intent(this, NotesActivity::class.java)
        startActivity(myIntent)
    }
    private fun logoutUser() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
        firebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}

