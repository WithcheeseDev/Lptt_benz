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
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.eol_entrance_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * Created by nuuneoi on 11/16/2014.
 */
class EOLEntranceFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    /* Views */
    lateinit var btnEOLEntCancel: Button
    lateinit var btnEOLEntConfirm: Button
    lateinit var tvEOLEntProductStatus: TextView
    private lateinit var itemProcessEolEnt : ThisProcessItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.eol_entrance_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById

        btnEOLEntCancel = rootView.btnEOLEntCancel
        btnEOLEntConfirm = rootView.btnEOLEntConfirm
        itemProcessEolEnt = rootView.itemProcessEolEnt
        tvEOLEntProductStatus = rootView.tvEOLEntProductStatus
        itemProcessEolEnt.setProcessItem(productID, productionName)
        btnEOLEntCancel.setOnClickListener {
            beginScanProduct()
        }
        btnEOLEntConfirm.setOnClickListener {
            val registerProduct =
                    HttpClient.getInstance().api.registerProduct(productID, locationID, locationName)
            registerProduct.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        beginPopBackStack()
                    } else if (response.code() == 404) {
                        Log.e("404", "Error 404")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    if (t.message != null) {
                        Log.e("Error", t.message!!)
                        beginPopBackStack()
                    }
                }
            })
        }

        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, locationName)
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    when {
                        response.isSuccessful -> {
                            val productStatus = response.body()!!.string()
                            tvEOLEntProductStatus.text = productStatus
                            if (productStatus != "Waiting List")
                                btnEOLEntConfirm.isEnabled = false
                        }
                        response.code() == 404 -> {
                            Log.e("404", "Error 404")
                            beginScanProduct()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if(t.message != null){
                    Log.e("Error", t.message!!)
                    beginScanProduct()
                }
            }
        })
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

    companion object {

        fun newInstance(): EOLEntranceFragment {
            val fragment = EOLEntranceFragment()
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

    fun beginPopBackStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack()
        }
    }

    fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }
}
