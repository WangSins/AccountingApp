package me.sin.accountingapp.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.include_soft_keyboard.*
import me.sin.accountingapp.R
import me.sin.accountingapp.adapter.CategoryAdapter
import me.sin.accountingapp.base.BaseActivity
import me.sin.accountingapp.constant.Constant
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.database.RecordDBDao
import me.sin.accountingapp.util.DateUtil
import java.util.*


class AddRecordActivity : BaseActivity(), View.OnClickListener {

    private var mUserInput = ""
    private lateinit var mCategoryAdapter: CategoryAdapter
    private var mCurrentCategory: String = "全部"
    private var mType: Int = RecordBean.TYPE_EXPENSE
    private val mRemark = mCurrentCategory
    private var mRecord = RecordBean()
    private var mDate = DateUtil.currentDate
    private var inEdit = false

    override fun getLayoutResId(): Int = R.layout.activity_add_record

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_record, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.history -> {
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    if (DateUtil.afterToday(year, month + 1, dayOfMonth)) {
                        toast("不能预知未来")
                        return@OnDateSetListener
                    }
                    mDate = DateUtil.getDateStr(year, month + 1, dayOfMonth).toString()
                }, mDate.substring(0, 4).toInt(), mDate.substring(5, 7).toInt() - 1, mDate.substring(8, 10).toInt()).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {
        et_name?.setText(mRemark)
        mCategoryAdapter = CategoryAdapter()
        rv_category?.let {
            it.adapter = mCategoryAdapter
            it.layoutManager = GridLayoutManager(this, 4)
        }
        val recordExtra = intent.getSerializableExtra(Constant.KEY_RECORD)
        if (recordExtra != null) {
            inEdit = true
            mRecord = recordExtra as RecordBean
        }
        val dateExtra = intent.getStringExtra(Constant.KEY_DATE)
        if (dateExtra != null) {
            mDate = dateExtra
        }
    }

    override fun initEvent() {
        keyboard_one.setOnClickListener(this)
        keyboard_two.setOnClickListener(this)
        keyboard_three.setOnClickListener(this)
        keyboard_four.setOnClickListener(this)
        keyboard_five.setOnClickListener(this)
        keyboard_six.setOnClickListener(this)
        keyboard_seven.setOnClickListener(this)
        keyboard_eight.setOnClickListener(this)
        keyboard_nine.setOnClickListener(this)
        keyboard_zero.setOnClickListener(this)
        //返回
        keyboard_backspace.setOnClickListener {
            if (mUserInput.isNotEmpty()) {
                mUserInput = mUserInput.substring(0, mUserInput.length - 1)
            }
            if (mUserInput.isNotEmpty() && mUserInput[mUserInput.length - 1] == '.') {
                mUserInput = mUserInput.substring(0, mUserInput.length - 1)
            }
            updateAmountText()
        }
        //完成
        keyboard_done.setOnClickListener {
            Log.e("wsy", "handleDone:mUserInput --> $mUserInput ")
            if (userInputIsNotZero()) {
                with(mRecord) {
                    type = if (mType == RecordBean.TYPE_EXPENSE) {
                        RecordBean.TYPE_EXPENSE
                    } else {
                        RecordBean.TYPE_INCOME
                    }
                    category = mCategoryAdapter.getCurrentSelected()
                    remark = et_name.text.toString()
                    amount = mUserInput.toDouble()
                    if (inEdit) {
                        RecordDBDao.editRecord(uuid, this)
                    } else {
                        uuid = UUID.randomUUID().toString()
                        timeStamp = System.currentTimeMillis()
                        date = mDate
                        RecordDBDao.addRecord(this)
                    }
                }
                setResult(2, Intent().apply {
                    putExtra(Constant.KEY_DATE, mDate)
                })
                finish()
            } else {
                toast("金额不能为0")
                mUserInput = ""
            }
        }
        //小数点
        keyboard_dot.setOnClickListener {
            if (!mUserInput.contains(".")) {
                mUserInput += "."
            }
        }
        //输入类型
        keyboard_type.setOnClickListener {
            if (mType == RecordBean.TYPE_EXPENSE) {
                mType = RecordBean.TYPE_INCOME
                keyboard_type.setImageResource(R.drawable.ic_income)
            } else {
                mType = RecordBean.TYPE_EXPENSE
                keyboard_type.setImageResource(R.drawable.ic_expense)
            }
            mCategoryAdapter.changeType(mType)
            mCurrentCategory = mCategoryAdapter.getCurrentSelected()
        }
        //选中类型
        mCategoryAdapter.setOnCategoryClickListener(object : CategoryAdapter.OnCategoryClickListener {
            override fun onItemClick(category: String) {
                mCurrentCategory = category
                et_name?.setText(category)
            }
        })
    }

    override fun onClick(v: View) {
        val input = (v as Button).run {
            text.toString()
        }
        if (mUserInput.contains(".")) {
            if (mUserInput == ".") {
                mUserInput = "0."
            }
            val userInputSplitArray = mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (userInputSplitArray.size == 1 || (userInputSplitArray.size > 1 && userInputSplitArray[1].length < 2)) {
                mUserInput += input
            }
        } else {
            if (mUserInput != "0") {
                mUserInput += input
            } else if ((mUserInput == "0" && input != "0")) {
                mUserInput = input
            }
        }
        updateAmountText()
    }

    private fun userInputIsNotZero(): Boolean {
        return mUserInput.isNotEmpty() && mUserInput != "0" && mUserInput != "0." && mUserInput != "0.0" && mUserInput != "0.00"
    }

    @SuppressLint("SetTextI18n")
    private fun updateAmountText() {
        if (mUserInput.contains(".")) {
            val userInputSplitArray = mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            when {
                userInputSplitArray.size == 1 -> tv_amount?.text = "${mUserInput}00"
                userInputSplitArray[1].length == 1 -> tv_amount?.text = "${mUserInput}0"
                userInputSplitArray[1].length == 2 -> tv_amount?.text = mUserInput
            }
        } else {
            tv_amount?.text = if (mUserInput.isEmpty()) {
                "0.00"
            } else {
                "$mUserInput.00"
            }
        }
    }

}
