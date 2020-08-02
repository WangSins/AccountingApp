package me.sin.accountingapp.util

import me.sin.accountingapp.database.RecordDBDao
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 当前日期
     */
    val currentDate: String
        get() {
            return with(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) {
                format(Date())
            }
        }

    /**
     * 当前所有日期
     */
    val currentDates: LinkedList<String>
        get() {
            val list = RecordDBDao.availableDate
            if (!list.contains(currentDate)) {
                list.addLast(currentDate)
            }
            return list
        }

    /**
     * 获取当前日期
     */
    fun getDateStr(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
    }

    /**
     * 年月日获取格式化日期
     */
    fun getDateStr(year: Int, month: Int, dayOfMonth: Int): String {
        val yearStr = year.toString()
        var monthStr = month.toString()
        var dayStr = dayOfMonth.toString()
        if (monthStr.length == 1) {
            monthStr = "0$monthStr"
        }
        if (dayStr.length == 1) {
            dayStr = "0$dayStr"
        }
        return "$yearStr-$monthStr-$dayStr"
    }

    /**
     * 判断日期是否是今天以后
     */
    fun afterToday(year: Int, month: Int, day: Int): Boolean {
        return getDateStr() < getDateStr(year, month, day)
    }

    /**
     * 格式化时间戳 HH:mm
     */
    fun getFormattedTime(timeStamp: Long): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timeStamp))
    }

    fun getFormattedStr(date: String, sourceFormat: String, targetFormat: String): String {
        return SimpleDateFormat(targetFormat).format(SimpleDateFormat(sourceFormat).parse(date))
    }

    /**
     * 字符串到日期类 yyyy-MM-dd
     */
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

    /**
     * 获取周几
     */
    fun getWeekDay(date: String): String {
        return with(arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")) {
            Calendar.getInstance().apply {
                time = strToDate(date)
            }.let {
                get(it.get(Calendar.DAY_OF_WEEK) - 1)
            }
        }
    }

    /**
     * 获取月份
     */
    fun getMonth(date: String): String {
        return with(arrayOf("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月")) {
            Calendar.getInstance().apply {
                time = strToDate(date)
            }.let {
                get(it.get(Calendar.MONTH)) + " " + it.get(Calendar.DAY_OF_MONTH).toString()
            }
        }
    }
}
