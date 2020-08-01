package me.sin.accountingapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.sin.accountingapp.R
import me.sin.accountingapp.adapter.viewholder.BillViewHolder
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.util.DateUtil
import me.sin.accountingapp.util.ResUtil
import java.util.*

/**
 * Created by Sin on 2020/8/2
 */
class BillAdapter : RecyclerView.Adapter<BillViewHolder>() {

    private var mRecords = LinkedList<RecordBean>()
    private lateinit var onBillClickListener: OnBillClickListener

    fun setData(records: LinkedList<RecordBean>) {
        mRecords.clear()
        mRecords.addAll(records)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BillViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_bill, p0, false)
        return BillViewHolder(view)
    }

    override fun getItemCount(): Int = mRecords.size

    override fun onBindViewHolder(p0: BillViewHolder, p1: Int) {
        with(mRecords[p1]) {
            p0.remarkTV.text = remark
            p0.amountTV.text = if (type == RecordBean.TYPE_EXPENSE) {
                "- $amount"
            } else {
                "+ $amount"
            }
            p0.timeTV.text = DateUtil.getFormattedTime(timeStamp)
            p0.categoryIv.setImageResource(ResUtil.getResourceIcon(category))
            p0.itemView.setOnLongClickListener {
                onBillClickListener.onItemLongClick(it, p1)
                false
            }
        }
    }

    interface OnBillClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    fun setOnBillClickListener(listener: OnBillClickListener) {
        onBillClickListener = listener
    }

}