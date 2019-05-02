package me.sin.accountingapp.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import me.sin.accountingapp.R

/**
 * Created by Sin on 2019/3/17
 */
class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var background: RelativeLayout = itemView.findViewById(R.id.cell_background)
    var imageView: ImageView = itemView.findViewById(R.id.iv_category)
    var textView: TextView = itemView.findViewById(R.id.tv_category)

}
