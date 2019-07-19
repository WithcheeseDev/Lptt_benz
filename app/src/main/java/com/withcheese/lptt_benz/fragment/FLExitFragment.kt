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
import com.withcheese.lptt_benz.fragment.dialog.IoDialog
import com.withcheese.lptt_benz.fragment.dialog.NioDialog
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.fl_exit_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class FLExitFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var eventCode: String
    /* Views */
    private lateinit var tvFLExitThisLocation: TextView
    private lateinit var tvFLExitProductID: TextView
    private lateinit var tvFLExitProductStatus: TextView
    private lateinit var btnFLExitPaVoCa: Button
    private lateinit var btnFLExitNio: Button
    private lateinit var btnFLExitIo: Button
    private lateinit var itemProcessFLExit: ThisProcessItem
    /* Collections */
    lateinit var locationUtils: LocationUtils
    lateinit var hashLocationID: HashMap<String, String>
    lateinit var hashLocationName: HashMap<String, String>

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
        val rootView = inflater.inflate(R.layout.fl_exit_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        itemProcessFLExit = rootView.itemProcessFLExit
        tvFLExitProductStatus = rootView.tvFLExitProductStatus
        btnFLExitNio = rootView.btnFLExitNio
        btnFLExitIo = rootView.btnFLExitIo
        btnFLExitPaVoCa = rootView.btnFLExitPaVoCa
        itemProcessFLExit.setProcessItem(productID, productionName)
        btnFLExitIo.setOnClickListener {
            if (eventCode == "FNL") {
                val args = Bundle()
                val ioDialog = IoDialog.newInstance()
                ioDialog.arguments = setBundle(args)
                if (fragmentManager != null) {
                    ioDialog.show(fragmentManager!!, "IoDialog")
                }
            } else if (eventCode == "PAVOCA") {
                terminateProcess("PAVOCA")
            }
        }
        btnFLExitNio.setOnClickListener {
            if (eventCode == "FNL") {
                val args = Bundle()
                val flSelectStationFragment = FLSelectStationFragment.newInstance()
                flSelectStationFragment.arguments = setBundle(args)
                beginTransaction(flSelectStationFragment)
            } else if (eventCode == "PAVOCA") {
                beginTransferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                        "n.I.O.", "PAVOCA")
            }
        }
        btnFLExitPaVoCa.setOnClickListener {
            val args = Bundle()
            setBundle(args)
            val nioDialog = NioDialog.newInstance()
            nioDialog.arguments = args
            if (fragmentManager != null) {
                nioDialog.show(fragmentManager!!, "NioDialog")
            }
        }
        getProductStatus(tvFLExitProductStatus)
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

    private fun getProductStatus(textView: TextView) {
        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, hashLocationName["FL-1"])
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val status = response.body()!!.string()
                        textView.text = status
                        setEvent(status)
                    } else if (response.code() == 404) {
                        Log.e("404", "Error 404")
                        beginScanProduct()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t.message != null) {
                    Log.e("Error", t.message!!)
                    beginPopStack()
                }
            }
        })
    }

    companion object {

        fun newInstance(): FLExitFragment {
            val fragment = FLExitFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setEvent(status: String) {
        when (status) {
            "PAVOCA" -> {
                btnFLExitPaVoCa.visibility = View.GONE
                eventCode = "PAVOCA"
            }
            "Processing" -> eventCode = "FNL"
            else -> {
                btnFLExitPaVoCa.visibility = View.GONE
                btnFLExitNio.visibility = View.GONE
                btnFLExitIo.visibility = View.GONE
            }
        }
    }

    private fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("LocationID", locationID)
        bundle.putString("LocationName", locationName)
        bundle.putString("ProductID", productID)
        bundle.putString("FinishLocation", "PAVOCA")
        return bundle
    }

    private fun beginTransferProduct(nextLocationID: String?, nextLocationName: String?,
                                     outcomeStatus: String, mainLocationName: String) {
        val transferProduct =
                HttpClient.getInstance().api.transferProduct(this.productID, this.locationID, this.locationName,
                        nextLocationID, nextLocationName, outcomeStatus, mainLocationName, "", "", "")
        transferProduct.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    beginScanProduct()
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanProduct()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t.message != null) {
                    Log.e("Error", t.message!!)
                    beginScanProduct()
                }
            }
        })
    }

    private fun terminateProcess(terminatedLocation: String) {
        val terminateProcess =
                HttpClient.getInstance().api.terminateProcess(this.productID, this.locationID, terminatedLocation)
        terminateProcess.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    beginScanProduct()
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanProduct()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t.message != null) {
                    Log.e("Error", t.message!!)
                    beginScanProduct()
                }
            }
        })
    }

    fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            productID = bundle.getString("ProductID")!!
            locationID = bundle.getString("LocationID")!!
            locationName = bundle.getString("LocationName")!!
            productionName = bundle.getString("ProductionName")!!
        }
    }

    private fun beginTransaction(fragment: Fragment) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun beginPopStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ScanProductFragment", 1)
        }
    }

    fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }
}
