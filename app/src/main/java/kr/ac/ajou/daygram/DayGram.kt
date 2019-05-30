package kr.ac.ajou.daygram

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.widget.*
import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_day_gram.*
import java.util.*
import androidx.core.util.Pair

interface callBackActivity{
    fun callBack() : Activity
}

class DayGram : AppCompatActivity(), callBackActivity {

    private val db = DataBaseHelper(this)
    var currentYear = 2019
    val recyclerViewAdapter = MainViewAdapter(this)

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

        var searchView = findViewById<SearchView>(R.id.searchView)
        searchView.queryHint = "Search Title"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                recyclerViewAdapter.items = ArrayList(db.getAll().filter {
                    it.title.contains(newText)
                })
                recyclerViewAdapter.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

        // DB의 데이터대로 초기화
        recyclerViewAdapter.items = db.getAll()

        // RecyclerView 설정
        recycler_list.adapter = recyclerViewAdapter
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(recycler_list)

        // 연도 선택 Dialog 호출
        YearText.setOnClickListener {
            Log.d("TAT", "clicked")
            showYearDialog()
        }

        // activity_day_gram.xml 에 있는 버튼 조작
        CameraButton.setOnClickListener {
            val writeIntent = Intent(this,WriteSnapshot::class.java);
            startActivity(writeIntent)
        }

        // 삭제 intent 를 받는다
        // DayGramDetailView.kt 참조.
        val extras : Bundle? = intent.extras

        if(extras != null){
            // 삭제할 카드의 위치를 받는다
            val value = extras.getInt("DELETE_CARD")
            if(value != null)
                removeCard(value)
        }
    }

    private fun removeCard(position: Int){
        val items = recyclerViewAdapter.items

        // DB 에서 지우고
        db?.remove(items[position].writeTime)
        // items ArrayList 에서 지우고
        items.remove(recyclerViewAdapter.items[position])
        // recyclerList 에 알리고
        recyclerViewAdapter.notifyItemRemoved(position)
    }

    override fun onResume() {
        super.onResume()
        currentYear = 2019
        recyclerViewAdapter.items = db.getAll()
    }

    private fun showYearDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setTitle("Year Picker")
        dialog.setContentView(R.layout.year_picker)

        val applyButton : Button = dialog.findViewById(R.id.ApplyButton)
        val cancelButton : Button = dialog.findViewById(R.id.CancelButton)
        val numPicker : NumberPicker = dialog.findViewById(R.id.numberPicker1)

        var standardYear = 2019
        //var currentYear = 2019

        numPicker.maxValue = standardYear + 50
        numPicker.minValue = standardYear - 50
        numPicker.wrapSelectorWheel = false
        numPicker.value = when(standardYear == currentYear){
            true -> standardYear
            else -> currentYear
        }
        numPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        applyButton.setOnClickListener {
            currentYear = numPicker.value
            YearText.text = numPicker.value.toString()
            recyclerViewAdapter.items = ArrayList(db.getAll().filter {
                it.getYear() == currentYear
            })
            recyclerViewAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        dialog.show()
    }

    override fun callBack() : Activity {
        return this
    }
}

class MainViewAdapter(context: Context) : Adapter<MainViewAdapter.SnapshotViewHolder>() {
    var mContext = context
    var callBack = context as callBackActivity

    // SnapshotViewHolder 의 내용이 저장되는 ArrayList
    var items : ArrayList<Snapshot> = arrayListOf()
    var db : DataBaseHelper? = null

    // SnapshotViewHolder 생성자를 호출해 줌
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SnapshotViewHolder{
        var holder = LayoutInflater.from(parent.context).inflate(R.layout.main_view_item_material, parent, false)
        db = DataBaseHelper(parent.context)
        return SnapshotViewHolder(holder)
    }

    // Custom ViewHolder
    override fun onBindViewHolder(holder : SnapshotViewHolder, position : Int) {
        // 사용자가 카드를 스크롤할 때 Snapshot 의 내용들을 items 의 index 에 따라 바꾸는 함수
        // 그러니까 여기선 값을 바꾸는 게 아니라 items 리스트의 값을 가져오기만 해야 함!
        // ViewHolder 멤버 함수 하나에 다 집어넣음

        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SnapshotViewHolder(view : View) : RecyclerView.ViewHolder(view){
        // 레이아웃 activity_day_gram 의 View 들과 연결

        var data : Snapshot? = null
        var image : ImageView = view.findViewById(R.id.ImageView)
        var dateTextView : TextView = view.findViewById(R.id.DayText)
        var monthTextView : TextView = view.findViewById(R.id.MonthText)
        var titleTextView : TextView = view.findViewById(R.id.TitleText)
        var timeTextView : TextView = view.findViewById(R.id.TimeText)

        // onBindViewHolder 에서 호출하는 함수. View 에 값을 채워 넣는다
        fun bind(snapshot: Snapshot){
            data = snapshot
            val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
            gc.timeInMillis = data!!.writeTime
            dateTextView.text = gc.get(GregorianCalendar.DATE).toString()
            monthTextView.text = gc.getDisplayName(GregorianCalendar.MONTH,GregorianCalendar.LONG,Locale.US)
            titleTextView.text = data?.title
            timeTextView.text = gc.get(GregorianCalendar.HOUR_OF_DAY).toString() + " : " + gc.get(GregorianCalendar.MINUTE).toString() + " : " + gc.get(GregorianCalendar.SECOND).toString()
            var bitmap = BitmapFactory.decodeFile(data?.imageSource)
            image.setImageBitmap(bitmap)

            image.setOnClickListener {

                var intent = Intent(mContext, DayGramDetailView::class.java)
                intent.putExtra("image", data?.imageSource)
                intent.putExtra("title", data?.title)
                intent.putExtra("date", data?.writeTime)
                intent.putExtra("content", data?.content)
                intent.putExtra("position", layoutPosition)

                val p1: Pair<View, String> = Pair(image, mContext.getString(R.string.tr_imageView))
                val p2: Pair<View, String> = Pair(dateTextView, mContext.getString(R.string.tr_dateView))
                val p3: Pair<View, String> = Pair(monthTextView, mContext.getString(R.string.tr_monthView))
                val p4: Pair<View, String> = Pair(titleTextView, mContext.getString(R.string.tr_titleView))
                val p5: Pair<View, String> = Pair(timeTextView, mContext.getString(R.string.tr_titleView))
                var options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(callBack.callBack(), p1, p2, p3, p4, p5)
                mContext.startActivity(intent, options.toBundle())
            }
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

    fun getYear() : Int{
        val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
        gc.timeInMillis = writeTime
        return gc.get(GregorianCalendar.YEAR)
    }

    fun set_title(_title : String){
        title = _title
    }

    fun set_content(_content : String){
        content = _content
    }
}
