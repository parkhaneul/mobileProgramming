package kr.ac.ajou.daygram

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.FieldPosition

private const val DATABASE_NAME : String = "SnapShots"
private const val DATABASE_VERSION : Int = 4

class DataBaseHelper(context : Context) : SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION){
    private val DATABASE_SNAPSHOT = "Snapshot"

    private val KEY_ID = "id"
    private val KEY_TITLE = "Title"
    private val KEY_TIME = "Time"
    private val KEY_IMAGE = "Image"
    private val KEY_MAIN = "Main"
    private val KEY_STAR = "Star"
    private val KEY_LATITUDE = "Latitude"
    private val KEY_LONGITUDE = "Longitude"

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_SNAPSHOT : String = "CREATE TABLE " + DATABASE_SNAPSHOT + " (" +
                KEY_ID + " integer primary key autoincrement, " +
                KEY_TITLE + " TITLE, " +
                KEY_TIME + " TIME, " +
                KEY_IMAGE + " IMAGE, " +
                KEY_STAR + " STAR, " +
                KEY_LATITUDE + " LATITUDE, " +
                KEY_LONGITUDE + " LONGITUDE, " +
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
        value.put("Latitude", snapshot.latitude)
        value.put("Longitude", snapshot.longitude)
        value.put("Star", snapshot.getStarToInt())
        db.insert(DATABASE_SNAPSHOT,null,value)
        db.close()
    }

    // DB 에서 지울 때는 시간 : Long 을 비교해서 지운다
    // 그러니까 Snapshot 의 시간은 같을 일이 없다고 가정함
    fun remove(time: Long){
        var db = this.writableDatabase
        db.delete(DATABASE_SNAPSHOT, "Time=?", arrayOf(time.toString()))
        db.close()
        //Time으로 잡지 말고, id로 잡아야함 근데, TIme이 고유의 값이니까 그럴 필요 없겠다.
    }

    fun get(id : Int) : Snapshot{
        var db = this.writableDatabase
        var SELECT_SNAPSHOT : String = "SELECT * FROM " + DATABASE_SNAPSHOT + " WHERE " + KEY_ID + " = " + id
        var cursor = db.rawQuery(SELECT_SNAPSHOT,null)
        cursor.moveToFirst()

        var snapshot = Snapshot("tempPath")
        snapshot.id = cursor.getInt(0)
        snapshot.title = cursor.getString(1)
        snapshot.writeTime = cursor.getLong(2)
        snapshot.imageSource = cursor.getString(3)
        snapshot.content = cursor.getString(4)
        snapshot.latitude = cursor.getDouble(5)
        snapshot.longitude = cursor.getDouble(6)
        snapshot.setStarFromInt(1)
        //Log.d("get db : ", cursor.getInt(7).toString())

        db.close()

        return snapshot
    }

    fun updateSnapshot(snapshot: Snapshot){
        var UPDATE_SNAPSHOT : String = "UPDATE " + DATABASE_SNAPSHOT + " SET " + KEY_STAR + " = " + snapshot.getStarToInt() + " WHERE " + KEY_ID + " = " + snapshot.id
        var db = this.writableDatabase
        //var value = ContentValues()
        //value.put(KEY_STAR, snapshot.getStarToInt())

        db.execSQL(UPDATE_SNAPSHOT)
        //db.update(DATABASE_SNAPSHOT, value,KEY_ID + "=" + snapshot.id, null)
        db.close()
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
                snapshot.latitude = cursor.getDouble(5)
                snapshot.longitude = cursor.getDouble(6)
                snapshot.setStarFromInt(1)
                list.add(snapshot)
            }while (cursor.moveToNext())
        }
        db.close()
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