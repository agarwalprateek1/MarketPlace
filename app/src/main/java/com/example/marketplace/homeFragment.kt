package com.example.marketplace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCategories()
        getBrands()
        getProducts()
        val brRecycler = view.findViewById<RecyclerView>(R.id.brandRecycler)
        val catRecycler = view.findViewById<RecyclerView>(R.id.categoryRecycler)
        val proRecycler = view.findViewById<RecyclerView>(R.id.productRecycler)
        brRecycler.adapter = BrandAdapter(requireContext(),brandList)
        catRecycler.adapter = CategoryAdapter(requireContext(),categoryList)
        proRecycler.adapter = ProductAdapter(requireContext(),productList)
    }
    private fun getProducts() {
        Firebase.firestore.collection("Products").get().addOnSuccessListener {
            productList=ArrayList()
            for(i in it.documents){
                productList.add(i.toObject(Products::class.java)!!)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBrands() {
        Firebase.firestore.collection("Brands").get().addOnSuccessListener {
            brandList=ArrayList()
            for(i in it.documents){
                brandList.add(i.toObject(Categories::class.java)!!)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCategories() {
        Firebase.firestore.collection("Categories").get().addOnSuccessListener {
            categoryList=ArrayList()
            for(i in it.documents){
                categoryList.add(i.toObject(Categories::class.java)!!)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }
}