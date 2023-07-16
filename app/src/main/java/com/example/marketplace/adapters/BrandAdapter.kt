package com.example.marketplace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace.classes.Categories
import com.example.marketplace.databinding.BrandItemBinding

class BrandAdapter(val context: Context,val list:ArrayList<Categories>):RecyclerView.Adapter<BrandAdapter.ViewHolder>(){
    class ViewHolder(val binding:BrandItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.ViewHolder {
        val binding = BrandItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandAdapter.ViewHolder, position: Int) {
        Glide.with(context).load(list[position].Image).into(holder.binding.brandImgView)
        holder.binding.brandImgView.contentDescription = list[position].Name
    }

    override fun getItemCount(): Int = list.size

}