package com.withcheese.lptt_benz.manager

import android.content.Context
import com.google.gson.GsonBuilder
import com.inthecheesefactory.thecheeselibrary.manager.Contextor
import com.withcheese.lptt_benz.manager.http.HttpApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by nuuneoi on 11/16/2014.
 */
class HttpClient private constructor() {
    private val retrofit: Retrofit


    val api: HttpApi
        get() = retrofit.create(HttpApi::class.java)

    init {
        val mContext: Context = Contextor.getInstance().context

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()

        retrofit = Retrofit.Builder()
                .baseUrl(Base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    companion object {
        private const val Base_url = "http://192.168.1.4:9000"
        private var instance: HttpClient? = null

        fun getInstance(): HttpClient {
            if (instance == null)
                instance = HttpClient()
            return instance as HttpClient
        }
    }

}
