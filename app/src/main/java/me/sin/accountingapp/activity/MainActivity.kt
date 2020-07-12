package me.sin.accountingapp.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import com.robinhood.ticker.TickerUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_content_main.*
import me.sin.accountingapp.R
import me.sin.accountingapp.adapters.MainViewPagerAdapter
import me.sin.accountingapp.base.BaseActivity
import me.sin.accountingapp.utils.AnimatorUtil
import me.sin.accountingapp.utils.DateUtil

class MainActivity : BaseActivity() {

    private lateinit var mPagerAdapter: MainViewPagerAdapter
    private var mCurrentPagerPosition = 0
    private var mFabShow = true
    private val mIntentFilter = IntentFilter("updateHeader");
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
        view_pager?.adapter = mPagerAdapter
        view_pager?.currentItem = mPagerAdapter.currentLatsIndex
        mCurrentPagerPosition = mPagerAdapter.currentLatsIndex
        updateHeader()
    }

    override fun initEvent() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, mIntentFilter)
        fab_add_record.setOnClickListener {
            val intent = Intent(this@MainActivity, AddRecordActivity::class.java)
            startActivityForResult(intent, 1)
        }
        view_pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                mCurrentPagerPosition = p0
                if (mCurrentPagerPosition < mPagerAdapter.currentLatsIndex) {
                    if (mFabShow) {
                        AnimatorUtil.scaleObjectAnimation(fab_add_record, 1f, 0f, 300)
                        fab_add_record.isEnabled = false
                        mFabShow = false
                    }
                } else {
                    if (!mFabShow) {
                        AnimatorUtil.scaleObjectAnimation(fab_add_record, 0f, 1f, 300)
                        fab_add_record.isEnabled = true
                        mFabShow = true
                    }
                }
                updateHeader()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPagerAdapter.reload()
        updateHeader()
    }

    private fun updateHeader() {
        val amount = mPagerAdapter.getTotalCost(mCurrentPagerPosition).toString()
        tv_amount?.text = amount
        val date = mPagerAdapter.getDateStr(mCurrentPagerPosition)
        tv_date?.text = DateUtil.getWeekDay(date)
    }

    override fun release() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
    }

}
