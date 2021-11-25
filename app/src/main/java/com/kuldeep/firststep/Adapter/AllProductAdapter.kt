package com.kuldeep.firststep.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuldeep.firststep.Model.Product
import com.kuldeep.firststep.ProductDescriptionActivity
import com.kuldeep.firststep.R

class AllProductAdapter(private val allProductList: List<Product>, private val context : Context) : RecyclerView.Adapter<AllProductAdapter.AllProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_view,
            parent, false)

        return AllProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllProductViewHolder, position: Int) {
        val currentItem = allProductList[position]

        Glide.with(context).load(currentItem.imageResource).into(holder.imageView)
        holder.productId.text = currentItem.productId
        holder.productName.text = currentItem.productName
        holder.productBrand.text = currentItem.brand
//        holder.productCategory.text = currentItem.category
        holder.productPrice.text = "₹"+currentItem.price
        holder.productAddedDate.text = currentItem.dateAdded
        //Log.d("33333333333",Glide.with(context).load(currentItem.imageResource).toString())
        holder.productView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = Intent(context, ProductDescriptionActivity::class.java)
                intent.putExtra("productToBeDisplayed",currentItem.productId)
                context.startActivity(intent)
            }
        })
    }


    override fun getItemCount() = allProductList.size

    inner class AllProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        val productId: TextView = itemView.findViewById(R.id.productId)
        val productName: TextView = itemView.findViewById(R.id.tvProductName)
        val productBrand: TextView = itemView.findViewById(R.id.tvProductBrand)
        //        val productCategory: TextView = itemView.findViewById(R.id.tvProductCategory)
        val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        val productAddedDate: TextView = itemView.findViewById(R.id.tvProductAddedDate)
        val productCardView: CardView = itemView.findViewById(R.id.productCardView)

        val productView : ConstraintLayout

        init {
            productView = itemView.findViewById<ConstraintLayout>(R.id.productView)
        }

    }

}