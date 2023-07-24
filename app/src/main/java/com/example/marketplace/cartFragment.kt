package com.example.marketplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.marketplace.adapters.CartAdapter
import com.example.marketplace.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class cartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    val s = "Total Cost : Rs. "
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth=Firebase.auth
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = requireContext().getSharedPreferences("info",AppCompatActivity.MODE_PRIVATE)
        preferences.edit().putBoolean("cart_opener",false).apply()
        val user = auth.currentUser
        Firebase.firestore.collection("users").document(user!!.uid).get().addOnSuccessListener {
            val mp : HashMap<String,Int> = it.get("cartItems") as HashMap<String,Int>
            val list = mp.toList()
            var list_main : ArrayList<Pair<String,Int>> = ArrayList()
            var cost:Long=0
            for(i in list){
                list_main.add(i)

                Firebase.firestore.collection("Products").document(i.first).get().addOnSuccessListener {

                    cost += it.get("sp").toString().toLong() * i.second
                    binding.tv1.text = s+cost.toString()
                    binding.cartRecycler.adapter = CartAdapter(requireContext(),list_main,binding.empty,binding.tv1,cost)


                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                }

            }


            if(list_main.isEmpty())
                binding.empty.visibility=View.VISIBLE
            else
                binding.empty.visibility=View.GONE
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
        }

        binding.checkout.setOnClickListener {
            
        }
    }

}