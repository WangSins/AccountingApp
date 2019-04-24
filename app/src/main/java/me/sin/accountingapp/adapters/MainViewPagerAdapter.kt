package me.sin.accountingapp.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.sin.accountingapp.fragment.MainFragment
import me.sin.accountingapp.utils.DateUtil
import me.sin.accountingapp.utils.GlobalUtil
import java.util.*

class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragments = LinkedList<MainFragment>()

    private var dates = LinkedList<String>()

    val latsIndex: Int
        get() = fragments.size - 1

    init {
        initFragments()
    }

    private fun initFragments() {
        dates = GlobalUtil.instance.databaseHelper.avaliableDate

        if (!dates.contains(DateUtil.formattedDate)) {
            dates.addLast(DateUtil.formattedDate)
        }

        for (date in dates) {
            val fragment = MainFragment.newInstance(date)
            fragments.add(fragment)
        }
    }

    fun reload() {
        for (fragment in fragments) {
            fragment.reload()
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    fun getDateStr(index: Int): String = dates[index]

    fun getTotalCost(index: Int): Int = fragments[index].getTotalCost()
}
