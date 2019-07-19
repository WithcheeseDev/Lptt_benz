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
import kotlinx.android.synthetic.main.rectification_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class RectificationFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var eventCode: String
    /* Views */
    private lateinit var btnRectCancel: Button
    private lateinit var btnRectRegister: Button
    private lateinit var btnRectTransfer: Button
    private lateinit var tvRectification: TextView
    private lateinit var tvRectProductStatus: TextView
    private lateinit var itemProcessRect: ThisProcessItem
    /* Collections */
    private lateinit var locationUtils: LocationUtils
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get location collections */
        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        /* Get arguments */
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.rectification_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnRectCancel = rootView.btnRectCancel
        btnRectRegister = rootView.btnRectRegister
        btnRectTransfer = rootView.btnRectTransfer
        tvRectification = rootView.tvRectification
        tvRectProductStatus = rootView.tvRectProductStatus
        itemProcessRect = rootView.itemProcessRect

        btnRectCancel.setOnClickListener {
            beginPopStack()
        }
        btnRectRegister.setOnClickListener {
            when (eventCode) {
                "Waiting" -> {
                    val registerProduct =
                            HttpClient.getInstance().api.registerProduct(productID, locationID, locationName)
                    registerProduct.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            if (response.isSuccessful) {
                                Log.e("Product", "registered")
                                beginScanProduct()
                            } else if (response.code() == 404) {
                                Log.e("404", "Error 404")
                                beginScanProduct()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("Error", t.message!!)
                            beginScanProduct()
                        }
                    })
                }
                "OnProgress" -> {
                    val args = Bundle()
                    if (locationID == "03-R") {
                        val rectTaskFragment = RectTaskFragment.newInstance()
                        rectTaskFragment.arguments = setBundle(args)
                        beginTransaction(rectTaskFragment, "RectTaskFragment")
                    }
                    if (locationID == "02-R") {
                        val jobTaskFragment = JobTaskFragment.newInstance()
                        jobTaskFragment.arguments = setBundleWithTask(args)
                        beginTransaction(jobTaskFragment, "JobTaskFragment")
                    }
                }
            }
        }
        btnRectTransfer.setOnClickListener {
            val args = Bundle()
            val selectLocationFragment = SelectLocationFragment.newInstance()
            selectLocationFragment.arguments = setBundle(args)
            beginTransaction(selectLocationFragment, "SelectLocationFragment")
        }
        itemProcessRect.setProcessItem(productID, productionName)

        if (locationID == "02-R") {
            tvRectification.text = "Paint Rectification"
        }

        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, locationName)
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            when (val productStatus = response.body()!!.string()) {
                                "Waiting List", "STAY IN : KA" -> {
                                    tvRectProductStatus.text = productStatus
                                    eventCode = "Waiting"
                                }
                                "Processing" -> {
                                    btnRectRegister.text = "In progress"
                                    btnRectTransfer.visibility = View.GONE
                                    tvRectProductStatus.text = productStatus
                                    eventCode = "OnProgress"
                                }
                                else -> {
                                    tvRectProductStatus.text = productStatus
                                    btnRectRegister.visibility = View.GONE
                                    btnRectTransfer.visibility = View.GONE
                                }
                            }
                        }
                    } else if (response.code() == 404)
                        if (fragmentManager != null)
                            fragmentManager!!.popBackStack("ScanProductFragment", 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (fragmentManager != null) {
                    Log.e("Error msg", t.message!!)
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

        fun newInstance(): RectificationFragment {
            val fragment = RectificationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.locationID = bundle.getString("LocationID")!!
            this.productID = bundle.getString("ProductID")!!
            this.locationName = bundle.getString("LocationName")!!
            this.productionName = bundle.getString("ProductionName")!!
        }
    }

    private fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("LocationID", this.locationID)
        bundle.putString("ProductID", this.productID)
        bundle.putString("LocationName", this.locationName)
        bundle.putString("ProductionName", this.productionName)
        return bundle
    }

    private fun setBundleWithTask(bundle: Bundle): Bundle {
        bundle.putString("LocationID", this.locationID)
        bundle.putString("ProductID", this.productID)
        bundle.putString("LocationName", this.locationName)
        bundle.putString("ProductionName", this.productionName)
        bundle.putString("StationName", "")
        bundle.putString("OutcomeStatus", "")
        bundle.putString("ProcessTask", "")
        return bundle
    }

    private fun beginTransaction(fragment: Fragment, tag: String) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fragment)
                    .addToBackStack(tag)
                    .commit()
        }
    }

    private fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }

    private fun beginPopStack() {
        fragmentManager!!.popBackStack()
    }
}
