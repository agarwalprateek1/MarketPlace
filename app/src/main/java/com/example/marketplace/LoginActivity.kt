package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.marketplace.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        val user = auth.currentUser
        if(user!=null){
            Toast.makeText(applicationContext,"Welcome, ${user!!.displayName}!",Toast.LENGTH_SHORT).show()
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        binding.regIntent.setOnClickListener {
            val i = Intent(this,RegisterActivity::class.java)
            startActivity(i)
        }
        binding.login.setOnClickListener {
            val x = validateCredentials()
            when (x) {
                1 -> {
                    addUser()
                }
                2 -> {
                    Toast.makeText(this,"Password must be at least 8 characters long.",Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    Toast.makeText(this,"Invalid E-mail Address!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addUser() {
        val email = binding.emailLogin.editText!!.text.toString()
        val pass = binding.passLogin.editText!!.text.toString()
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(applicationContext,"Welcome, ${user!!.displayName}!",Toast.LENGTH_SHORT).show()
                    binding.emailLogin.editText!!.text.clear()
                    binding.passLogin.editText!!.text.clear()
                    val i = Intent(this,MainActivity::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(this,"Authentication failed.",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateCredentials() : Int {
        val s1 = binding.emailLogin.editText!!.text.toString()
        val s2 = binding.passLogin.editText!!.text.toString()
        return if(s1.isNotBlank() && s1.contains("@")){
            if(s2.length>=8) 1
            else 2
        } else 3
    }
}