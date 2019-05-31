package kr.ac.ajou.daygram

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_detailview_material.*
import java.util.*

class DayGramDetailView : AppCompatActivity() {

    private var curCardPosition : Int = 0

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
            // 팝업 등으로 확인하는 절차 필요
            // TODO
            removeCard()
            // DetailView 액티비티 종료
            this.finish()
        }
    }

    private fun setDetailView(){
        val intent = intent
        val imageSource = intent.getStringExtra("image")
        val date = intent.getLongExtra("date",0L)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        // DayGram.kt 의 bind()에서 카드의 위치를 받는다
        curCardPosition = intent.getIntExtra("position", 0)

        val imageView = findViewById<ImageView>(R.id.ImageView)
        val dateView = findViewById<TextView>(R.id.DayText)
        val monthView = findViewById<TextView>(R.id.MonthText)
        val titleView = findViewById<TextView>(R.id.TitleText)
        val timeView = findViewById<TextView>(R.id.TimeText)
        val mainView = findViewById<TextView>(R.id.MainText)

        var bitmap = BitmapFactory.decodeFile(imageSource)
        imageView.setImageBitmap(rotateBitmap(bitmap, 90f))

        val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
        gc.timeInMillis = date
        dateView.text = gc.get(GregorianCalendar.DATE).toString()
        monthView.text = gc.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, Locale.US)
        titleView.text = title
        timeView.text = gc.get(GregorianCalendar.HOUR_OF_DAY).toString() + " : " + gc.get(GregorianCalendar.MINUTE).toString() + " : " + gc.get(GregorianCalendar.SECOND).toString()
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
        // TODO
        // 지금 북마크가 되어 있는지 확인한 뒤
        // 별 아이콘을 바꾸고
        // Snapshot 객체의 값을 바꾼다
    }
}
