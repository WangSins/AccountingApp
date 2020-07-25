package me.sin.accountingapp.database

import java.io.Serializable

class RecordBean : Serializable {

    var uuid: String = ""
    var type: Int = 0
    var category: String = ""
    var remark: String = ""
    var amount: Double = 0.0
    var timeStamp: Long = 0
    var date: String = ""

    companion object {
        const val TYPE_EXPENSE = 0
        const val TYPE_INCOME = 1
    }
}
