package me.sin.accountingapp.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
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
    private val mIntentFilter = IntentFilter(Constant.ACTION_UPDATE_HEADER);
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateHeader()
        }
    }

    override fun initActionBar() {
        supportActionBar?.elevation = 0f
    }

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun initData() {
        tv_amount?.setCharacterLists(TickerUtils.provideNumberList())
        mPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        view_pager?.let {
            it.adapter = mPagerAdapter
            it.currentItem = mPagerAdapter.currentLatsIndex
        }
        mCurrentPagerPosition = mPagerAdapter.currentLatsIndex
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
        mPagerAdapter.reload(mCurrentPagerPosition)
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
