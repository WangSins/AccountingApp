package me.sin.accountingapp.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.sin.accountingapp.fragment.MainFragment
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil
import java.util.*

class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var mFragments = LinkedList<MainFragment>()
    private var mDates: LinkedList<String>
    var currentLatsIndex: Int

    init {
        mDates = LinkedList()
        initFragments()
        currentLatsIndex = mFragments.size - 1
    }

    private fun initFragments() {
        mDates = GlobalUtil.databaseHelper.availableDate

        if (!mDates.contains(DateUtil.formattedDate)) {
            mDates.addLast(DateUtil.formattedDate)
        }

        for (date in mDates) {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString("dateKey", date)
            fragment.arguments = args
            mFragments.add(fragment)
        }
    }

    fun reload() {
        mFragments[currentLatsIndex].reload()
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    fun getDateStr(index: Int): String = mDates[index]

    fun getTotalCost(index: Int): Int = mFragments[index].getTotalCost()
}
