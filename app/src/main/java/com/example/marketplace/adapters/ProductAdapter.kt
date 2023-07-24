package com.example.marketplace.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketplace.IndividualProductActivity
import com.example.marketplace.ProductDisplayActivity
import com.example.marketplace.classes.Products
import com.example.marketplace.databinding.ProductItemBinding

class ProductAdapter(val context:Context, val list:ArrayList<Products>):RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    class ViewHolder(val binding:ProductItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].CoverImage).into(holder.binding.prodImgView)
        holder.binding.nameTV.text=list[position].Name
        holder.binding.brandTV.text=list[position].ProductBrand
        holder.binding.mrpTV.text="MRP : Rs."+list[position].MRP+"/-"
        holder.binding.spTV.text="Our Price : Rs."+list[position].SP+"/-"
        holder.binding.categoryTV.text=list[position].ProductCategory
        holder.itemView.setOnClickListener {
            val i=Intent(context,IndividualProductActivity::class.java)
            i.putExtra("pid",list[position].ProductID)
            context.startActivity(i)
        }
    }

}