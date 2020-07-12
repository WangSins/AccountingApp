package me.sin.accountingapp.activity

import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.include_soft_keyboard.*
import me.sin.accountingapp.R
import me.sin.accountingapp.adapters.CategoryAdapter
import me.sin.accountingapp.base.BaseActivity
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.GlobalUtil

class AddRecordActivity : BaseActivity(), View.OnClickListener {

    private var mUserInput = ""
    private lateinit var mCategoryAdapter: CategoryAdapter
    private var mCurrentCategory: String? = "全部"
    private var mType: RecordBean.RecordType = RecordBean.RecordType.RECORD_TYPE_EXPENSE
    private val mRemark = mCurrentCategory
    private var mRecord = RecordBean()
    private var inEdit = false

    override fun getLayoutResId(): Int = R.layout.activity_add_record

    override fun initData() {
        et_name?.setText(mRemark)
        mCategoryAdapter = CategoryAdapter()
        rv_record?.adapter = mCategoryAdapter
        rv_record?.layoutManager = GridLayoutManager(this, 4)

        val recordExtra = intent.getSerializableExtra("record")
        if (recordExtra != null) {
            inEdit = true
            this.mRecord = recordExtra as RecordBean
        }
    }

    override fun initEvent() {
        handleBackspace()
        handleDone()
        handleDot()
        handleTypeChange()
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
        mCategoryAdapter.setOnCategoryClickListener(object : CategoryAdapter.OnCategoryClickListener {
            override fun onClick(category: String?) {
                mCurrentCategory = category
                et_name?.setText(category)
            }
        })
    }

    private fun handleDot() {
        keyboard_dot.setOnClickListener {
            if (!mUserInput.contains(".")) {
                mUserInput += "."
            }
        }
    }

    private fun handleTypeChange() {
        keyboard_type.setOnClickListener {
            if (mType == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                mType = RecordBean.RecordType.RECORD_TYPE_INCOME
                keyboard_type.setImageResource(R.drawable.baseline_attach_money_24)
            } else {
                mType = RecordBean.RecordType.RECORD_TYPE_EXPENSE
                keyboard_type.setImageResource(R.drawable.baseline_money_off_24)
            }
            mCategoryAdapter.changeType(mType)
            mCurrentCategory = mCategoryAdapter.currentSelected
        }
    }

    private fun handleBackspace() {
        keyboard_backspace.setOnClickListener {
            if (mUserInput.isNotEmpty()) {
                mUserInput = mUserInput.substring(0, mUserInput.length - 1)
            }
            if (mUserInput.isNotEmpty() && mUserInput[mUserInput.length - 1] == '.') {
                mUserInput = mUserInput.substring(0, mUserInput.length - 1)
            }
            updateAmountText()
        }
    }

    private fun handleDone() {
        keyboard_done.setOnClickListener {
            if (mUserInput.isNotEmpty()) {
                mRecord.amount = java.lang.Double.valueOf(mUserInput)
                if (mType == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                    mRecord.setType(1)
                } else {
                    mRecord.setType(2)
                }
                mRecord.category = mCategoryAdapter.currentSelected
                mRecord.remark = et_name.text.toString()

                if (inEdit) {
                    GlobalUtil.instance.databaseHelper.editRecord(mRecord.uuid, mRecord)
                } else {
                    GlobalUtil.instance.databaseHelper.addRecord(mRecord)
                }
                finish()
            } else {
                Toast.makeText(applicationContext, "金额不能为0", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View) {
        val button = v as Button
        val input = button.text.toString()
        if (mUserInput.contains(".")) {
            if (mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1 || mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length < 2) {
                mUserInput += input
            }
        } else {
            mUserInput += input
        }
        updateAmountText()
    }

    private fun updateAmountText() {
        if (mUserInput.contains(".")) {
            when {
                mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1 -> tv_amount?.text = mUserInput + "00"
                mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length == 1 -> tv_amount?.text = mUserInput + "0"
                mUserInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length == 2 -> tv_amount?.text = mUserInput
            }
        } else {
            tv_amount?.text = if (mUserInput == "") {
                "0.00"
            } else {
                "$mUserInput.00"
            }
        }
    }

}
