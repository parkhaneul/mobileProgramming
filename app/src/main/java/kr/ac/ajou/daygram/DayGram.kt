package kr.ac.ajou.daygram

import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_day_gram.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DayGram : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_gram)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()
    }
}

class mainViewAdapter : Adapter<RecyclerView.ViewHolder>() {
    private var items : Array<Snapshot?> = arrayOfNulls<Snapshot>(5);

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        TODO()
    }

    /*
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.single_line_row, parent, false);
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
        items[position].let { items ->
            with(holder){
                TODO()
            }
        }
    }
}

class Snapshot(title : String?, date : String?, image : Bitmap?, main : String?){
    var title : String?
    var date : String?
    var image : Bitmap?
    var main : String?
    init{
        this.title = title
        this.date = date
        this.image = image
        this.main = main
    }
}

