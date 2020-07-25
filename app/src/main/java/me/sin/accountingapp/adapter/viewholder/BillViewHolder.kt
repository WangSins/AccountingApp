package me.sin.accountingapp.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import me.sin.accountingapp.R

class BillViewHolder(itemView: View) {
    val remarkTV: TextView = itemView.findViewById(R.id.tv_remark)
    val amountTV: TextView = itemView.findViewById(R.id.tv_amount)
    val timeTV: TextView = itemView.findViewById(R.id.tv_time)
    val categoryIv: ImageView = itemView.findViewById(R.id.iv_category)
}
