package com.withcheese.lptt_benz.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.ScanAreaFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.contentContainer, ScanAreaFragment.newInstance(), "ScanAreaFragment")
                    .commit()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                Log.e("BP", "T")
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}
