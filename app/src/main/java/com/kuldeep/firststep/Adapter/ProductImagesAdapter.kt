package com.kuldeep.firststep.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuldeep.firststep.Model.ProductImages
import com.kuldeep.firststep.Model.Review
import com.kuldeep.firststep.R

class ProductImagesAdapter(private val context : Context, private val productImagesList : ArrayList<ProductImages>) : RecyclerView.Adapter<ProductImagesAdapter.ProductImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagesViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_images_list,
            parent,false)
        return ProductImagesViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ProductImagesViewHolder, position: Int) {
        val currentitem = productImagesList[position]

//        holder.photo.text = currentitem.image_url
        Glide.with(context).load(currentitem.image_url).into(holder.photo)
    }

    override fun getItemCount(): Int { return productImagesList.size }

    inner class ProductImagesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView

        init {
            photo =  itemView.findViewById(R.id.photo)
        }

    }

}