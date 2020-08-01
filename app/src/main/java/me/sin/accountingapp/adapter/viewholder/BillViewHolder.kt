package me.sin.accountingapp.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import me.sin.accountingapp.R

class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val remarkTV: TextView = itemView.findViewById(R.id.tv_remark)
    val amountTV: TextView = itemView.findViewById(R.id.tv_amount)
    val timeTV: TextView = itemView.findViewById(R.id.tv_time)
    val categoryIv: ImageView = itemView.findViewById(R.id.iv_category)
}
