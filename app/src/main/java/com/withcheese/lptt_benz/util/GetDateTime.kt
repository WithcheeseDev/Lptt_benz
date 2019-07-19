package com.withcheese.lptt_benz.util

import android.content.Context
import com.inthecheesefactory.thecheeselibrary.manager.Contextor
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nuuneoi on 11/16/2014.
 */
class GetDateTime private constructor() {

    private val mContext: Context

    val date: String
        get() {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            return simpleDateFormat.format(Date())
        }

    val time: String
        get() {
            val simpleTimeFormat = SimpleDateFormat("hh:mm:ss")
            return simpleTimeFormat.format(Date())
        }

    val dateTime: String
        get() {
            val simpleTimeFormat = SimpleDateFormat("yyyy-MM-dd hh:zmm:ss")
            return simpleTimeFormat.format(Date())
        }

    init {
        mContext = Contextor.getInstance().context
    }

    companion object {

        private lateinit var instance: GetDateTime

        fun instance(): GetDateTime {
            if (instance == null)
                instance = GetDateTime()
            return instance as GetDateTime
        }
    }

    /* Get current time !
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:zmm:ss");
        String format = simpleDateFormat.format(new Date());
        tvDateTime.setText(format);
        */

}
