package me.sin.accountingapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import me.sin.accountingapp.R
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.viewholder.BillViewHolder
import java.util.*

class BillLVAdapter : BaseAdapter() {

    private var records = LinkedList<RecordBean>()

    fun setData(records: LinkedList<RecordBean>) {
        this.records = records
        notifyDataSetChanged()
    }

    override fun getCount(): Int = records.size

    override fun getItem(position: Int): Any = records[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: BillViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, null)

            val recordBean = getItem(position) as RecordBean
            holder = BillViewHolder(convertView!!, recordBean)

            convertView.tag = holder

        } else {
            holder = convertView.tag as BillViewHolder
        }
        return convertView
    }
}

