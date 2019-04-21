package me.sin.accountingapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

import com.wsy.accountingapp.R

import java.util.LinkedList

import me.sin.accountingapp.activity.AddRecordActivity
import me.sin.accountingapp.adapters.BillListViewAdapter
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil

@SuppressLint("ValidFragment")
class MainFragment @SuppressLint("ValidFragment")
constructor(date: String) : Fragment(), AdapterView.OnItemLongClickListener {

    private lateinit var rootView: View
    private lateinit var tvDay: TextView
    private lateinit var lvBill: ListView
    private lateinit var billListViewAdapter: BillListViewAdapter

    private var records: LinkedList<RecordBean>
    private var date = ""

    val totalCost: Int
        get() {
            var totalCost = 0.0
            for (record in records) {
                if (record.getType() == 1) {
                    totalCost -= record.amount
                } else {
                    totalCost += record.amount
                }
            }
            return totalCost.toInt()
        }

    init {
        this.date = date
        records = GlobalUtil.instance.databaseHelper.readRecords(date)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        initView()
        return rootView
    }

    fun reload() {

        records = GlobalUtil.instance.databaseHelper.readRecords(date)

        billListViewAdapter.setData(records)
        lvBill.adapter = billListViewAdapter

        if (billListViewAdapter.count > 0) {
            rootView.findViewById<View>(R.id.no_record_layout).visibility = View.INVISIBLE
        }
    }

    private fun initView() {
        tvDay = rootView.findViewById(R.id.tv_day)
        lvBill = rootView.findViewById(R.id.lv_bill)
        tvDay.text = date
        billListViewAdapter = activity?.let { BillListViewAdapter(it) }!!
        billListViewAdapter.setData(records)
        lvBill.adapter = billListViewAdapter

        if (billListViewAdapter.count > 0) {
            rootView.findViewById<View>(R.id.no_record_layout).visibility = View.INVISIBLE
        }

        tvDay.text = DateUtil.getDateTitle(date)

        lvBill.onItemLongClickListener = this
    }

    override fun onItemLongClick(parent: AdapterView<*>, view: View, position: Int, id: Long): Boolean {
        showDialog(position)
        return false
    }

    private fun showDialog(index: Int) {
        val options = arrayOf("移除", "编辑")
        val selectedRecord = records[index]
        val builder = AlertDialog.Builder(context)
        builder.create()
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    val uuid = selectedRecord.uuid
                    GlobalUtil.instance.databaseHelper.removeRecord(uuid)
                    reload()
                    GlobalUtil.instance.mainActivity.updateHeader()
                }
                1 -> {
                    val intent = Intent(activity, AddRecordActivity::class.java)
                    val extra = Bundle()
                    extra.putSerializable("record", selectedRecord)
                    intent.putExtras(extra)
                    startActivityForResult(intent, 1)
                }
            }
        }
        builder.setNegativeButton("取消", null)
        builder.setCancelable(false)
        builder.create().show()
    }
}
