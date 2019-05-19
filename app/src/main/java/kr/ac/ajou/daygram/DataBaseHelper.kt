package kr.ac.ajou.daygram

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.FieldPosition

private const val DATABASE_NAME : String = "SnapShots"
private const val DATABASE_VERSION : Int = 2

class DataBaseHelper(context : Context) : SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION){
    private val DATABASE_SNAPSHOT = "Snapshot"

    private val KEY_ID = "id"
    private val KEY_TITLE = "Title"
    private val KEY_TIME = "Time"
    private val KEY_IMAGE = "Image"
    private val KEY_MAIN = "Main"

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_SNAPSHOT : String = "CREATE TABLE " + DATABASE_SNAPSHOT + " (" +
                KEY_ID + " integer primary key autoincrement, " +
                KEY_TITLE + " TITLE, " +
                KEY_TIME + " TIME, " +
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
        value.put("Time",snapshot.writeTime)
        value.put("Image", snapshot.imageSource)
        value.put("Main",snapshot.content)
        db.insert(DATABASE_SNAPSHOT,null,value)
        db.close()
    }

    fun remove(snapshot: Snapshot, time: Long){
        var db = this.writableDatabase
        db.delete(DATABASE_SNAPSHOT, "Title=?", arrayOf(time.toString()))

    }

    fun getAll() : ArrayList<Snapshot>{
        var list = ArrayList<Snapshot>()

        var SELECT_ALL = "SELECT * FROM " + DATABASE_SNAPSHOT;
        var db = this.writableDatabase
        var cursor = db.rawQuery(SELECT_ALL,null)

        if(cursor.moveToFirst()){
            do{
                var snapshot = Snapshot("tempPath")
                snapshot.id = cursor.getInt(0)
                snapshot.title = cursor.getString(1)
                snapshot.writeTime = cursor.getLong(2)
                snapshot.imageSource = cursor.getString(3)
                snapshot.content = cursor.getString(4)
                list.add(snapshot)
            }while (cursor.moveToNext())
        }

        return list
    }

    fun removeAll() {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        val db = this.getWritableDatabase() // helper is object extends SQLiteOpenHelper
        db.delete(DATABASE_SNAPSHOT, null, null)
        db.close()
    }
}