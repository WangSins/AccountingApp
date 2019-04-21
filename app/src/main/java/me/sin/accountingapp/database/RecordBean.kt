package me.sin.accountingapp.database

import java.io.Serializable
import java.util.UUID

import me.sin.accountingapp.utils.DateUtil

class RecordBean : Serializable {

    var amount: Double = 0.0
    private lateinit var type: RecordType
    lateinit var category: String
    var remark: String? = null
    var date: String = DateUtil.formattedDate

    var timeStamp: Long = System.currentTimeMillis()
    var uuid: String = UUID.randomUUID().toString()

    enum class RecordType {
        RECORD_TYPE_EXPENSE, RECORD_TYPE_INCOME
    }

    fun getType(): Int {
        return if (this.type == RecordType.RECORD_TYPE_EXPENSE) {
            1
        } else {
            2
        }
    }

    fun setType(type: Int) {
        if (type == 1) {
            this.type = RecordType.RECORD_TYPE_EXPENSE
        } else {
            this.type = RecordType.RECORD_TYPE_INCOME
        }
    }
}
