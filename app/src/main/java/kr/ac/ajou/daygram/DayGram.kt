package kr.ac.ajou.daygram

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_day_gram.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.util.Pair

interface callBackActivity{
    fun callBack() : Activity
}

class DayGram : AppCompatActivity(), callBackActivity {

    private val REQUEST_TAKE_PICTURE = 1
    // 사진을 저장할 경로 저장
    private var mCurrentPhotoPath : String = "path not initialized"

    private val db = DataBaseHelper(this)

    private val recyclerViewAdapter = MainViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_gram)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        // 안드로이드 버전 확인 후 권한 요청
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),1)
            }
        }

        // DB의 데이터대로 초기화
        recyclerViewAdapter.items = db.getAll()

        // RecyclerView 설정
        recycler_list.adapter = recyclerViewAdapter
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(recycler_list)

        // activity_day_gram.xml 에 있는 버튼 조작
        CameraButton.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture(){
        // 카메라 인텐트 호출
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

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
            recyclerViewAdapter.items.add(Snapshot(mCurrentPhotoPath))
            recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.itemCount)

            // db에 사진을 저장한다
            db.add(recyclerViewAdapter.items.last())
        }
    }

    fun exifOrientationToDegrees(exifOrientation : Int) : Int
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270
        }
        return 0
    }

    fun rotate(bitmap : Bitmap, degrees : Int) : Bitmap
    {
        var temp = bitmap
        if(degrees != 0)
        {
            var m = Matrix()
            m.setRotate(degrees.toFloat(), bitmap.getWidth().toFloat() / 2, bitmap.getHeight().toFloat() / 2)

            var converted = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), m, true)
            if(temp != converted)
            {
                temp.recycle()
                temp = converted
            }
        }
        return temp;
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

    override fun callBack() : Activity {
        return this
    }
}

class MainViewAdapter(context: Context) : Adapter<MainViewAdapter.SnapshotViewHolder>() {

    // SnapshotViewHolder 의 내용이 저장되는 ArrayList
    var callBack : callBackActivity = context as callBackActivity
    var items : ArrayList<Snapshot> = arrayListOf()
    var db : DataBaseHelper? = null

    // SnapshotViewHolder 생성자를 호출해 줌. 수정할 일 없음(있음)
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SnapshotViewHolder{
        var holder = LayoutInflater.from(parent.context).inflate(R.layout.main_view_item_material, parent, false)
        db = DataBaseHelper(parent.context)
        return SnapshotViewHolder(holder)
    }

    // Custom ViewHolder
    override fun onBindViewHolder(holder : SnapshotViewHolder, position : Int) {
        // 사용자가 카드를 스크롤할 때 마다 Snapshot 의 내용들을 items 의 index 에 따라 바꾸는 함수
        // 그러니까 여기선 값을 바꾸는 게 아니라 items 리스트의 값을 가져오기만 해야 함!
        // ViewHolder 멤버 함수 하나에 다 집어넣음
        holder.bind(items[position])

        // 호출 안됨!!! 밑에 있는 ImageView가 Layout을 다 덮어 버림
        holder.titleTextView.setOnLongClickListener {
            Toast.makeText(holder.image.context, "TitleText Clicked: " + position, Toast.LENGTH_SHORT).show()
            this.notifyDataSetChanged()
            true
        }

        // Holder 를 길게 눌렀을 때 동작
        holder.image.setOnLongClickListener{
            Toast.makeText(holder.image.context, "ViewHolder Clicked: " + position, Toast.LENGTH_SHORT).show()

            // DB 에서 지우고
            db?.remove(items[position].writeTime)
            // items ArrayList 에서 지우고
            items.remove(items[position])
            // 새로고침
            this.notifyItemRemoved(position)
            true
        }

        holder.image.setOnClickListener {
            var intent = Intent(it.context,DayGramDetailView::class.java);
            val p1 : Pair<View,String>  = Pair(holder.image,"imageView")
            val p2 : Pair<View,String> = Pair(holder.dateTextView, "dateTextView")
            val p3 : Pair<View,String> = Pair(holder.monthTextView,"monthTextView")
            val p4 : Pair<View,String> = Pair(holder.titleTextView, "titleTextView")
            val p5 : Pair<View,String> = Pair(holder.timeTextView, "timeTextView")
            var options = ActivityOptionsCompat.makeSceneTransitionAnimation(callBack.callBack(),p1,p2,p3,p4,p5);

            startActivity(it.context,intent,options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SnapshotViewHolder(view : View) : RecyclerView.ViewHolder(view){
        // 레이아웃 activity_day_gram 의 View 들과 연결

        var image : ImageView = view.findViewById(R.id.ImageView)
        var dateTextView : TextView = view.findViewById(R.id.DayText)
        var monthTextView : TextView = view.findViewById(R.id.MonthText)
        var titleTextView : TextView = view.findViewById(R.id.TitleText)
        var timeTextView : TextView = view.findViewById(R.id.TimeText)
        //var layout : FrameLayout = view.findViewById(R.id.layout)

        // onBindViewHolder 에서 호출하는 함수. View 에 값을 채워 넣는다
        fun bind(snapshot: Snapshot){
            val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
            dateTextView.text = gc.get(GregorianCalendar.DATE).toString()
            monthTextView.text = gc.getDisplayName(GregorianCalendar.MONTH,GregorianCalendar.LONG,Locale.US)
            titleTextView.text = snapshot.title
            timeTextView.text = snapshot.writeTime.toString()

            var bitmap = BitmapFactory.decodeFile(snapshot.imageSource)
            image.setImageBitmap(bitmap)
        }
    }
}
class Snapshot(imageSrc: String){
    // 생성자. 기본값.
    var id : Int = 0
    var title : String = "Default Title"
    var content : String = "Default Main text"
    var writeTime : Long = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul")).timeInMillis
    var imageSource : String = imageSrc
}
