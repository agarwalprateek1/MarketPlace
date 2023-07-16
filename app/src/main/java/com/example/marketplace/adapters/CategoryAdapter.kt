package com.example.marketplace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace.classes.Categories
import com.example.marketplace.databinding.CategoryItemBinding

class CategoryAdapter(val context: Context, val list:ArrayList<Categories>):RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    class ViewHolder(val binding:CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].Image).into(holder.binding.categoryImgView)
        holder.binding.categoryTextView.text = list[position].Name
    }

}