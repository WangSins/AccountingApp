package me.sin.accountingapp.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import com.wsy.accountingapp.R
import me.sin.accountingapp.adapters.MainViewPagerAdapter
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MainViewPagerAdapter
    private lateinit var tvAmount: TickerView
    private lateinit var tvDate: TextView
    private var currentPagerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGlobalUtil()
        initActionBar()
        initView()
        initListener()
        updateHeader()
    }

    private fun initGlobalUtil() {
        GlobalUtil.instance.context = applicationContext
        GlobalUtil.instance.mainActivity = this
    }

    private fun initActionBar() {
        supportActionBar?.elevation = 0f
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        tvAmount = findViewById(R.id.tv_amount)
        tvAmount.setCharacterLists(TickerUtils.provideNumberList())
        tvDate = findViewById(R.id.tv_date)

        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        pagerAdapter.notifyDataSetChanged()
        viewPager.adapter = pagerAdapter
        viewPager.setOnPageChangeListener(this)
        viewPager.currentItem = pagerAdapter.latsIndex
    }

    private fun initListener() {
        findViewById<View>(R.id.fab).setOnClickListener {
            val intent = Intent(this@MainActivity, AddRecordActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pagerAdapter.reload()
        updateHeader()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        currentPagerPosition = position
        updateHeader()
    }

    fun updateHeader() {
        val amount = pagerAdapter.getTotalCost(currentPagerPosition).toString()
        tvAmount.text = amount
        val date = pagerAdapter.getDateStr(currentPagerPosition)
        tvDate.text = DateUtil.getWeekDay(date)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
