package me.sin.accountingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.sin.accountingapp.R
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil
import me.sin.accountingapp.adapters.viewholder.BillViewHolder
import java.util.*

class BillAdapter : BaseAdapter() {

    private var mRecords = LinkedList<RecordBean>()

    fun setData(records: LinkedList<RecordBean>) {
        mRecords.clear()
        mRecords.addAll(records)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = mRecords.size

    override fun getItem(position: Int): Any = mRecords[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val holder: BillViewHolder
        if (itemView == null) {
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
            holder = BillViewHolder(itemView!!)
            itemView.tag = holder
        } else {
            holder = itemView.tag as BillViewHolder
        }
        with(getItem(position) as RecordBean) {
            holder.remarkTV.text = remark
            holder.amountTV.text = if (getType() == 1) {
                "- $amount"
            } else {
                "+ $amount"
            }
            holder.timeTV.text = DateUtil.getFormattedTime(timeStamp)
            holder.categoryIv.setImageResource(GlobalUtil.getResourceIcon(category))
        }
        return itemView
    }
}

