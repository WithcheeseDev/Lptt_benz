package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.dialog.NioDialog
import com.withcheese.lptt_benz.fragment.dialog.WAExitIODialog
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.wa_exit_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class WAExitStateFragment : Fragment(), View.OnClickListener {
    /* Variables */
    lateinit var productID: String
    lateinit var productStatus: String
    lateinit var locationID: String
    lateinit var locationName: String
    private lateinit var productionName: String
    lateinit var locationUtils: LocationUtils
    lateinit var hashLocationName: HashMap<String, String>
    /* Views */
    lateinit var btnWAExitNIO: Button
    lateinit var btnWAExitIO: Button
    lateinit var tvWAExitProdStatus: TextView
    private lateinit var itemProcessWAExit: ThisProcessItem

    private lateinit var nioDialog: NioDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        getBundle(args)
        Log.e("ProductID", productID)
        Log.e("LocationID", locationID)
        Log.e("LocationName", locationName)
        Log.e("ProductionName", productionName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.wa_exit_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        locationUtils = LocationUtils.instance()
        hashLocationName = locationUtils.hashLocationName()

        btnWAExitIO = rootView.btnWAExitIO
        btnWAExitNIO = rootView.btnWAExitNIO
        tvWAExitProdStatus = rootView.tvWAExitProdStatus
        itemProcessWAExit = rootView.itemProcessWAExit

        itemProcessWAExit.setProcessItem(productID, productionName)
        btnWAExitNIO.setOnClickListener {
            val args = Bundle()
            nioDialog = NioDialog.newInstance()
            nioDialog.arguments = setBundle(args)
            beginShowDialog(nioDialog, "WAExitNIODialog")
        }
        btnWAExitIO.setOnClickListener {
            val args = Bundle()
            val waExitIODialog = WAExitIODialog.newInstance()
            waExitIODialog.arguments = setBundle(args)
            beginShowDialog(waExitIODialog, "WAExitIODialog")
        }

        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, hashLocationName[locationID])
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            productStatus = response.body()!!.string()
                            Log.e("PRODUCT STATUS", productStatus)
                            tvWAExitProdStatus.text = productStatus
                            if (productStatus != "Processing") {
                                btnWAExitIO.visibility = View.GONE
                                btnWAExitNIO.visibility = View.GONE
                            }
                        } else {
                            if (fragmentManager != null && response.errorBody() != null) {
                                Log.e("PRODUCT STATUS ERROR", response.errorBody()!!.toString())
                                fragmentManager!!.popBackStack()
                            }
                        }
                    } else if (response.code() == 404) {
                        if (response.errorBody() != null) {
                            Log.e("404", response.errorBody()!!.toString())
                            nioDialog.dismiss()
                            fragmentManager!!.popBackStack("ScanProductFragment", 1)
                        }
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                nioDialog.dismiss()
                fragmentManager!!.popBackStack()
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnWAExitNIO -> {
                val args = Bundle()
                val nioDialog = NioDialog.newInstance()
                nioDialog.arguments = setBundle(args)
                beginShowDialog(nioDialog, "WAExitNIODialog")
            }
            R.id.btnWAExitIO -> {
                val args = Bundle()
                val waExitIODialog = WAExitIODialog.newInstance()
                waExitIODialog.arguments = setBundle(args)
                beginShowDialog(waExitIODialog, "WAExitIODialog")
            }
        }
    }

    fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("ProductID", this.productID)
        bundle.putString("LocationID", this.locationID)
        bundle.putString("LocationName", this.locationName)
        bundle.putString("StationName", "")
        bundle.putString("FinishLocation", "")
        return bundle
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.productID = bundle.getString("ProductID").toString()
            this.locationID = bundle.getString("LocationID").toString()
            this.locationName = bundle.getString("LocationName").toString()
            this.productionName = bundle.getString("ProductionName").toString()
        }
    }

    private fun beginShowDialog(dialogFragment: DialogFragment, tag: String) {
        if (fragmentManager != null) {
            dialogFragment.show(fragmentManager!!, tag)
        }
    }

    companion object {

        fun newInstance(): WAExitStateFragment {
            val fragment = WAExitStateFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
