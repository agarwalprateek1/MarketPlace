package com.example.marketplace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace.databinding.CartItemBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.cos

val s = "Total Cost : Rs. "
class CartAdapter(val context:Context, val list:ArrayList<Pair<String,Int>>, val emp:TextView, val cost:TextView, var totalCost:Long):RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding : CartItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pid = list[position].first
        val quantity = list[position].second
        var sp:Long = 0
        Firebase.firestore.collection("Products").document(pid).get().addOnSuccessListener {
            Glide.with(context).load(it.get("coverImage")).into(holder.binding.prodImgCart)
            holder.binding.nameCart.text = it.get("name") as String
            holder.binding.spCart.text = "Rs. "+it.get("sp") as String
            sp = it.get("sp").toString().toLong()
            holder.binding.brandCart.text = it.get("productBrand") as String
            holder.binding.quantity.text = quantity.toString()
        }.addOnFailureListener {
            Toast.makeText(context,"Something went wrong. Please try again!",Toast.LENGTH_SHORT).show()
        }
        holder.binding.plusCart.setOnClickListener {
            val user = Firebase.auth.currentUser
            Firebase.firestore.collection("users").document(user!!.uid).get().addOnSuccessListener {
                var mp : HashMap<String,Int> = it.get("cartItems") as HashMap<String,Int>
                val x = mp.get(pid)!!+1
                mp.put(pid!!,mp.get(pid)!!+1)
                Firebase.firestore.collection("users").document(user!!.uid).update("cartItems",mp).addOnSuccessListener {
                    holder.binding.quantity.text = x.toString()

                        totalCost+=sp
                        cost.text = s+totalCost.toString()

                }.addOnFailureListener {
                    Toast.makeText(context,"Something went wrong.",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context,"Something went wrong.",Toast.LENGTH_SHORT).show()
            }
        }
        holder.binding.minusCart.setOnClickListener {
            val user = Firebase.auth.currentUser
            Firebase.firestore.collection("users").document(user!!.uid).get().addOnSuccessListener {
                var mp : HashMap<String,Int> = it.get("cartItems") as HashMap<String,Int>

                val x = mp.get(pid)!!-1
                if(x==0){
                    mp.remove(pid)
                }
                else{
                    mp.put(pid!!,x)
                }
                Firebase.firestore.collection("users").document(user!!.uid).update("cartItems",mp).addOnSuccessListener {
                    if(x!=0){
                        holder.binding.quantity.text = x.toString()
                    }
                    else{
                        list.removeAt(position)
                        notifyDataSetChanged()
                        if(list.isEmpty()){
                            emp.visibility = View.VISIBLE
                        }
                        else emp.visibility = View.GONE
                    }
                        totalCost-=sp
                        cost.text = s+totalCost.toString()

                }.addOnFailureListener {
                    Toast.makeText(context,"Something went wrong.",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context,"Something went wrong.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}