package com.example.marketplace

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marketplace.classes.Users
import com.example.marketplace.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        binding.loginIntent.setOnClickListener {
            val i = Intent(this,LoginActivity::class.java)
            startActivity(i)
        }
        binding.register.setOnClickListener {
            val x = validateCredentials()
            if(x==1){
                addUser()
            }
            else if(x==2){
                Toast.makeText(this,"Password must be at least 8 characters long.",Toast.LENGTH_SHORT).show()
            }
            else if(x==3){
                Toast.makeText(this,"Invalid E-mail Address!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser() {
        val email = binding.emailReg.editText!!.text.toString()
        val pass = binding.passReg.editText!!.text.toString()
        val name = binding.nameReg.editText!!.text.toString()
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()
                    user!!.updateProfile(profileUpdates).addOnCompleteListener(this){
                        if(it.isSuccessful){
                            var data: Users = Users("", HashMap())
                            Firebase.firestore.collection("users").document(user!!.uid).set(data).addOnSuccessListener {
                                Toast.makeText(this,"Welcome, ${user.displayName}!",Toast.LENGTH_SHORT).show()
                                val i = Intent(this,MainActivity::class.java)
                                startActivity(i)
                            }.addOnFailureListener {
                                Toast.makeText(this,"Authentication Failed.",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this,"Authentication Failed.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateCredentials() : Int {
        val s1 = binding.emailReg.editText!!.text.toString()
        val s2 = binding.passReg.editText!!.text.toString()
        return if(s1.isNotBlank() && s1.contains("@")){
            if(s2.length>=8) 1
            else 2
        } else 3
    }
}