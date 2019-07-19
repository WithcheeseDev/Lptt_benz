package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.io_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class IoDialog : DialogFragment(), View.OnClickListener {
    /* Variables */
    lateinit var productID: String
    lateinit var locationID: String
    lateinit var locationName: String
    var eventCode: Int = 0
    lateinit var locationUtils: LocationUtils
    lateinit var hashLocationID: HashMap<String, String>
    lateinit var hashLocationName: HashMap<String, String>
    /* Views */
    @BindView(R.id.btnIoCancel)
    lateinit var btnIoCancel: Button
    @BindView(R.id.btnIoConfirm)
    lateinit var btnIoConfirm: Button
    @BindView(R.id.tvIoLocation2Location)
    lateinit var tvIoLocation2Location: TextView

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
        val rootView = inflater.inflate(R.layout.io_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnIoCancel = rootView.btnIoCancel
        btnIoConfirm = rootView.btnIoConfirm
        tvIoLocation2Location = rootView.tvIoLocation2Location

        btnIoCancel.setOnClickListener(this)
        btnIoConfirm.setOnClickListener(this)

        setBackgroundTransparent()

        val locationKey = locationName!![0]
        if (locationKey == 'F') {
            setTargetLocation("FNL to Finish Parking")
            eventCode = 1
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnIoConfirm -> {
                if (eventCode == 1) {
                    transferProduct("FP", "Finish Parking", "I.O.",
                            hashLocationName["FL-1"], "", "", "")
                }
            }
            R.id.btnIoCancel -> {
                beginDismiss()
            }
        }
    }

    private fun transferProduct(nextLocationID: String, nextLocationName: String, outcomeStatus: String,
                                mainLocationName: String?, nioStation: String, empID: String, jobTask: String) {
        val transferProduct = HttpClient.getInstance().api.transferProduct(this.productID, this.locationID, this.locationName,
                nextLocationID, nextLocationName, outcomeStatus, mainLocationName, nioStation, empID, jobTask)
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
        if (dialog != null) {
            dialog.dismiss()
        }
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            productID = bundle.getString("ProductID")!!!!
            locationID = bundle.getString("LocationID")!!!!
            locationName = bundle.getString("LocationName")!!!!
        }
    }

    private fun setTargetLocation(location: String) {
        tvIoLocation2Location.text = location
    }

    private fun setBackgroundTransparent() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {

        fun newInstance(): IoDialog {
            val fragment = IoDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
