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
import com.withcheese.lptt_benz.fragment.dialog.AuthenticationDialog
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.sw_exit_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class SWExitFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private var stationName = "none"
    /* Views */
    private lateinit var btnSWExitNio: Button
    private lateinit var btnSWExitIo: Button
    private lateinit var tvSWExitProductStatus: TextView
    private lateinit var itemProcessSWExit: ThisProcessItem
    /* Collections */
    private lateinit var locationUtils: LocationUtils
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.sw_exit_fragment, container, false)
        initInstances(rootView)
        return rootView
    }


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

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnSWExitNio = rootView.btnSWExitNio
        btnSWExitIo = rootView.btnSWExitIo
        itemProcessSWExit = rootView.itemProcessSWExit
        tvSWExitProductStatus = rootView.tvSWExitProductStatus
        itemProcessSWExit.setProcessItem(productID, productionName)
        getProductStatus(tvSWExitProductStatus, btnSWExitNio, btnSWExitIo)
        btnSWExitNio.setOnClickListener {
            val jobTaskFragment = JobTaskFragment.newInstance()
            jobTaskFragment.arguments = setBundle(locationID, locationName, productID,
                    stationName, "nio", "Shower")
            beginTransaction(jobTaskFragment)
        }
        btnSWExitIo.setOnClickListener {
            val args = Bundle()
            args.putString("LocationID", locationID)
            args.putString("LocationName", locationName)
            args.putString("ProductID", productID)
            args.putString("OutcomeStatus", "io")
            val authenticationDialog = AuthenticationDialog.newInstance()
            authenticationDialog.arguments = args
            authenticationDialog.show(fragmentManager!!, "AuthDialog")
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

    fun setBundle(locationId: String?, locationName: String?, productId: String?,
                  stationName: String, OutcomeStatus: String, task: String): Bundle {
        val args = Bundle()
        args.putString("ProductID", productId)
        args.putString("LocationID", locationId)
        args.putString("LocationName", locationName)
        args.putString("ProductionName", productionName)
        args.putString("StationName", stationName)
        args.putString("OutcomeStatus", OutcomeStatus)
        args.putString("Task", task)
        return args
    }

    private fun getProductStatus(textView: TextView, nioButton: Button, ioButton: Button) {
        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, hashLocationName["SW-1"])
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val status = response.body()!!.string()
                            textView.text = status
                            if (status != "Processing") {
                                nioButton.visibility = View.GONE
                                ioButton.visibility = View.GONE
                            }
                        }
                    } else if (response.code() == 404) {
                        if (fragmentManager != null) {
                            fragmentManager!!.popBackStack("ScanProductFragment", 1)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
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

    private fun beginScanProduct() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ShowWaitingList", 1)
        }
    }

    private fun beginTransaction(fm: Fragment) {
        fragmentManager!!.beginTransaction()
                .replace(R.id.contentContainer, fm)
                .addToBackStack(null)
                .commit()
    }

    companion object {

        fun newInstance(): SWExitFragment {
            val fragment = SWExitFragment()
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
            productionName = args . getString ("ProductionName")!!
        }
    }
}
