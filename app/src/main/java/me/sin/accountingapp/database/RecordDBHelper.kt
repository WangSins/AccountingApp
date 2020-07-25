package me.sin.accountingapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecordDBHelper : SQLiteOpenHelper {

    constructor(context: Context?) : this(context, DB_NAME, null, DB_VERSION) {}

    constructor(context: Context?, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int)
            : super(context, name, factory, version) {
    }

    companion object {

        const val DB_NAME = "Accounting"
        const val DB_VERSION = 1
        const val TABLE_NAME = "Record"
        const val COLUMN_ID = "uuid"
        const val COLUMN_TYPE = "type"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_REMARK = "remark"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_TIME = "time"
        const val COLUMN_DATE = "date"
        private const val CREATE_RECORD_DB = (
                "create table $TABLE_NAME " +
                        "("
                        + "id integer primary key autoincrement, "
                        + "$COLUMN_ID text, "
                        + "$COLUMN_TYPE integer, "
                        + "$COLUMN_CATEGORY text, "
                        + "$COLUMN_REMARK text, "
                        + "$COLUMN_AMOUNT double, "
                        + "$COLUMN_TIME integer, "
                        + "$COLUMN_DATE date " +
                        ")"
                )
    }

    override fun onCreate(db: SQLiteDatabase) = db.execSQL(CREATE_RECORD_DB)

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}
