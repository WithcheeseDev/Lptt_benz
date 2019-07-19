package com.withcheese.lptt_benz.manager

import android.content.Context

import com.inthecheesefactory.thecheeselibrary.manager.Contextor

/**
 * Created by nuuneoi on 11/16/2014.
 */
class SingletonTemplate private constructor() {

    private val mContext: Context

    init {
        mContext = Contextor.getInstance().context
    }

    companion object {

        private var instance: SingletonTemplate? = null

        fun getInstance(): SingletonTemplate {
            if (instance == null)
                instance = SingletonTemplate()
            return instance as SingletonTemplate
        }
    }

}
