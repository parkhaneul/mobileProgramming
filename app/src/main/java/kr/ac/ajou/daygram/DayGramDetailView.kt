package kr.ac.ajou.daygram

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_CONTACT : String = "CONTACT"

public class DayGramDetailView : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detailview_material)
    }
}