package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.nio_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class NioDialog : DialogFragment(), View.OnClickListener {
    /* Variables */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var stationName: String
    private lateinit var finishLocation: String
    lateinit var eventCode: String
    private var locationKey: Char = '0'
    private lateinit var locationUtils: LocationUtils
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>
    /* Views */
    lateinit var btnNIOCancel: Button
    lateinit var btnNIOConfirm: Button
    lateinit var tvNioLocation2Location: TextView
    lateinit var tvNio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments*/
        val args = arguments
        getBundle(args)
        /* Get location collections */
        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.nio_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        setBackgroundTransparent()

        btnNIOCancel = rootView.btnNIOCancel
        btnNIOConfirm = rootView.btnNIOConfirm
        tvNio = rootView.tvNio
        tvNioLocation2Location = rootView.tvNioLocation2Location
        btnNIOCancel.setOnClickListener(this)
        btnNIOConfirm.setOnClickListener(this)

        locationKey = locationID[0]
        Log.e("FINISH", finishLocation)
        when (locationKey) {
            'W' -> {
                setTargetLocationAndTag("WA to Rectification", "WA2Rect")
            }
            'E' -> {
                when (stationName) {
                    "HUD" -> {
                        setTargetLocationAndTag("HUD to Rectification", "HUD2Rect")
                    }
                    "Roller Pit" -> {
                        setTargetLocationAndTag("Roller Pit to Rectification", "RP2Rect")
                    }
                    "Engine Check" -> {
                        setTargetLocationAndTag("Engine Check to Rectification", "EGC2Rect")
                    }
                }
            }
            'F' -> {
                when (finishLocation) {
                    "PAVOCA" -> {
                        tvNio.text = "PA/VoCA ?"
                        setTargetLocationAndTag("FNL to PA/VoCA", "FL2VOCA")
                    }
                    "" -> {
                        when (stationName) {
                            "FQG" -> {
                                setTargetLocationAndTag("FQG to Rectification", "FQG2Rect")
                            }
                            "WSA" -> {
                                setTargetLocationAndTag("WSA to Rectification", "WSA2Rect")
                            }
                            "PreBuy" -> {
                                setTargetLocationAndTag("Pre-buy off to Rectification", "PRE2Rect")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    /*
     * Save Instance State Here
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save Instance State here
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnNIOConfirm -> {
                when (eventCode) {
                    /* USES : For Wheel Alignment */
                    "WA2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], "", "", "")
                    }
                    /* USES : For End of Line */
                    "HUD2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], stationName, "", "")
                    }
                    "RP2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], stationName, "", "")
                    }
                    "EGC2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], stationName, "", "")
                    }
                    "FL2VOCA" -> {
                        transferProduct("PAVOCA", "PAVOCA",
                                "I.O.", hashLocationName[locationID], "", "", "")
                    }
                    /* USES : For Finish Line */
                    "FQG2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], "FQG", "", "")
                    }
                    "WSA2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], "WSA", "", "")
                    }
                    "PRE2Rect" -> {
                        transferProduct(hashLocationID["03-R"], hashLocationName["03-R"],
                                "n.I.O.", hashLocationName[locationID], "Pre-buy off", "", "")
                    }
                }
            }
            R.id.btnNIOCancel -> {
                beginDismiss()
            }
        }

    }

    private fun setStationNameAndTag(stationName: String, tag: String) {
        val transferTORect = "$stationName to Rectification"
        tvNioLocation2Location.text = transferTORect
        eventCode = tag
    }

    private fun transferProduct(nextLocationID: String?, nextLocationName: String?, outcomeStatus: String,
                                mainLocationName: String?, nioStation: String?, empID: String, jobtask: String) {
        val transferProduct =
                HttpClient.getInstance().api.transferProduct(this.productID, this.locationID, this.locationName,
                        nextLocationID, nextLocationName, outcomeStatus, mainLocationName, nioStation, empID, jobtask)
        transferProduct.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                beginPopBackStack()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                beginPopBackStack()
            }
        })
    }

    private fun beginPopBackStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ScanProductFragment", 1)
            dialog.dismiss()
        }
    }

    private fun beginDismiss() {
        if (dialog != null)
            dialog.dismiss()
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.productID = bundle.getString("ProductID")!!
            this.locationID = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!

            when {
                bundle.getString("StationName") != null -> this.stationName = bundle.getString("StationName")!!
                else -> this.stationName = ""
            }
            when{
                bundle.getString("FinishLocation") != null -> this.finishLocation = bundle.getString("FinishLocation")!!
                else -> this.finishLocation = ""
            }

        }
    }

    private fun setTargetLocationAndTag(location: String, tag: String) {
        tvNioLocation2Location.text = location
        eventCode = tag
    }

    private fun setBackgroundTransparent() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {

        fun newInstance(): NioDialog {
            val fragment = NioDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
