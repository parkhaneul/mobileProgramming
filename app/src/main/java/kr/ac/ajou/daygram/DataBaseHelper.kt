package kr.ac.ajou.daygram

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME : String = "SnapShots"
private const val DATABASE_VERSION : Int = 1

class DataBaseHelper(context : Context) : SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION){
    private val DATABASE_SNAPSHOT = "Snapshot"

    private val KEY_ID = "id"
    private val KEY_TITLE = "Title"
    private val KEY_DATE = "Date"
    private val KEY_MONTH = "Month"
    private val KEY_YEAR = "year"
    private val KEY_IMAGE = "Image"
    private val KEY_MAIN = "Main"

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_SNAPSHOT : String = "CREATE TABLE " + DATABASE_SNAPSHOT + " (" +
                KEY_ID + " integer primary key autoincrement, " +
                KEY_TITLE + " TITLE, " +
                KEY_DATE + " DATE, " +
                KEY_MONTH + " MONTH, " +
                KEY_YEAR + " YEAR, " +
                KEY_IMAGE + " IMAGE, " +
                KEY_MAIN + " MAIN);"
        db?.execSQL(CREATE_SNAPSHOT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var DROP_SNAPSHOT : String = "DROP TABLE IF EXISTS " + DATABASE_SNAPSHOT
        db?.execSQL(DROP_SNAPSHOT)
        onCreate(db)
    }

    fun add(snapshot : Snapshot){
        var db = this.writableDatabase
        var value = ContentValues()
        value.put("Title",snapshot.title)
        value.put("Date", snapshot.date)
        value.put("Month", snapshot.month)
        value.put("Image", snapshot.image)
        value.put("YEAR",snapshot.year)
        value.put("Main",snapshot.main)
        db.insert(DATABASE_SNAPSHOT,null,value)
        db.close()
    }

    fun getAll() : ArrayList<Snapshot>{
        var list = ArrayList<Snapshot>()

        var SELECT_ALL = "SELECT * FROM " + DATABASE_SNAPSHOT;
        var db = this.writableDatabase
        var cursor = db.rawQuery(SELECT_ALL,null)

        if(cursor.moveToFirst()){
            do{
                var snapshot = Snapshot()
                snapshot.id = cursor.getInt(0)
                snapshot.title = cursor.getString(1)
                snapshot.date = cursor.getString(2)
                snapshot.month = cursor.getString(3)
                snapshot.year = cursor.getString(4)
                snapshot.image = cursor.getInt(5)
                snapshot.main = cursor.getString(6)
                list.add(snapshot)
            }while (cursor.moveToNext())
        }

        return list
    }
}