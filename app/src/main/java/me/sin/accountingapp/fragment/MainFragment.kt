package me.sin.accountingapp.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import me.sin.accountingapp.R
import me.sin.accountingapp.activity.AddRecordActivity
import me.sin.accountingapp.adapter.BillAdapter
import me.sin.accountingapp.base.BaseFragment
import me.sin.accountingapp.constant.Constant
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.database.RecordDBDao
import me.sin.accountingapp.util.DateUtil
import java.util.*

class MainFragment : BaseFragment() {

    private var mBillAdapter: BillAdapter = BillAdapter()
    private lateinit var mRecords: LinkedList<RecordBean>
    private var mDate = ""

    override fun getLayoutResId(): Int = R.layout.fragment_main

    override fun initData() {
        lv_bill?.adapter = mBillAdapter
        refresh()
    }

    override fun initEvent() {
        lv_bill?.setOnItemLongClickListener { _, _, position, _ ->
            showDialog(position)
            false
        }
    }

    fun refresh() {
        mDate = arguments?.getString(Constant.KEY_DATE).toString()
        mRecords = RecordDBDao.readRecords(mDate)
        mBillAdapter.setData(mRecords)
        no_record_layout?.visibility = if (mBillAdapter.count > 0) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
        tv_day?.text = DateUtil.getDateTitle(mDate)
    }

    fun getTotalCost(): Int {
        mDate = arguments?.getString(Constant.KEY_DATE).toString()
        mRecords = RecordDBDao.readRecords(mDate)
        var totalCost = 0.0
        for (record in mRecords) {
            if (record.type == RecordBean.TYPE_EXPENSE) {
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
                            RecordDBDao.removeRecord(it)
                        }
                        refresh()
                        activity?.let {
                            LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(Constant.ACTION_UPDATE_HEADER))
                        }
                    }
                    1 -> {
                        Bundle().run {
                            putSerializable(Constant.KEY_RECORD, selectedRecord)
                            startActivityForResult(AddRecordActivity::class.java, this, 1)
                        }

                    }
                }
            }
            it.setNegativeButton("取消", null)
            it.setCancelable(false)
            it.create().show()
        }
    }
}
