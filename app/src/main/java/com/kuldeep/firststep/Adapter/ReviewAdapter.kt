package com.kuldeep.firststep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kuldeep.firststep.Model.Review
import com.kuldeep.firststep.R

class ReviewAdapter(private val reviewList : ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.review_list,
            parent,false)
        return ReviewViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentitem = reviewList[position]

        holder.review.text = currentitem.review
    }

    override fun getItemCount(): Int { return reviewList.size }

    inner class ReviewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val review: TextView

        init {
            review =  itemView.findViewById(R.id.tvreview)
        }

    }

    }