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
import me.sin.accountingapp.activity.AddRecordActivity
import me.sin.accountingapp.adapters.BillLVAdapter
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil
import java.util.*

@SuppressLint("ValidFragment")
class MainFragment constructor(date: String) : Fragment(), AdapterView.OnItemLongClickListener {

    private lateinit var rootView: View
    private lateinit var tvDay: TextView
    private lateinit var lvBill: ListView
    private lateinit var billLVAdapter: BillLVAdapter

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

        billLVAdapter.setData(records)
        lvBill.adapter = billLVAdapter

        if (billLVAdapter.count > 0) {
            rootView.findViewById<View>(R.id.no_record_layout).visibility = View.INVISIBLE
        }
    }

    private fun initView() {
        tvDay = rootView.findViewById(R.id.tv_day)
        lvBill = rootView.findViewById(R.id.lv_bill)
        tvDay.text = date
        billLVAdapter = activity?.let { BillLVAdapter(it) }!!
        billLVAdapter.setData(records)
        lvBill.adapter = billLVAdapter

        if (billLVAdapter.count > 0) {
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
        AlertDialog.Builder(context).let {
            it.create()
            it.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        selectedRecord.uuid.let {
                            GlobalUtil.instance.databaseHelper.removeRecord(it)
                        }
                        reload()
                        GlobalUtil.instance.mainActivity.updateHeader()
                    }
                    1 -> {
                        val extra = Bundle().apply {
                            putSerializable("record", selectedRecord)
                        }
                        val intent = Intent(activity, AddRecordActivity::class.java).apply {
                            putExtras(extra)
                        }
                        startActivityForResult(intent, 1)
                    }
                }
            }
            it.setNegativeButton("取消", null)
            it.setCancelable(false)
            it.create().show()
        }
    }
}
