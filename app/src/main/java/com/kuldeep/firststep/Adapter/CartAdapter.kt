package com.kuldeep.firststep.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuldeep.firststep.Model.Cart
import com.kuldeep.firststep.R


class CartAdapter(private var amount: Double, private val cartList: List<Cart>, private val context : Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    private var avl_quantity:Int=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.cart_item,
            parent, false)

        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartList[position]

        Log.d("222222222","byeeeeeeee")
        Glide.with(context).load(currentItem.imageResource).into(holder.imageView)
        holder.listNameView.text = currentItem.productName
        holder.priceView.text = currentItem.price
        avl_quantity = 10
    }

    override fun getItemCount() = cartList.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.cart_img1)
        val listNameView: TextView = itemView.findViewById(R.id.list_name)
        val priceView: TextView = itemView.findViewById(R.id.item_price)
        val quantityView: TextView = itemView.findViewById(R.id.quantity)

        init {
            val inc = itemView.findViewById<TextView>(R.id.add_item)
            val dec = itemView.findViewById<TextView>(R.id.remove_item)
            inc.setOnClickListener {
                increaseItem()
            }
            dec.setOnClickListener {
                decreaseItem()
            }
        }
        private fun increaseItem() {
            Log.d("jajajjajajajjajajaja","bababbabababba")
            if (avl_quantity != null) {
                if(quantityView.text.toString().toInt() < avl_quantity) {
                    quantityView.text = (quantityView.text.toString().toInt()+1).toString()
//                    amount = amount + priceView.toString().toDouble()
                }
            }
        }
        private fun decreaseItem() {
            Log.d("yayayayayayay","hahahahhahah")
            if(quantityView.text.toString().toInt() > 1) {
                quantityView.text = (quantityView.text.toString().toInt()-1).toString()
            }
        }
    }
}