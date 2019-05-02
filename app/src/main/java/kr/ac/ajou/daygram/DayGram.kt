package kr.ac.ajou.daygram

import android.content.res.Resources
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_day_gram.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

private val screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels

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
    }
}

class MainViewAdapter : Adapter<RecyclerView.ViewHolder>() {
    private var items : Array<Snapshot?> = arrayOfNulls<Snapshot>(5);

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = MainViewHolder(p0)

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_view_item,parent,false)){

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

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int) {
        items[position]?.date = ""+position+1
        items[position]?.month = "MAY"


        items[position].let { items ->
            with(holder) {
            }
        }
    }
}

class Snapshot(title : String?, date : String?, month : String?, image : Bitmap?, main : String?){
    var title : String?
    var date : String?
    var month : String?
    var image : Bitmap?
    var main : String?
    init{
        this.title = title
        this.date = date
        this.month = month
        this.image = image
        this.main = main
    }
}
