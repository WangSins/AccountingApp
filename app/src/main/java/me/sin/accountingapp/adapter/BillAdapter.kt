package me.sin.accountingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.sin.accountingapp.R
import me.sin.accountingapp.adapter.viewholder.BillViewHolder
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.util.DateUtil
import me.sin.accountingapp.util.ResUtil
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
            holder.amountTV.text = if (type == RecordBean.TYPE_EXPENSE) {
                "- $amount"
            } else {
                "+ $amount"
            }
            holder.timeTV.text = DateUtil.getFormattedTime(timeStamp)
            holder.categoryIv.setImageResource(ResUtil.getResourceIcon(category))
        }
        return itemView
    }
}

