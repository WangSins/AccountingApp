package me.sin.accountingapp.database

import android.content.ContentValues
import me.sin.accountingapp.base.BaseApplication
import java.util.*

/**l
 * Created by Sin on 2020/7/25
 */
object RecordDBDao {

    private var recordDBHelper: RecordDBHelper = RecordDBHelper(BaseApplication.context)

    val availableDate: LinkedList<String>
        get() {
            val dates = LinkedList<String>()
            val db = recordDBHelper.writableDatabase
            val sql = "select DISTINCT * from ${RecordDBHelper.TABLE_NAME} order by ${RecordDBHelper.COLUMN_DATE} asc"
            val cursor = db.rawQuery(sql, arrayOf())
            var date: String
            if (cursor.moveToFirst()) {
                do {
                    date = cursor.getString(cursor.getColumnIndex(RecordDBHelper.COLUMN_DATE))
                    if (!dates.contains(date)) {
                        dates.add(date)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            return dates
        }

    fun addRecord(bean: RecordBean) {
        val db = recordDBHelper.writableDatabase
        val values = ContentValues().apply {
            with(bean) {
                put(RecordDBHelper.COLUMN_ID, uuid)
                put(RecordDBHelper.COLUMN_TYPE, type)
                put(RecordDBHelper.COLUMN_CATEGORY, category)
                put(RecordDBHelper.COLUMN_REMARK, remark)
                put(RecordDBHelper.COLUMN_AMOUNT, amount)
                put(RecordDBHelper.COLUMN_DATE, date)
                put(RecordDBHelper.COLUMN_TIME, timeStamp)
            }
        }
        db.insert(RecordDBHelper.TABLE_NAME, null, values)
        values.clear()
    }

    fun removeRecord(uuid: String) {
        with(recordDBHelper.writableDatabase) {
            delete(RecordDBHelper.TABLE_NAME, "${RecordDBHelper.COLUMN_ID} = ?", arrayOf(uuid))
        }
    }

    fun editRecord(uuid: String, record: RecordBean) {
        removeRecord(uuid)
        record.uuid = uuid
        addRecord(record)
    }

    fun readRecords(dateStr: String): LinkedList<RecordBean> {
        val records = LinkedList<RecordBean>()
        val db = recordDBHelper.writableDatabase
        val sql = "select DISTINCT * from ${RecordDBHelper.TABLE_NAME} where ${RecordDBHelper.COLUMN_DATE} = ? order by ${RecordDBHelper.COLUMN_TIME} asc"
        val cursor = db.rawQuery(sql, arrayOf(dateStr))
        var record: RecordBean
        if (cursor.moveToFirst()) {
            do {
                record = RecordBean().apply {
                    uuid = cursor.getString(cursor.getColumnIndex(RecordDBHelper.COLUMN_ID))
                    type = cursor.getInt(cursor.getColumnIndex(RecordDBHelper.COLUMN_TYPE))
                    category = cursor.getString(cursor.getColumnIndex(RecordDBHelper.COLUMN_CATEGORY))
                    remark = cursor.getString(cursor.getColumnIndex(RecordDBHelper.COLUMN_REMARK))
                    amount = cursor.getDouble(cursor.getColumnIndex(RecordDBHelper.COLUMN_AMOUNT))
                    date = cursor.getString(cursor.getColumnIndex(RecordDBHelper.COLUMN_DATE))
                    timeStamp = cursor.getLong(cursor.getColumnIndex(RecordDBHelper.COLUMN_TIME))
                }
                records.add(record)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }

}