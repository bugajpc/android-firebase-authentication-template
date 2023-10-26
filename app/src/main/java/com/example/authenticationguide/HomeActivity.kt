package com.example.authenticationguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = Firebase.auth
        val userIdTextView: TextView = findViewById(R.id.user_id_textView)
        val signOutButton: Button = findViewById(R.id.sign_out_button)

        val addPostImageView: ImageView = findViewById(R.id.addPost_imageView)
        val userPostsImageView: ImageView = findViewById(R.id.userPosts_imageView)
        val homeRecyclerView: RecyclerView = findViewById(R.id.homeRecyclerView)
        val currentUser = auth.currentUser

        var posts = mutableListOf<Post>()

        val adapter = PostAdapter(posts)
        homeRecyclerView.adapter = adapter
        homeRecyclerView.layoutManager = LinearLayoutManager(this)

        val db = Firebase.firestore
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Home", "${document.id} => ${document.data}")
                    posts.add(
                        Post(document.id.toString(), document.data["uid"].toString(), document.data["text"].toString())
                    )
                }
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.d("Home", "Error getting documents: ", exception)
            }

        addPostImageView.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }

        userPostsImageView.setOnClickListener {
            val intent = Intent(this, UserPostsActivity::class.java)
            startActivity(intent)
        }

        signOutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        if(currentUser != null)
        {
            userIdTextView.text = currentUser.email.toString()
        }
        else
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}