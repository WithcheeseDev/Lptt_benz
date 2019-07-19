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
import kotlinx.android.synthetic.main.sw_ent_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * Created by nuuneoi on 11/16/2014.
 */
class SWEntranceFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    /* Views */
    private lateinit var btnSWEntCancel: Button
    private lateinit var btnSWEntConfirm: Button
    private lateinit var tvSWEntProductStatus: TextView
    private lateinit var itemProcessSWEnt: ThisProcessItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.sw_ent_fragment, container, false)
        initInstances(rootView)
        return rootView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnSWEntCancel = rootView.btnSWEntCancel
        btnSWEntConfirm = rootView.btnSWEntConfirm
        itemProcessSWEnt = rootView.itemProcessSWEnt
        tvSWEntProductStatus = rootView.tvSWEntProductStatus
        itemProcessSWEnt.setProcessItem(productID, productionName)
        getProductStatus(tvSWEntProductStatus, btnSWEntConfirm)
        btnSWEntConfirm.setOnClickListener {
            inProgressProduct()
        }
        btnSWEntCancel.setOnClickListener {
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
                if (response.isSuccessful) {
                    try {
                        val status = response.body()!!.string()
                        textView.text = status
                        if (status != "Waiting List") {
                            confirmButton.isEnabled = false
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanProduct()
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
                if (response.isSuccessful) {
                    beginScanProduct()
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanProduct()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                beginScanProduct()
            }
        })
    }

    private fun beginScanProduct() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ShowWaitingList", 1)
        }
    }

    companion object {

        fun newInstance(): SWEntranceFragment {
            val fragment = SWEntranceFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getBundle(args: Bundle?) {
        if (args != null) {
            productID = args.getString("ProductID")!!
            locationID = args.getString("LocationID")!!
            locationName = args.getString("LocationName")!!
            productionName = args.getString("ProductionName")!!
        }
    }
}
