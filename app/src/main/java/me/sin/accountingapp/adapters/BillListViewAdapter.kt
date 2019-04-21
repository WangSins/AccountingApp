package me.sin.accountingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.wsy.accountingapp.R

import java.util.LinkedList

import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.viewholder.BillViewHolder

class BillListViewAdapter(mContext: Context) : BaseAdapter() {

    private var records = LinkedList<RecordBean>()

    private val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(mContext)
    }

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
            convertView = mInflater.inflate(R.layout.cell_list_view, null)

            val recordBean = getItem(position) as RecordBean
            holder = BillViewHolder(convertView!!, recordBean)

            convertView.tag = holder

        } else {
            holder = convertView.tag as BillViewHolder
        }
        return convertView
    }
}

