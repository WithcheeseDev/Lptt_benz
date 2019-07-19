package com.withcheese.lptt_benz.util

import android.content.Context
import android.provider.Settings
import com.inthecheesefactory.thecheeselibrary.manager.Contextor

/**
 * Created by nuuneoi on 10/16/2014.
 */
class Utils private constructor() {

    private val mContext: Context = Contextor.getInstance().context

    val deviceId: String
        get() = Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)

    val versionName: String
        get() {
            try {
                val pInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
                return pInfo.versionName
            } catch (e: Exception) {
                return "1.0"
            }

        }

    companion object {

        private var instance: Utils? = null

        fun getInstance(): Utils {
            if (instance == null)
                instance = Utils()
            return instance as Utils
        }
    }

}
