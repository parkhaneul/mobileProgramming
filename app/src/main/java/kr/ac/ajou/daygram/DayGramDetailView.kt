package kr.ac.ajou.daygram

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.item_detailview_material.*
import java.util.*



class DayGramDetailView : AppCompatActivity() {

    private val db = DataBaseHelper(this)
    private var curCardPosition : Int = 0
    private var snapshot : Snapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detailview_material)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        supportPostponeEnterTransition()

        setDetailView()         // Layout 의 View 에 값 할당

        startPostponedEnterTransition()

        // 북마크 버튼
        StarButton.setOnClickListener {
            // DB, ImageView 도 바꿔야 한다
            // TODO
            toggleBookmark()
        }

        // 삭제 버튼
        DeleteButton.setOnClickListener {
            // 삭제 확인 다이얼로그
            val builder = AlertDialog.Builder(this)
            builder.setMessage("정말로 삭제하시겠습니까?")
            builder.setPositiveButton("삭제") { _, _ ->

                // 삭제
                removeCard()
                // DetailView 액티비티 종료
                this.finish()
            }

            // 취소 버튼. 아무것도 안 함
            builder.setNegativeButton("취소", null)

            builder.show()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setDetailView(){
        val intent = intent
        var id = intent.getIntExtra("id",0)
        val imageSource = intent.getStringExtra("image")
        val date = intent.getLongExtra("date",0L)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val starred = intent.getBooleanExtra("starred", false)

        Log.d("snapshot detailView : ", starred.toString())
        snapshot = Snapshot(imageSource, starred,id);

        // DayGram.kt 의 bind()에서 카드의 위치를 받는다
        curCardPosition = intent.getIntExtra("position", 0)

        val imageView = findViewById<ImageView>(R.id.ImageView)
        val dateView = findViewById<TextView>(R.id.DayText)
        val monthView = findViewById<TextView>(R.id.MonthText)
        val titleView = findViewById<TextView>(R.id.TitleText)
        val timeView = findViewById<TextView>(R.id.TimeText)
        val mainView = findViewById<TextView>(R.id.MainText)
        val starButton = findViewById<ImageView>(R.id.StarButton)

        // 표시되는 이미지 회전
        var bitmap = BitmapFactory.decodeFile(imageSource)
        imageView.setImageBitmap(rotateBitmap(bitmap, 90f))

        val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
        gc.timeInMillis = date
        dateView.text = gc.get(GregorianCalendar.DATE).toString()
        monthView.text = gc.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, Locale.US)
        titleView.text = title
        timeView.text = gc.get(GregorianCalendar.HOUR_OF_DAY).toString() + " : " + gc.get(GregorianCalendar.MINUTE).toString() + " : " + gc.get(GregorianCalendar.SECOND).toString()
        if(starred) {
            starButton.setBackgroundResource(R.drawable.ic_star_black_24dp)
        }else{
            starButton.setBackgroundResource(R.drawable.ic_star_border_black_24dp)
        }
        mainView.text = content
    }

    private fun rotateBitmap(source : Bitmap, angle : Float) : Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun removeCard(){
        // 현재 선택한 카드의 위치를 intent 로 보낸다.
        val deleteIntent = Intent(this, DayGram::class.java)
        deleteIntent.putExtra("DELETE_CARD", curCardPosition)
        startActivityForResult(deleteIntent, 1)
    }

    private fun toggleBookmark(){
        // 변경할 카드의 위치를 보낸다
        snapshot!!.isBookmarked = !snapshot!!.isBookmarked

        var db = DataBaseHelper(this)
        db.updateSnapshot(snapshot!!)

        // 별 이미지를 변경한다
        val starButton = findViewById<ImageView>(R.id.StarButton)

        if(snapshot!!.isBookmarked) {
            starButton.setBackgroundResource(R.drawable.ic_star_black_24dp)
        }else{
            starButton.setBackgroundResource(R.drawable.ic_star_border_black_24dp)
        }
    }
}
