package com.example.authenticationguide

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostAdapter(val posts: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    val db = Firebase.firestore
    val auth = Firebase.auth
    val currentUser = auth.currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userId: TextView = holder.itemView.findViewById(R.id.uid_item_textView)
        val text: TextView = holder.itemView.findViewById(R.id.text_item_textView)
        val deleteimageView: ImageView = holder.itemView.findViewById(R.id.delete_imageView)
        userId.text = posts[position].uid
        text.text = posts[position].text
        if(posts[position].uid != currentUser!!.uid)
        {
            deleteimageView.visibility = View.INVISIBLE
        }
        deleteimageView.setOnClickListener {
            if(posts[position].uid == currentUser!!.uid)
            {
                AlertDialog.Builder(it.context)
                    .setTitle("Uwaga")
                    .setMessage("Czy na pewno chcesz usunąć post?")
                    .setPositiveButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("Usuń") { dialog, which ->
                        db.collection("posts").document(posts[position].id)
                            .delete()
                            .addOnSuccessListener {
                                posts.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, posts.size)
                            }
                            .addOnFailureListener {  }
                    }.show()
            }

        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}