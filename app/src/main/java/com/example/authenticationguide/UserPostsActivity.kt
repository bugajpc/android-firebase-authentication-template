package com.example.authenticationguide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserPostsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_posts)
        val db = Firebase.firestore
        auth = Firebase.auth
        val user = auth.currentUser

        val posts = mutableListOf<Post>()

        val userPostsRecyclerView:RecyclerView = findViewById(R.id.userPosts_recyclerView)
        val adapter = PostAdapter(posts)
        userPostsRecyclerView.adapter = adapter
        userPostsRecyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("posts")
            .whereEqualTo("uid", user!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("UserPosts", "${document.id} => ${document.data}")
                    posts.add(
                        Post(document.id ,document.data["uid"].toString(), document.data["text"].toString())
                    )
                }
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.w("UserPosts", "Error getting documents: ", exception)
            }
    }
}