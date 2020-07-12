package me.sin.accountingapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateUtil {

    //2017-06-15
    val formattedDate: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            return formatter.format(Date())
        }

    //unix time -> 11:11
    fun getFormattedTime(timeStamp: Long): String {
        val formatter = SimpleDateFormat("HH:mm")
        return formatter.format(Date(timeStamp))
    }

    private fun strToDate(date: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
            return format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    fun getWeekDay(date: String): String {
        val weekdays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = strToDate(date)
        val index = calendar.get(Calendar.DAY_OF_WEEK) - 1
        return weekdays[index]
    }

    fun getDateTitle(date: String): String {
        val months = arrayOf("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月")
        val calendar = Calendar.getInstance()
        calendar.time = strToDate(date)
        val monthIndex = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return months[monthIndex] + " " + day.toString()
    }
}
