package com.withcheese.lptt_benz.manager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.inthecheesefactory.thecheeselibrary.manager.Contextor
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.util.LocationUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Created by nuuneoi on 11/16/2014.
 */
class LocationManager private constructor() {

    private val mContext: Context

    fun addProduct(productId: String, locationId: String,
                   locationName: String, args: Bundle,
                   fm: FragmentManager, processTag: String) {
        val addProduct = HttpClient.getInstance().api
                .addProduct(productId, locationId, locationName)
        addProduct.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val productStatus = response.body()!!.string()
                    if (productStatus == "Waiting") {
                        val targetLocation = locationUtils!!.hashEntranceLocationFragment().get(locationId)
                        if (targetLocation != null) {
                            targetLocation!!.setArguments(args)
                        }
                        if (targetLocation != null) {
                            fm.beginTransaction()
                                    .replace(R.id.contentContainer, targetLocation!!)
                                    .addToBackStack(processTag)
                                    .commit()
                        }
                    } else {
                        val targetLocation = locationUtils!!.hashExitLocationFragment().get(locationId)
                        if (targetLocation != null) {
                            targetLocation!!.setArguments(args)
                        }
                        if (targetLocation != null) {
                            fm.beginTransaction()
                                    .replace(R.id.contentContainer, targetLocation!!)
                                    .addToBackStack(processTag)
                                    .commit()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("CONNECTION", "FAILURE")
            }
        })
    }

    init {
        mContext = Contextor.getInstance().context
    }

    companion object {

        private var instance: LocationManager? = null
        private val locationUtils: LocationUtils? = null

        fun getInstance(): LocationManager {
            if (instance == null)
                instance = LocationManager()
            return instance as LocationManager
        }
    }

}
