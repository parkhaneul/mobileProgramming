package kr.ac.ajou.daygram

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_day_gram.*
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

//private val screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels

class DayGram : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_gram)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        recycler_list.adapter = MainViewAdapter()
        //recycler_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var helper = PagerSnapHelper()
        helper.attachToRecyclerView(recycler_list)

        // activity_day_gram 에 있는 버튼들 조작
        SearchButton.setOnClickListener {
            // TODO
        }
        ListButton.setOnClickListener {
            // TODO
        }
        WriteButton.setOnClickListener {
            Toast.makeText(this , "Write Button pressed", Toast.LENGTH_SHORT).show()
        }
        DateButton.setOnClickListener {
            // TODO
        }
        CalenderButton.setOnClickListener {
            // TODO
        }
    }
}

class MainViewAdapter : Adapter<MainViewAdapter.SnapshotViewHolder>() {

    var items : Array<Snapshot> = arrayOf(Snapshot(), Snapshot())

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SnapshotViewHolder{
        var holder = LayoutInflater.from(p0.context).inflate(R.layout.main_view_item, p0, false)

        return SnapshotViewHolder(holder)
    }

    override fun onBindViewHolder(holder : SnapshotViewHolder, position : Int) {
        holder.dateTextView.text = items[position].date
        holder.monthTextView.text = items[position].month
        holder.image.setImageResource(R.drawable.image_default)

        /*
        items[position].let { items ->
            with(holder) {}
        }*/
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class SnapshotViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var image : RoundImageView = view.findViewById(R.id.SnapshotImage)
        var dateTextView : TextView = view.findViewById(R.id.DateText)
        var monthTextView : TextView = view.findViewById(R.id.MonthText)
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

class Snapshot(title : String = "title", date : String = "0", month : String = "MAY", main : String = "main"){
    var title : String = title
    var date : String? = date
    var month : String? = month
    //var image : Bitmap? = bitmap
    var main : String? = main
}
