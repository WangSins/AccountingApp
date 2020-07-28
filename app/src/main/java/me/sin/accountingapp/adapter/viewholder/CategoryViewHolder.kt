package me.sin.accountingapp.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.sin.accountingapp.R

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var categoryBg: ViewGroup = itemView.findViewById(R.id.cell_background)
    var categoryIv: ImageView = itemView.findViewById(R.id.iv_category)
    var categoryTv: TextView = itemView.findViewById(R.id.tv_category)
}
