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
import kotlinx.android.synthetic.main.eol_exit_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class EOLExitFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private var stationName = ""
    /* Views */
    private lateinit var btnEOLExitNIO: Button
    private lateinit var btnEOLExitIO: Button
    private lateinit var tvEOLExitThisLocation: TextView
    private lateinit var tvEOLExitProductID: TextView
    private lateinit var tvEOLExitProdStatus: TextView
    /* Collections */
    private lateinit var locationUtils: LocationUtils
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
        /* Get location collections */
        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.eol_exit_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnEOLExitNIO = rootView.btnEOLExitNIO
        btnEOLExitIO = rootView.btnEOLExitIO
        tvEOLExitThisLocation = rootView.tvEOLExitThisLocation
        tvEOLExitProductID = rootView.tvEOLExitProductID
        tvEOLExitProdStatus = rootView.tvEOLExitProdStatus
        tvEOLExitThisLocation.text = locationName
        tvEOLExitProductID.text = productID
        btnEOLExitNIO.setOnClickListener {
            val args = Bundle()
            val selectStationFragment = SelectStationFragment.newInstance()
            selectStationFragment.arguments = setBundle(args, "nio")
            beginTransaction(selectStationFragment)
        }
        btnEOLExitIO.setOnClickListener {
            val args = Bundle()
            val authenticationDialog = AuthenticationDialog.newInstance()
            authenticationDialog.arguments = setBundle(args, "io")
            authenticationDialog.show(fragmentManager!!, "AuthenticationDialog")
        }

        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, hashLocationName["EL-1"])
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val status = response.body()!!.string()
                        tvEOLExitProdStatus.text = status
                        if (status != "Processing") {
                            btnEOLExitIO.isEnabled = false
                            btnEOLExitNIO.isEnabled = false
                        }
                    } else if (response.code() == 404) {
                        Log.e("404", "Error 404")
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

    /*
     * Restore Instance State Here
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    fun setBundle(bd :Bundle, outcomeStatus : String?): Bundle {
        bd.putString("ProductID", productID)
        bd.putString("LocationID", locationID)
        bd.putString("LocationName", locationName)
        bd.putString("ProductionName", productionName)
        bd.putString("StationName", stationName)
        bd.putString("OutcomeStatus", outcomeStatus)
        return bd
    }

    companion object {

        fun newInstance(): EOLExitFragment {
            val fragment = EOLExitFragment()
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

    fun beginTransaction(fm: Fragment) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fm)
                    .addToBackStack(null)
                    .commit()
        }
    }

    fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }
}
