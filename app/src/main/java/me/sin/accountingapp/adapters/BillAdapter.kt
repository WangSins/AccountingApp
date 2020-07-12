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
        var convertView = convertView
        val holder: BillViewHolder
        val recordBean = getItem(position) as RecordBean
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
            holder = BillViewHolder(convertView!!)
            convertView.tag = holder
        } else {
            holder = convertView.tag as BillViewHolder
        }
        holder.remarkTV.text = recordBean.remark
        holder.amountTV.text = if (recordBean.getType() == 1) {
            "- ${recordBean.amount}"
        } else {
            "+ ${recordBean.amount}"
        }
        holder.timeTV.text = DateUtil.getFormattedTime(recordBean.timeStamp)
        holder.categoryIv.setImageResource(GlobalUtil.instance.getResourceIcon(recordBean.category))
        return convertView
    }
}

