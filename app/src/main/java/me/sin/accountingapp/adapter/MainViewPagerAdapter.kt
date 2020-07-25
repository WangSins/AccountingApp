package me.sin.accountingapp.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.sin.accountingapp.constant.Constant
import me.sin.accountingapp.database.RecordDBDao
import me.sin.accountingapp.fragment.MainFragment
import me.sin.accountingapp.util.DateUtil
import java.util.*

class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var mFragments = LinkedList<MainFragment>()
    private var mDates: LinkedList<String> = LinkedList()
    var currentLatsIndex: Int

    init {
        mDates = RecordDBDao.availableDate
        if (!mDates.contains(DateUtil.formattedDate)) {
            mDates.addLast(DateUtil.formattedDate)
        }
        for (date in mDates) {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(Constant.KEY_DATE, date)
            fragment.arguments = args
            mFragments.add(fragment)
        }
        currentLatsIndex = mFragments.size - 1
    }

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    fun reload(index: Int) {
        mFragments[index].refresh()
        notifyDataSetChanged()
    }

    fun getDateStr(index: Int): String = mDates[index]

    fun getTotalCost(index: Int): Int = mFragments[index].getTotalCost()
}
