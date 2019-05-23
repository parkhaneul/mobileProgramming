package kr.ac.ajou.daygram

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WriteSnapshot : AppCompatActivity() {

    private val REQUEST_TAKE_PICTURE = 1
    private var mCurrentPhotoPath : String = "path not initialized"

    // 사진을 저장할 경로 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_snapshot)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        takePicture()
    }

    private fun takePicture(){
        // 카메라 인텐트 호출
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // 파일을 생성하고 url을 가져온다
        val file: File = createFile()
        val uri: Uri = FileProvider.getUriForFile(this, "kr.ac.ajou.daygram.fileprovider", file)
        // 카메라 intent 로 얻은 사진을 url 에 넣는다?
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,uri)

        // 사진 결과 확인
        startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == Activity.RESULT_OK){
            // RecyclerView 에 추가한다
            /*
            recyclerViewAdapter.items.add(Snapshot(mCurrentPhotoPath))
            recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.itemCount)

            // db에 사진을 저장한다
            db.add(recyclerViewAdapter.items.last())*/
        }
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // 파일 이름을 정한다
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

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
