package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.wa_exit_io_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class WAExitIODialog : DialogFragment() {
    /* Vars */
    lateinit var locationID: String
    lateinit var locationName: String
    lateinit var productID: String
    /* Views */
    lateinit var btnWAExitIOCancel: Button
    lateinit var btnWAExitIOTransfer: Button
    lateinit var btnWAExitIOConfirm: Button
    /* Collections */
    lateinit var locationUtils: LocationUtils
    lateinit var hashLocationID: HashMap<String, String>
    lateinit var hashMainLocationID: HashMap<String, String>
    lateinit var hashLocationName: HashMap<String, String>
    lateinit var hashMainLocationName: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        getBundle(args)
        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        hashMainLocationID = locationUtils.hashMainLocationID()
        hashMainLocationName = locationUtils.hashMainLocationName()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.wa_exit_io_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        setBackgroundTransparent()
        btnWAExitIOCancel = rootView.btnWAExitIOCancel
        btnWAExitIOTransfer = rootView.btnWAExitIOTransfer
        btnWAExitIOConfirm = rootView.btnWAExitIOConfirm

        btnWAExitIOCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnWAExitIOTransfer.setOnClickListener {
            val transferToRect =
                    HttpClient.getInstance().api.transferProduct(productID, locationID, locationName,
                            hashLocationID["03-R"], hashLocationName["03-R"], "I.O.",
                            hashLocationName[locationID], "", "", "")
            transferToRect.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        beginDismiss("ScanProductFragment")
                    } else if (response.code() == 404) {
                        beginDismiss("ScanAreaFragment")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    beginDismiss("ScanAreaFragment")
                }
            })
        }
        btnWAExitIOConfirm.setOnClickListener {
            val transferToEOL =
                    HttpClient.getInstance().api.transferProduct(productID, locationID, locationName,
                            hashLocationID["EL-1"], hashLocationName["EL-1"], "I.O.",
                            hashLocationName[locationID], "", "", "")
            transferToEOL.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        beginDismiss("ScanProductFragment")
                    } else if (response.code() == 404) {
                        beginDismiss("ScanAreaFragment")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    beginDismiss("ScanAreaFragment")
                }
            })
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

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.locationID = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!
            this.productID = bundle.getString("ProductID")!!
        }
    }

    private fun setBackgroundTransparent() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun beginDismiss(tag: String) {
        if (fragmentManager != null && dialog != null) {
            dialog.dismiss()
            fragmentManager!!.popBackStack(tag, 1)
        }
    }

    companion object {

        fun newInstance(): WAExitIODialog {
            val fragment = WAExitIODialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
