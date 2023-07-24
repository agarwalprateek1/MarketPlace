package com.example.marketplace

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.marketplace.adapters.ProductAdapter
import com.example.marketplace.classes.Products
import com.example.marketplace.databinding.ActivityProductDisplayBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProductDisplayActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductDisplayBinding
    var list:ArrayList<Products> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i = intent
        val s1=i.getIntExtra("choice",3)

        if(s1==1){
                //brand
                val s2=i.getStringExtra("name")
                binding.specifyTV.text=s2.toString().uppercase()
                getData(s1,s2)
            }
        else if(s1==2){
                //category
                val s2=i.getStringExtra("name")
                binding.specifyTV.text=s2.toString().uppercase()
                getData(s1,s2)
            }
        else if(s1==3){
                //all products
                binding.specifyTV.text="ALL PRODUCTS"
                getDataAll()
        }
    }

    private fun getDataAll() {
        Firebase.firestore.collection("Products").get().addOnSuccessListener {
            list=ArrayList()
            for(i in it.documents){
                list.add(i.toObject(Products::class.java)!!)
            }
            binding.productActivityRecycler.adapter = ProductAdapter(this,list)
        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getData(s1: Int, s2: String?) {
        var x=""
        if(s1==1){
            x="productBrand"
        }
        else if(s1==2){
            x="productCategory"
        }
        Firebase.firestore.collection("Products").whereEqualTo(x,s2).get().addOnSuccessListener {
            list=ArrayList()
            for(i in it.documents){
                list.add(i.toObject(Products::class.java)!!)
            }
            binding.productActivityRecycler.adapter = ProductAdapter(this,list)
        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong.",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.contact_support -> {
                val i = Intent()
                i.action= Intent.ACTION_SENDTO
                i.data= Uri.parse("mailto:agarwalprateek520@gmail.com")
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}