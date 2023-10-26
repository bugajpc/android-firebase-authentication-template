package com.example.authenticationguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddPostActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val addButton: Button = findViewById(R.id.add_button)
        val addEditText: EditText = findViewById(R.id.add_editTextText)
        auth = Firebase.auth
        val db = Firebase.firestore
        val user = auth.currentUser

        addButton.setOnClickListener {

            val post = hashMapOf(
                "text" to addEditText.text.toString(),
                "uid" to user!!.uid
            )

            db.collection("posts")
                .add(post)
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully written!")
                    Toast.makeText(
                        baseContext,
                        "Dodano post",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
        }
    }
}