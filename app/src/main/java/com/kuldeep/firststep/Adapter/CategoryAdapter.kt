package com.kuldeep.firststep.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuldeep.firststep.AllProductActivity
import com.kuldeep.firststep.Model.Category
import com.kuldeep.firststep.Model.Product
import com.kuldeep.firststep.ProductDescriptionActivity
import com.kuldeep.firststep.R
import java.util.ArrayList

class CategoryAdapter(private val categoryList: List<Category>, private val productList: List<Product>, private val context : Context): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.category_view,
            parent, false)

        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = categoryList[position]
        val params = holder.categoryCardView.layoutParams as ViewGroup.MarginLayoutParams
        if (position%2 == 0){
            params.setMargins(48,24,0,24)
            holder.categoryCardView.layoutParams = params
            holder.productRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        } else {
            params.setMargins(0,24,48,24)
            holder.categoryCardView.layoutParams = params
            holder.productRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,true)

        }

        holder.categoryName.text = currentItem.categoryName

        val productAdapter = ProductAdapter(position,currentItem.productList!!, context)
       // holder.productRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        holder.productRv.setHasFixedSize(true)
        holder.productRv.adapter = productAdapter

        holder.seeAll.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = Intent(context, AllProductActivity::class.java)
                context.startActivity(intent)
            }
        })


//        Glide.with(context).load(currentItem.imageResource).into(holder.imageView)
//        holder.productName.text = currentItem.productName
//        holder.productBrand.text = currentItem.brand
//        holder.productCategory.text = currentItem.category
//        holder.productPrice.text = "₹"+currentItem.price
//        holder.productAddedDate.text = currentItem.dateAdded
        //Log.d("33333333333",Glide.with(context).load(currentItem.imageResource).toString())
    }

    override fun getItemCount() = categoryList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryCardView: CardView = itemView.findViewById(R.id.categoryCardView)
        val categoryName: TextView = itemView.findViewById(R.id.tvcategory)
        val productRv: RecyclerView = itemView.findViewById((R.id.product_list))
        val seeAll: TextView =  itemView.findViewById(R.id.tvSeeAll)

//        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
//        val productName: TextView = itemView.findViewById(R.id.tvProductName)
//        val productBrand: TextView = itemView.findViewById(R.id.tvProductBrand)
//        val productCategory: TextView = itemView.findViewById(R.id.tvProductCategory)
//        val productPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
//        val productAddedDate: TextView = itemView.findViewById(R.id.tvProductAddedDate)

    }

}