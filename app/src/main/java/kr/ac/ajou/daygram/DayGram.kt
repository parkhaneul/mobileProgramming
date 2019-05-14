package kr.ac.ajou.daygram

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_day_gram.*
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

class DayGram : AppCompatActivity() {

    val TAKE_PICTURE = 1
    //https://blog.naver.com/whdals0/221408855795

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_gram)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),1)
            }
        }

        var db = DataBaseHelper(this)
        val recyclerViewAdapter = MainViewAdapter()
        recyclerViewAdapter.items = db.getAll()
        recycler_list.adapter = recyclerViewAdapter
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(recycler_list)

        // activity_day_gram.xml 에 있는 버튼 조작
        CameraButton.setOnClickListener {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent,TAKE_PICTURE)
        }
        /*
        DateButton.setOnClickListener {
            var temp = Snapshot("Title", ""+recyclerViewAdapter.itemCount)
            db.add(temp)
            recyclerViewAdapter.items.add(temp)
            recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.itemCount)
        }
        CalenderButton.setOnClickListener {
            db.removeAll()
            recyclerViewAdapter.items.clear()
            recyclerViewAdapter.notifyDataSetChanged()
        }*/
    }
}

class MainViewAdapter : Adapter<MainViewAdapter.SnapshotViewHolder>() {

    // Snapshot ViewHolder의 내용이 저장되는 ArrayList
    // 원래 Array였는데 추가하기 편하려고 바꿈
    var items : ArrayList<Snapshot> = arrayListOf(Snapshot())

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SnapshotViewHolder{
        var holder = LayoutInflater.from(parent.context).inflate(R.layout.main_view_item_material,parent, false)

        return SnapshotViewHolder(holder)
    }

    // Custom ViewHolder
    override fun onBindViewHolder(holder : SnapshotViewHolder, position : Int) {
        // Snapshot 의 내용들을 items ArrayList의 값에 따라 바꾼다
        // 근데 로컬에 데이터 저장하고 불러오려면 어떻게 바꿔야 하지
        items[position].let { items ->
            with(holder) {
                val gc = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))
                gc.timeInMillis = items.time
                this.dateTextView.text = gc.get(GregorianCalendar.DATE).toString()
                this.monthTextView.text = gc.getDisplayName(GregorianCalendar.MONTH,GregorianCalendar.LONG,Locale.US)
                this.titleTextView.text = items.title
                this.timeTextView.text = gc.get(GregorianCalendar.HOUR_OF_DAY).toString() + " : " + gc.get(GregorianCalendar.MINUTE).toString()
                this.image.setImageResource(items.image)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SnapshotViewHolder(view : View) : RecyclerView.ViewHolder(view){
        // 일단 findViewById로... 안 쓸 수도 있는 것 같긴 한데
        var cardView : MaterialCardView = view.findViewById(R.id.cardView)
        var image : ImageView = view.findViewById(R.id.ImageView)
        var dateTextView : TextView = view.findViewById(R.id.DayText)
        var monthTextView : TextView = view.findViewById(R.id.MonthText)
        var titleTextView : TextView = view.findViewById(R.id.TitleText)
        var timeTextView : TextView = view.findViewById(R.id.TimeText)
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
class Snapshot(title : String = "title", main : String = "main",gc : GregorianCalendar = GregorianCalendar(TimeZone.getTimeZone("Asia/Seoul"))){
    // init 생성자는 필요 없다네요 Kotlin 생성자는 신기하네
    var id : Int = 0
    var title : String = title
    var time = gc.timeInMillis
    var image : Int = R.drawable.image_default
    // 사실 Bitmap 이 뭔지 잘 모르겠다. 아직 이미지는 한 장 뿐이니까...
    var main : String? = main
}
