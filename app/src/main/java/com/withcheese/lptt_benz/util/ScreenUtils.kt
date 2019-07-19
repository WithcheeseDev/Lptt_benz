package com.withcheese.lptt_benz.util

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import com.inthecheesefactory.thecheeselibrary.manager.Contextor


/**
 * Created by nuuneoi on 10/16/2014.
 */
class ScreenUtils private constructor() {

    private val mContext: Context

    val screenWidth: Int
        get() {
            val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }

    val screenHeight: Int
        get() {
            val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }

    init {
        mContext = Contextor.getInstance().context
    }

    companion object {

        private var instance: ScreenUtils? = null

        fun getInstance(): ScreenUtils {
            if (instance == null)
                instance = ScreenUtils()
            return instance as ScreenUtils
        }
    }

}
