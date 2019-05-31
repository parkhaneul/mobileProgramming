package kr.ac.ajou.daygram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_write_snapshot.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WriteSnapshot : AppCompatActivity() {

    private val db = DataBaseHelper(this)
    private val REQUEST_TAKE_PICTURE = 1
    private var mCurrentPhotoPath : String = "path not initialized"
    private var snapshot : Snapshot? = null
    // 사진을 저장할 경로 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_snapshot)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        takePicture()
        saveButton.setOnClickListener{
            saveSnapshot()
        }
    }

    private fun saveSnapshot(){
        if(snapshot != null) {
            snapshot!!.set_title(titleText.text.toString())
            snapshot!!.set_content(contentText.text.toString())
            db.add(snapshot!!)

            val writeIntent = Intent(this,DayGram::class.java)
            startActivity(writeIntent)
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
            snapshot = Snapshot(mCurrentPhotoPath)
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
