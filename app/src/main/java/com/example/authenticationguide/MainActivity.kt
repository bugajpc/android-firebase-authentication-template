package com.example.authenticationguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth

        val currentUser = auth.currentUser

        val emailEdit: EditText = findViewById(R.id.email_editText)
        val passwordEdit: EditText = findViewById(R.id.password_editText)
        val registerButton: Button = findViewById(R.id.register_button)
        val goToLogin: TextView = findViewById(R.id.go_to_login_textView)

        goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(emailEdit.text.toString(), passwordEdit.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        val user = auth.currentUser
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Hello:"+user!!.email.toString(),
                            Toast.LENGTH_SHORT,
                        ).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d("Main", "Nie zalogowany")
            Toast.makeText(
                baseContext,
                "Niezalogowany",
                Toast.LENGTH_SHORT,
            ).show()
        }
        else {
            Log.d("Main", "Zalogowany")
            Toast.makeText(
                baseContext,
                "Zalogowany",
                Toast.LENGTH_SHORT,
            ).show()
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}