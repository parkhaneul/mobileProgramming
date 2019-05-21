package kr.ac.ajou.daygram

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_detailview_material.*
import java.util.*

class DayGramDetailView_material : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detailview_material)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        supportPostponeEnterTransition()

        var intent = intent
        var imageSource = intent.getStringExtra("image")
        var date = intent.getLongExtra("date",0L)
        var title = intent.getStringExtra("title")
        var content = intent.getStringExtra("content")

        var imageView = findViewById<ImageView>(R.id.ImageView)
        var dateView = findViewById<TextView>(R.id.DayText)
        var monthView = findViewById<TextView>(R.id.MonthText)
        var titleView = findViewById<TextView>(R.id.TitleText)
        var timeView = findViewById<TextView>(R.id.TimeText)
        var mainView = findViewById<TextView>(R.id.MainText)

        imageView.setImageBitmap(BitmapFactory.decodeFile(imageSource))
        val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
        gc.timeInMillis = date
        dateView.text = gc.get(GregorianCalendar.DATE).toString()
        monthView.text = gc.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, Locale.US)
        titleView.text = title
        timeView.text = gc.get(GregorianCalendar.HOUR_OF_DAY).toString() + " : " + gc.get(GregorianCalendar.MINUTE).toString() + " : " + gc.get(GregorianCalendar.SECOND).toString()
        mainView.text = content

        startPostponedEnterTransition()
    }
}
