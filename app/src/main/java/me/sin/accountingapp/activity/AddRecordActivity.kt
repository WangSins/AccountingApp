package me.sin.accountingapp.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import com.wsy.accountingapp.R

import me.sin.accountingapp.adapters.CategoryRecyclerAdapter
import me.sin.accountingapp.database.RecordBean
import me.sin.accountingapp.utils.GlobalUtil

class AddRecordActivity : AppCompatActivity(), View.OnClickListener, CategoryRecyclerAdapter.OnCategoryClickListener {

    private lateinit var rtName: EditText
    private lateinit var tvAmount: TextView
    private var userInput = ""

    private lateinit var rvRecord: RecyclerView
    private lateinit var categoryRecyclerAdapter: CategoryRecyclerAdapter

    private var category: String? = "全部"
    private var type: RecordBean.RecordType = RecordBean.RecordType.RECORD_TYPE_EXPENSE
    private val remark = category

    private var record = RecordBean()

    private var inEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        initView()
        handleBackspace()
        handleDone()
        handleDot()
        handleTypeChange()
        initListener()
    }

    private fun initActionBar() {
        supportActionBar?.elevation = 0f
    }

    private fun initView() {
        setContentView(R.layout.activity_add_record)
        tvAmount = findViewById(R.id.tv_amount)
        rtName = findViewById(R.id.et_name)
        rtName.setText(remark)
        rvRecord = findViewById(R.id.rv_record)
        categoryRecyclerAdapter = CategoryRecyclerAdapter(this)
        rvRecord.adapter = categoryRecyclerAdapter
        val gridLayoutManager = GridLayoutManager(this, 4)
        rvRecord.layoutManager = gridLayoutManager
        categoryRecyclerAdapter.notifyDataSetChanged()
        val recordExtra = intent.getSerializableExtra("record")
        if (recordExtra != null) {
            inEdit = true
            this.record = recordExtra as RecordBean
        }
    }

    private fun initListener() {
        findViewById<View>(R.id.keyboard_one).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_two).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_three).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_four).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_five).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_six).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_seven).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_eight).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_nine).setOnClickListener(this)
        findViewById<View>(R.id.keyboard_zero).setOnClickListener(this)
        categoryRecyclerAdapter.setOnCategoryClickListener(this)
    }

    private fun handleDot() {
        findViewById<View>(R.id.keyboard_dot).setOnClickListener {
            if (!userInput.contains(".")) {
                userInput += "."
            }
        }
    }

    private fun handleTypeChange() {
        findViewById<View>(R.id.keyboard_type).setOnClickListener {
            val button = findViewById<ImageButton>(R.id.keyboard_type)
            if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                type = RecordBean.RecordType.RECORD_TYPE_INCOME
                button.setImageResource(R.drawable.baseline_attach_money_24)
            } else {
                type = RecordBean.RecordType.RECORD_TYPE_EXPENSE
                button.setImageResource(R.drawable.baseline_money_off_24)
            }
            categoryRecyclerAdapter.changeType(type)
            category = categoryRecyclerAdapter.selected
        }
    }

    private fun handleBackspace() {
        findViewById<View>(R.id.keyboard_backspace).setOnClickListener {
            if (userInput.isNotEmpty()) {
                userInput = userInput.substring(0, userInput.length - 1)
            }
            if (userInput.isNotEmpty() && userInput[userInput.length - 1] == '.') {
                userInput = userInput.substring(0, userInput.length - 1)
            }
            updateAmountText()
        }
    }

    private fun handleDone() {
        findViewById<View>(R.id.keyboard_done).setOnClickListener {
            if (userInput != "") {
                val amount = java.lang.Double.valueOf(userInput)
                record.amount = amount
                if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE) {
                    record.setType(1)
                } else {
                    record.setType(2)
                }
                record.category = categoryRecyclerAdapter.selected
                record.remark = rtName.text.toString()

                if (inEdit) {
                    GlobalUtil.instance.databaseHelper.editRecord(record.uuid, record)
                } else {
                    GlobalUtil.instance.databaseHelper.addRecord(record)
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
        if (userInput.contains(".")) {
            if (userInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1 || userInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length < 2) {
                userInput += input
            }
        } else {
            userInput += input
        }
        updateAmountText()

    }

    private fun updateAmountText() {
        if (userInput.contains(".")) {
            if (userInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size == 1) {
                tvAmount.text = userInput + "00"
            } else if (userInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length == 1) {
                tvAmount.text = userInput + "0"
            } else if (userInput.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length == 2) {
                tvAmount.text = userInput
            }
        } else {
            if (userInput == "") {
                tvAmount.text = "0.00"
            } else {
                tvAmount.text = "$userInput.00"
            }
        }
    }

    override fun onClick(category: String?) {
        this.category = category
        rtName.setText(category)
    }

}
