package me.sin.accountingapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

    override fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_RECORD_DB)

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun addRecord(bean: RecordBean) {
        val db = this.writableDatabase
        val values = ContentValues().also {
            it.put("uuid", bean.uuid)
            it.put("type", bean.getType())
            it.put("category", bean.category)
            it.put("remark", bean.remark)
            it.put("amount", bean.amount)
            it.put("date", bean.date)
            it.put("time", bean.timeStamp)
        }
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

                val record = RecordBean().also {
                    it.uuid = uuid
                    it.setType(type)
                    it.category = category
                    it.remark = remark
                    it.amount = amount
                    it.date = date
                    it.timeStamp = timeStamp
                }

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
