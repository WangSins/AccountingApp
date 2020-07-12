package me.sin.accountingapp.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import me.sin.accountingapp.R
import me.sin.accountingapp.activity.AddRecordActivity
import me.sin.accountingapp.adapters.BillAdapter
import me.sin.accountingapp.base.BaseFragment
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil
import java.util.*

class MainFragment : BaseFragment() {

    private var mBillAdapter: BillAdapter = BillAdapter()
    private lateinit var mRecords: LinkedList<RecordBean>
    private var mDate = ""

    override fun getLayoutResId(): Int = R.layout.fragment_main

    override fun initData() {
        upData()
        setData()
    }

    override fun initEvent() {
        lv_bill?.setOnItemLongClickListener { _, _, position, _ ->
            showDialog(position)
            false
        }
    }

    private fun upData() {
        mDate = arguments?.getString("dateKey").toString()
        mRecords = GlobalUtil.databaseHelper.readRecords(mDate)
    }

    private fun setData() {
        mBillAdapter.setData(mRecords)
        lv_bill?.adapter = mBillAdapter
        if (mBillAdapter.count > 0) {
            no_record_layout?.visibility = View.INVISIBLE
        }
        tv_day?.text = DateUtil.getDateTitle(mDate)
    }

    fun reload() {
        upData()
        mRecords = GlobalUtil.databaseHelper.readRecords(mDate)
        mBillAdapter.setData(mRecords)
        if (mBillAdapter.count > 0) {
            no_record_layout?.visibility = View.INVISIBLE
        }
    }

    fun getTotalCost(): Int {
        upData()
        var totalCost = 0.0
        for (record in mRecords) {
            if (record.getType() == 1) {
                totalCost -= record.amount
            } else {
                totalCost += record.amount
            }
        }
        return totalCost.toInt()
    }

    private fun showDialog(index: Int) {
        val options = arrayOf("移除", "编辑")
        val selectedRecord = mRecords[index]
        AlertDialog.Builder(context).let { it ->
            it.create()
            it.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        selectedRecord.uuid.let {
                            GlobalUtil.databaseHelper.removeRecord(it)
                        }
                        reload()
                        val intent = Intent("updateHeader")
                        activity?.let {
                            LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
                        }
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
