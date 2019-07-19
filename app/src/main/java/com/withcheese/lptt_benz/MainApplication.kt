package com.withcheese.lptt_benz

import android.app.Application

import com.inthecheesefactory.thecheeselibrary.manager.Contextor

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Contextor.getInstance().init(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
