package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.fl_ent_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * Created by nuuneoi on 11/16/2014.
 */
class FLEntFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    /* Views */
    private lateinit var tvFLEntThisLocation: TextView
    private lateinit var tvFLEntProductID: TextView
    private lateinit var tvFLEntProductStatus: TextView
    private lateinit var btnFLEntCancel: Button
    private lateinit var btnFLEntConfirm: Button
    private lateinit var itemProcessFLEnt: ThisProcessItem
    /* Collections */
    private lateinit var locationUtils: LocationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
        /* Get location collections  */
        locationUtils = LocationUtils.instance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fl_ent_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        itemProcessFLEnt = rootView.itemProcessFLEnt
        tvFLEntProductStatus = rootView.tvFLEntProductStatus
        btnFLEntCancel = rootView.btnFLEntCancel
        btnFLEntConfirm = rootView.btnFLEntConfirm
        itemProcessFLEnt.setProcessItem(productID, productionName)
        getProductStatus(tvFLEntProductStatus, btnFLEntConfirm)
        btnFLEntConfirm.setOnClickListener {
            inProgressProduct()
        }
        btnFLEntCancel.setOnClickListener {
            beginScanProduct()
        }
    }

    /*
     * Restore Instance State Here
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private fun getProductStatus(textView: TextView, confirmButton: Button) {
        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, locationName)
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if(response.isSuccessful) {
                        val status = response.body()!!.string()
                        textView.text = status
                        if (status != "Waiting List") {
                            confirmButton.isEnabled = false
                        }
                    }else if (response.code() == 404){
                        Log.e("404", "Error 404")
                        beginScanProduct()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    private fun inProgressProduct() {
        val registerProduct =
                HttpClient.getInstance().api.registerProduct(productID, locationID, locationName)
        registerProduct.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    beginScanProduct()
                }else if(response.code() == 404){
                    Log.e("404", "Error 404")
                    beginScanProduct()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t.message != null){
                    Log.e("Error", t.message!!)
                    beginScanProduct()
                }
            }
        })
    }

    companion object {

        fun newInstance(): FLEntFragment {
            val fragment = FLEntFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            productID = bundle.getString("ProductID")!!
            locationID = bundle.getString("LocationID")!!
            locationName = bundle.getString("LocationName")!!
            productionName = bundle.getString("ProductionName")!!
        }
    }

    private fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }

}
