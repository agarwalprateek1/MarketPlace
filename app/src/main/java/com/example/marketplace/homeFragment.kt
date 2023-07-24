package com.example.marketplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.marketplace.adapters.BrandAdapter
import com.example.marketplace.adapters.CategoryAdapter
import com.example.marketplace.adapters.ProductAdapter
import com.example.marketplace.classes.Categories
import com.example.marketplace.classes.Products
import com.example.marketplace.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class homeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    var brandList:ArrayList<Categories> = ArrayList()
    var categoryList:ArrayList<Categories> = ArrayList()
    var productList:ArrayList<Products> = ArrayList()
    lateinit var proRecycler:RecyclerView
    lateinit var catRecycler:RecyclerView
    lateinit var brRecycler:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(layoutInflater)
        proRecycler = binding.productRecycler
        catRecycler = binding.categoryRecycler
        brRecycler = binding.brandRecycler
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences = requireContext().getSharedPreferences("info",AppCompatActivity.MODE_PRIVATE)
        if(preferences.getBoolean("cart_opener",false)){
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }
        getCategories()
        getBrands()
        getProducts()

    }
    private fun getProducts() {
        Firebase.firestore.collection("Products").get().addOnSuccessListener {
            productList=ArrayList()
            for(i in it.documents){
                productList.add(i.toObject(Products::class.java)!!)
            }

            proRecycler.adapter = ProductAdapter(requireContext(),productList)
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Something went wrong.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBrands() {
        Firebase.firestore.collection("Brands").get().addOnSuccessListener {
            brandList=ArrayList()
            for(i in it.documents){
                brandList.add(i.toObject(Categories::class.java)!!)
            }

            brRecycler.adapter = BrandAdapter(requireContext(),brandList)
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Something went wrong.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCategories() {
        Firebase.firestore.collection("Categories").get().addOnSuccessListener {
            categoryList=ArrayList()
            for(i in it.documents){
                categoryList.add(i.toObject(Categories::class.java)!!)
            }

            catRecycler.adapter = CategoryAdapter(requireContext(),categoryList)
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Something went wrong.",Toast.LENGTH_SHORT).show()
        }
    }
}