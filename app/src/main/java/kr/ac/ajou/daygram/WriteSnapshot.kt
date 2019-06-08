package kr.ac.ajou.daygram

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_write_snapshot.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WriteSnapshot : AppCompatActivity() {

    private val db = DataBaseHelper(this)
    private var locationManager : LocationManager? = null
    private val REQUEST_TAKE_PICTURE = 1
    private val context : Context = this

    // 현재 경도 위도
    private var curLongitude : Double? = null
    private var curLatitude : Double? = null

    private var mCurrentPhotoPath : String = "path not initialized"
    private var snapshot : Snapshot? = null
    // 사진을 저장할 경로 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_snapshot)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        // 위치정보 받아오기 시도
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        try{
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200L, 0F, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200L, 0F, locationListener)
        }
        catch (ex:SecurityException){
            Toast.makeText(window.context, "No location available", Toast.LENGTH_SHORT).show()
        }


        takePicture()
        saveButton.setOnClickListener{
            saveSnapshot()
        }
    }

    private var locationListener = object : LocationListener {
        override fun onLocationChanged(location : Location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴
            curLongitude = location.longitude //경도
            curLatitude = location.latitude   //위도

           // var msg="New Latitude: "+curLatitude + "New Longitude: "+curLongitude
           // Toast.makeText(context , msg, Toast.LENGTH_LONG).show()

        }
        override fun onProviderDisabled(provider : String) {
            Toast.makeText(context , "Provider disabled", Toast.LENGTH_LONG).show()
        }

        override fun onProviderEnabled(provider : String) {
            Toast.makeText(context , "Provider enabled", Toast.LENGTH_LONG).show()
        }

        override fun onStatusChanged(provider : String, status : Int, extras : Bundle) {
            Toast.makeText(context , "Status changed", Toast.LENGTH_LONG).show()
        }
    }


    private fun saveSnapshot(){
        if(snapshot != null) {
            snapshot!!.set_title(titleText.text.toString())
            snapshot!!.set_content(contentText.text.toString())
            snapshot!!.latitude = curLatitude
            snapshot!!.longitude = curLongitude
            db.add(snapshot!!)

            val writeIntent = Intent(this,DayGram::class.java)
            startActivity(writeIntent)
            this.finish()
        }
    }

    private fun takePicture(){
        // 카메라 인텐트 호출
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 파일을 생성하고 url 을 가져온다
        val file: File = createFile()
        val uri: Uri = FileProvider.getUriForFile(this, "kr.ac.ajou.daygram.fileprovider", file)

        // 카메라 intent 로 얻은 사진을 url 에 넣는다?
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri)

        // 사진 결과 확인
        startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == Activity.RESULT_OK){
            var image = findViewById<ImageView>(R.id.photoResultView)
            var bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            image.setImageBitmap(rotateBitmap(bitmap, 90f))
            image.adjustViewBounds = true
            snapshot = Snapshot(mCurrentPhotoPath)
        }else{
            val writeIntent = Intent(this,DayGram::class.java)
            startActivity(writeIntent)
        }
    }

    private fun rotateBitmap(source : Bitmap, angle : Float) : Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // 파일 이름을 정한다
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir/* directory */
        ).apply {
            // 파일 경로를 저장: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
}
