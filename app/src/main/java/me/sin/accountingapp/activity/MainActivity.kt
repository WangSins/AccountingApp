package me.sin.accountingapp.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import me.sin.accountingapp.R
import me.sin.accountingapp.adapters.MainViewPagerAdapter
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MainViewPagerAdapter
    private lateinit var tvAmount: TickerView
    private lateinit var tvDate: TextView
    private var currentPagerPosition = 0
    private var fabShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGlobalUtil()
        initActionBar()
        initView()
        initData()
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
        viewPager = findViewById(R.id.view_pager)
        tvDate = findViewById(R.id.tv_date)
    }

    private fun initData() {
        tvAmount.setCharacterLists(TickerUtils.provideNumberList())
        pagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = pagerAdapter.latsIndex
        currentPagerPosition = pagerAdapter.latsIndex
    }

    private fun initListener() {
        findViewById<View>(R.id.fab_add_record).setOnClickListener {
            val intent = Intent(this@MainActivity, AddRecordActivity::class.java)
            startActivityForResult(intent, 1)
        }
        viewPager.addOnPageChangeListener(this)
        pagerAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pagerAdapter.reload()
        updateHeader()
    }

    override fun onPageSelected(position: Int) {
        currentPagerPosition = position
        if (currentPagerPosition < pagerAdapter.latsIndex) {
            if (fabShow) {
                scaleObjectAnimation(findViewById<View>(R.id.fab_add_record), 1f, 0f, 300)
                findViewById<View>(R.id.fab_add_record).isEnabled = false
                fabShow = false
            }
        } else {
            if (!fabShow) {
                scaleObjectAnimation(findViewById<View>(R.id.fab_add_record), 0f, 1f, 300)
                findViewById<View>(R.id.fab_add_record).isEnabled = true
                fabShow = true
            }

        }
        updateHeader()
    }

    fun updateHeader() {
        val amount = pagerAdapter.getTotalCost(currentPagerPosition).toString()
        tvAmount.text = amount
        val date = pagerAdapter.getDateStr(currentPagerPosition)
        tvDate.text = DateUtil.getWeekDay(date)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    private fun scaleObjectAnimation(targetView: View, startSize: Float, endSize: Float, duration: Long) {
        val scaleY = ObjectAnimator.ofFloat(targetView, "scaleY", startSize, endSize)
        val scaleX = ObjectAnimator.ofFloat(targetView, "scaleX", startSize, endSize)

        val set = AnimatorSet()
        set.play(scaleY).with(scaleX)
        set.duration = duration
        set.start()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
