package kr.ac.ajou.daygram

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_day_gram.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DayGram : AppCompatActivity() {


    val REQUEST_TAKE_PICTURE = 1
    // 사진을 저장할 경로 저장
    private var mCurrentPhotoPath : String? = null
    // 찍은 사진으로 만든 Bitmap 저장
    private var currentBitmap : Bitmap? = null
    //https://blog.naver.com/whdals0/221408855795

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

        // 데이터베이스 설정
        var db = DataBaseHelper(this)
        val recyclerViewAdapter = MainViewAdapter()
        recyclerViewAdapter.items = db.getAll()
        recycler_list.adapter = recyclerViewAdapter
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(recycler_list)

        // activity_day_gram.xml 에 있는 버튼 조작
        CameraButton.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture(){
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

        // 파일을 생성하고 url을 가져온다
        val file: File = createFile()
        val uri: Uri = FileProvider.getUriForFile(this, "kr.ac.ajou.daygram.fileprovider", file)

        // 카메라 intent 로 얻은 사진을 url 에 넣는다
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,uri)

        // 사진 결과 확인
        startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TAKE_PICTURE && resultCode == Activity.RESULT_OK){
            // 사진을 저장할 경로 보관. 나중에 씀
            //val auxFile = File(mCurrentPhotoPath)

            // 저장한 사진을 Bitmap 형식으로 불러온다
            currentBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)

            // 불러온 이미지로 작업을 함
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
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
}

class MainViewAdapter : Adapter<MainViewAdapter.SnapshotViewHolder>() {
    // SnapshotViewHolder의 내용이 저장되는 ArrayList
    var items : ArrayList<Snapshot> = arrayListOf(Snapshot())

    // SnapshotViewHolder 생성자를 호출해 줌. 수정할 일 없음
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SnapshotViewHolder{
        var holder = LayoutInflater.from(parent.context).inflate(R.layout.main_view_item_material, parent, false)
        return SnapshotViewHolder(holder)
    }

    // Custom ViewHolder
    override fun onBindViewHolder(holder : SnapshotViewHolder, position : Int) {
        // 사용자가 카드를 스크롤할 때 마다 Snapshot 의 내용들을 items 의 index 에 따라 바꾼다
        // 그러니까 여기선 값을 바꾸는 게 아니라 items 리스트의 값을 가져오기만 해야 함!
        // 값을 바꾸려면 items 의 element, Snapshot 객체를 수정해야 한다

        items[position].let { items ->
            with(holder) {
                val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
                //gc.timeInMillis = items.writeTime
                this.dateTextView.text = gc.get(GregorianCalendar.DATE).toString()
                this.monthTextView.text = gc.getDisplayName(GregorianCalendar.MONTH,GregorianCalendar.LONG,Locale.US)
                this.titleTextView.text = items.title
                //this.timeTextView.text = gc.get(GregorianCalendar.HOUR_OF_DAY).toString() + " : " + gc.get(GregorianCalendar.MINUTE).toString()
                this.timeTextView.text = items.writeTime.toString()
                this.image.setImageResource(items.imageId)
            }
        }

        holder.layout.setOnClickListener {
            // TODO
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SnapshotViewHolder(view : View) : RecyclerView.ViewHolder(view){
        // 레이아웃 activity_day_gram 의 View 들과 연결
        //var cardView : MaterialCardView = view.findViewById(R.id.cardView)
        var image : ImageView = view.findViewById(R.id.ImageView)
        var dateTextView : TextView = view.findViewById(R.id.DayText)
        var monthTextView : TextView = view.findViewById(R.id.MonthText)
        var titleTextView : TextView = view.findViewById(R.id.TitleText)
        var timeTextView : TextView = view.findViewById(R.id.TimeText)
        var layout : FrameLayout = view.findViewById(R.id.layout)
    }

    /*
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.single_line_row, parent, false);0
    final ViewHolder holder = new ViewHolder(view);
    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                remove(mCommentList.get(position));
            }
        }
    });
    return holder;
}
     */
}
class Snapshot(){
    // 초기화, 기본값. 테스트용.
    var id : Int = 0
    var title : String = "Default Title"
    var content : String = "Default Main text"
    var writeTime : Long = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul")).timeInMillis
    var imageId : Int = R.drawable.image_default

    // 생성자를 이렇게 만든다
    // 글쓰기 기능 추가하면 사용할 생성자
    constructor(t : String, m : String) : this(){
        title = t
        content = m
    }
}
