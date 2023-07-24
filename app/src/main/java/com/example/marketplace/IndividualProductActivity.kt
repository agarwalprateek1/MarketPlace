package com.example.marketplace

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.marketplace.adapters.s
import com.example.marketplace.databinding.ActivityIndividualProductBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class IndividualProductActivity : AppCompatActivity() {

    private lateinit var binding:ActivityIndividualProductBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIndividualProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        supportActionBar?.title = "Product Details"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val pid= intent.getStringExtra("pid")
        val user = auth.currentUser
        getData(pid)
        binding.gotocart.setOnClickListener {
            val prefernces = this.getSharedPreferences("info", MODE_PRIVATE)
            prefernces.edit().putBoolean("cart_opener",true).apply()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.addtocart.setOnClickListener {
            Firebase.firestore.collection("users").document(user!!.uid).get().addOnSuccessListener {
                var mp = it.get("cartItems") as HashMap<String,Int>
                if(mp.contains(pid)){
                    mp.put(pid!!,mp.get(pid)!!+1)
                }
                else{
                    mp.put(pid!!,1)
                }
                Firebase.firestore.collection("users").document(user!!.uid).update("cartItems",mp).addOnSuccessListener {
                    Snackbar.make(binding.root,"Item Added to Cart!",Snackbar.LENGTH_LONG)
                        .setAction("UNDO") {
                            Firebase.firestore.collection("users").document(user!!.uid).get()
                                .addOnSuccessListener {
                                    var mp = it.get("cartItems") as HashMap<String, Int>
                                    val x = mp.get(pid)!! - 1
                                    if (x == 0) {
                                        mp.remove(pid)
                                    } else {
                                        mp.put(pid!!, x)
                                    }
                                    Firebase.firestore.collection("users").document(user!!.uid)
                                        .update("cartItems", mp).addOnSuccessListener {
                                        Snackbar.make(
                                            binding.root,
                                            "Item Removed from Cart!",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            this,
                                            "Something went wrong.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }.addOnFailureListener {
                                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        .show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Something went wrong.",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,"Something went wrong.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData(pid:String?) {
        Firebase.firestore.collection("Products").document(pid!!).get().addOnSuccessListener {
            val list = ArrayList<SlideModel>()
            val list1 = it.get("images") as ArrayList<String>
            for(i in 0 until list1.size){
                list.add(SlideModel(list1[i], ScaleTypes.CENTER_INSIDE))
            }
            binding.slideshow.setImageList(list)
            binding.tv1.text=it.get("name").toString()
            binding.tv2.text=it.get("productBrand").toString()
            binding.tv3.text=it.get("description").toString()
            binding.tv4.text="MRP : Rs."+it.get("mrp").toString()+"/-"
            binding.tv5.text="Our Price : Rs."+it.get("sp").toString()+"/-"
        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong.", Toast.LENGTH_SHORT).show()
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
                i.action=Intent.ACTION_SENDTO
                i.data= Uri.parse("mailto:agarwalprateek520@gmail.com")
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}