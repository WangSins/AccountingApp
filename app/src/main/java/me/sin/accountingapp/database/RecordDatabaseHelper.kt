package me.sin.accountingapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.util.LinkedList

class RecordDatabaseHelper(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    val availableDate: LinkedList<String>
        get() {
            val dates = LinkedList<String>()
            val db = this.writableDatabase
            val cursor = db.rawQuery("select DISTINCT * from Record order by date asc", arrayOf())
            if (cursor.moveToFirst()) {
                do {
                    val date = cursor.getString(cursor.getColumnIndex("date"))
                    if (!dates.contains(date)) {
                        dates.add(date)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            return dates
        }

    override fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_RECORD_DB)

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun addRecord(bean: RecordBean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            with(bean) {
                put("uuid", uuid)
                put("type", getType())
                put("category", category)
                put("remark", remark)
                put("amount", amount)
                put("date", date)
                put("time", timeStamp)
            }
        }
        db.insert(DB_NAME, null, values)
        values.clear()
    }

    fun removeRecord(uuid: String) {
        with(this.writableDatabase) {
            delete(DB_NAME, "uuid = ?", arrayOf(uuid))
        }
    }

    fun editRecord(uuid: String, record: RecordBean) {
        removeRecord(uuid)
        record.uuid = uuid
        addRecord(record)
    }

    fun readRecords(dateStr: String): LinkedList<RecordBean> {
        val records = LinkedList<RecordBean>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("select DISTINCT * from Record where date = ? order by time asc", arrayOf(dateStr))
        if (cursor.moveToFirst()) {
            do {
                records.add(RecordBean().apply {
                    uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                    setType(cursor.getInt(cursor.getColumnIndex("type")))
                    category = cursor.getString(cursor.getColumnIndex("category"))
                    remark = cursor.getString(cursor.getColumnIndex("remark"))
                    amount = cursor.getDouble(cursor.getColumnIndex("amount"))
                    date = cursor.getString(cursor.getColumnIndex("date"))
                    timeStamp = cursor.getLong(cursor.getColumnIndex("time"))
                })

            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }

    companion object {
        const val DB_NAME = "Record"
        private const val CREATE_RECORD_DB = ("create table Record ("
                + "id integer primary key autoincrement, "
                + "uuid text, "
                + "type integer, "
                + "category, "
                + "remark text, "
                + "amount double, "
                + "time integer, "
                + "date date )")
    }
}
