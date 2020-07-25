package me.sin.accountingapp.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    //2017-06-15
    val formattedDate: String
        get() {
            return with(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) {
                format(Date())
            }
        }

    //unix time -> 11:11
    fun getFormattedTime(timeStamp: Long): String {
        return with(SimpleDateFormat("HH:mm", Locale.getDefault())) {
            format(Date(timeStamp))
        }
    }

    private fun strToDate(date: String): Date {
        try {
            return with(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) {
                parse(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    fun getWeekDay(date: String): String {
        return with(arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")) {
            Calendar.getInstance().apply {
                time = strToDate(date)
            }.let {
                get(it.get(Calendar.DAY_OF_WEEK) - 1)
            }
        }
    }

    fun getDateTitle(date: String): String {
        return with(arrayOf("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月")) {
            Calendar.getInstance().apply {
                time = strToDate(date)
            }.let {
                get(it.get(Calendar.MONTH)) + " " + it.get(Calendar.DAY_OF_MONTH).toString()
            }
        }
    }
}
