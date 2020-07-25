package me.sin.accountingapp.database

import me.sin.accountingapp.util.DateUtil
import java.io.Serializable
import java.util.*

class RecordBean : Serializable {

    var amount: Double = 0.0
    var remark: String = ""
    var date: String = DateUtil.formattedDate
    var timeStamp: Long = System.currentTimeMillis()
    var uuid: String = UUID.randomUUID().toString()
    var type: Int = 0
    var category: String = ""

    companion object {
        const val TYPE_EXPENSE = 0
        const val TYPE_INCOME = 1
    }
}
