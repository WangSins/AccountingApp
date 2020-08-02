package me.sin.accountingapp.activity

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.robinhood.ticker.TickerUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_content_main.*
import me.sin.accountingapp.R
import me.sin.accountingapp.adapter.MainViewPagerAdapter
import me.sin.accountingapp.base.BaseActivity
import me.sin.accountingapp.constant.Constant
import me.sin.accountingapp.util.AnimatorUtil
import me.sin.accountingapp.util.DateUtil

class MainActivity : BaseActivity() {

    private lateinit var mPagerAdapter: MainViewPagerAdapter
    private var mCurrentPagerPosition = 0
    private var mIntentFilter = IntentFilter().apply {
        addAction(Constant.ACTION_UPDATE_HEADER)
        addAction(Constant.ACTION_RELOAD)
    }
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Constant.ACTION_UPDATE_HEADER -> updateHeader()
                Constant.ACTION_RELOAD -> {
                    mPagerAdapter.update(DateUtil.currentDates)
                    updateHeader()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.calendar -> {
                val currentSelectedDate = mPagerAdapter.getDateStr(mCurrentPagerPosition)
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val date = DateUtil.getDateStr(year, month + 1, dayOfMonth).toString()
                    if (!mPagerAdapter.getDates().contains(date)) {
                        toast("当日没有记录")
                        return@OnDateSetListener
                    }
                    mPagerAdapter.getDates().forEachIndexed { index, s ->
                        if (s == date) {
                            view_pager?.currentItem = index
                        }
                    }
                }, currentSelectedDate.substring(0, 4).toInt(), currentSelectedDate.substring(5, 7).toInt() - 1, currentSelectedDate.substring(8, 10).toInt()).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initActionBar() {
        supportActionBar?.elevation = 0f
    }

    override fun initData() {
        tv_amount?.setCharacterLists(TickerUtils.provideNumberList())
        mPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        mPagerAdapter.update(DateUtil.currentDates)
        view_pager?.let {
            it.adapter = mPagerAdapter
            it.currentItem = mPagerAdapter.getLastPosition()
        }
        mCurrentPagerPosition = mPagerAdapter.getLastPosition()
        updateHeader()
    }

    override fun initEvent() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mIntentFilter)
        fab_add_record?.setOnClickListener {
            startActivityForResult(AddRecordActivity::class.java, Bundle().apply {
                putString(Constant.KEY_DATE, mPagerAdapter.getDateStr(mCurrentPagerPosition))
            }, 1)
        }
        view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                if (mCurrentPagerPosition < p0) {
                    AnimatorUtil.rotationObjectAnimation(fab_add_record, 0f, 180f, 350)
                } else {
                    AnimatorUtil.rotationObjectAnimation(fab_add_record, 180f, 0f, 350)
                }
                mCurrentPagerPosition = p0
                updateHeader()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val date = data?.getStringExtra(Constant.KEY_DATE)
        if (date != null) {
            mPagerAdapter.reload(DateUtil.currentDates, date)
            mPagerAdapter.getDates().forEachIndexed { index, s ->
                if (s == date) {
                    view_pager?.currentItem = index
                }
            }
        } else {
            mPagerAdapter.reload(mCurrentPagerPosition)
        }
        updateHeader()
    }

    private fun updateHeader() {
        mPagerAdapter.run {
            tv_amount?.text = getTotalCost(mCurrentPagerPosition).toString()
            tv_date?.text = DateUtil.getWeekDay(getDateStr(mCurrentPagerPosition))
        }
    }

    override fun release() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
    }

}
