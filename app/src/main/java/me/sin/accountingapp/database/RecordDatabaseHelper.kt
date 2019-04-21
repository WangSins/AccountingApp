package me.sin.accountingapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import java.util.LinkedList

class RecordDatabaseHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    val avaliableDate: LinkedList<String>
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

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_RECORD_DB)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun addRecord(bean: RecordBean) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("uuid", bean.uuid)
        values.put("type", bean.getType())
        values.put("category", bean.category)
        values.put("remark", bean.remark)
        values.put("amount", bean.amount)
        values.put("date", bean.date)
        values.put("time", bean.timeStamp)
        db.insert(DB_NAME, null, values)
        values.clear()
    }

    fun removeRecord(uuid: String) {
        val db = this.writableDatabase
        db.delete(DB_NAME, "uuid = ?", arrayOf(uuid))
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
                val uuid = cursor.getString(cursor.getColumnIndex("uuid"))
                val type = cursor.getInt(cursor.getColumnIndex("type"))
                val category = cursor.getString(cursor.getColumnIndex("category"))
                val remark = cursor.getString(cursor.getColumnIndex("remark"))
                val amount = cursor.getDouble(cursor.getColumnIndex("amount"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val timeStamp = cursor.getLong(cursor.getColumnIndex("time"))

                val record = RecordBean()
                record.uuid = uuid
                record.setType(type)
                record.category = category
                record.remark = remark
                record.amount = amount
                record.date = date
                record.timeStamp = timeStamp

                records.add(record)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }

    companion object {

        val DB_NAME = "Record"

        private val CREATE_RECORD_DB = ("create table Record ("
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
