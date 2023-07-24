package com.example.marketplace

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentPagerAdapter
import com.example.marketplace.classes.Users
import com.example.marketplace.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class profileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth=Firebase.auth
        val user = auth.currentUser
        if(user==null){
            Toast.makeText(requireContext(),"Not signed in.",Toast.LENGTH_SHORT).show()
        }
        binding.currName.text = user?.displayName
        binding.currEmail.text = user?.email
        Firebase.firestore.collection("users").document(user!!.uid).get().addOnSuccessListener {
            val s = it.get("address")
            binding.address.editText!!.text.append(s.toString())
        }
        binding.logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(),"Successfully signed out.",Toast.LENGTH_SHORT).show()
            val i = Intent(requireContext(),LoginActivity::class.java)
            startActivity(i)
        }
        binding.del.setOnClickListener {
            val uid= user.uid
            user.delete().addOnSuccessListener {
                Firebase.firestore.collection("users").document(uid).delete().addOnSuccessListener {
                    Toast.makeText(requireContext(), "Account deleted.", Toast.LENGTH_SHORT).show()
                    val i = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(i)
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.updateAddress.setOnClickListener {
            val s = binding.address.editText!!.text.toString()
            if(s.isEmpty()){
                Toast.makeText(requireContext(),"Please add an address.",Toast.LENGTH_SHORT).show()
            }
            else{
                Firebase.firestore.collection("users").document(user!!.uid).update("address",s).addOnSuccessListener {
                    Toast.makeText(requireContext(),"Address Updated!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}