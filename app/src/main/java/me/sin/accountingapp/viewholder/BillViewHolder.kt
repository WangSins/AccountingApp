package me.sin.accountingapp.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.wsy.accountingapp.R

import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil

/**
 * Created by Sin on 2019/3/17
 */
class BillViewHolder(itemView: View, record: RecordBean) {
    private val remarkTV: TextView = itemView.findViewById(R.id.tv_remark)
    private val amountTV: TextView = itemView.findViewById(R.id.tv_amount)
    private val timeTV: TextView = itemView.findViewById(R.id.tv_time)
    private val categoryIcon: ImageView = itemView.findViewById(R.id.iv_category)

    init {

        remarkTV.text = record.remark

        if (record.getType() == 1) {
            amountTV.text = "- " + record.amount
        } else {
            amountTV.text = "+ " + record.amount
        }

        timeTV.text = DateUtil.getFormattedTime(record.timeStamp)
        categoryIcon.setImageResource(GlobalUtil.instance.getResourceIcon(record.category))
    }

}
