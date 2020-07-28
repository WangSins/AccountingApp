package me.sin.accountingapp.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import me.sin.accountingapp.constant.Constant
import me.sin.accountingapp.fragment.MainFragment
import java.util.*

class MainViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var mFragments = LinkedList<MainFragment>()
    private var mDates: LinkedList<String> = LinkedList()

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    /**
     * 根据索引刷新页面
     */
    fun reload(index: Int) {
        mFragments[index].refresh()
    }

    /**
     * 根据日期刷新页面，或者重载
     */
    fun reload(dates: LinkedList<String>, data: String) {
        if (mDates.contains(data)) {
            mFragments[mDates.lastIndexOf(data)].refresh()
        } else {
            update(dates)
        }
    }

    /**
     * 更新重载
     */
    fun update(dates: LinkedList<String>) {
        //Date
        mDates.clear()
        mDates.addAll(dates)
        //Fragment
        mFragments.clear()
        var fragment: MainFragment
        var args: Bundle
        for (date in dates) {
            fragment = MainFragment()
            args = Bundle()
            args.putString(Constant.KEY_DATE, date)
            fragment.arguments = args
            mFragments.add(fragment)
        }
        notifyDataSetChanged()
    }

    /**
     * 获取最后一个索引
     */
    fun getLastPosition(): Int = mFragments.size - 1

    /**
     * 获取日期集合
     */
    fun getDates(): LinkedList<String> = mDates

    /**
     * 根据索引获取当前日期
     */
    fun getDateStr(index: Int): String = mDates[index]

    /**
     * 根据索引获取总金额
     */
    fun getTotalCost(index: Int): Int = mFragments[index].getTotalCost()
}
