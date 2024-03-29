package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.myapplication.databinding.ActivityEmailSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class SignUp : AppCompatActivity() {
    lateinit var binding: ActivityEmailSignUpBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.bBackButtton.setOnClickListener {
           // startActivity(Intent(this, SignIn::class.java))
            this.finish()
        }
        binding.bEmailSingup.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etNickname.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d(TAG, "onCreate: $email $password")
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (name != ""){
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

// to test if is working
                        databaseReference.child(user!!.uid).child("email").setValue(user.email)
                        databaseReference.child(user.uid).child("passwd").setValue(password)
                        databaseReference.child(user.uid).child("name").setValue(name)

                        startActivity(Intent(this, MainActivity2::class.java))
                        this.finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                    } else{
                        Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}