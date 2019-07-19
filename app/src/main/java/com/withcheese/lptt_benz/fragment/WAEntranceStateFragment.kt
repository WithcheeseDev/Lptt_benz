package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.dialog.LoadingDialog
import com.withcheese.lptt_benz.fragment.dialog.RegisterDialog
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.wa_entrance_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Created by nuuneoi on 11/16/2014.
 */
class WAEntranceStateFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var productStatus: String
    private lateinit var timer: CountDownTimer
    /* Views */
    private lateinit var btnWAEntCancel: Button
    private lateinit var btnWAEntConfirm: Button
    private lateinit var tvWAEntProductStatus: TextView
    private lateinit var itemProcessWAEntrance: ThisProcessItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get bundle */
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.wa_entrance_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        tvWAEntProductStatus = rootView.tvWAEntProductStatus
        btnWAEntCancel = rootView.btnWAEntCancel
        btnWAEntConfirm = rootView.btnWAEntConfirm
        itemProcessWAEntrance = rootView.itemProcessWAEntrance
        /* Set production's name && product's id */
        itemProcessWAEntrance.setProcessItem(productID, productionName)
        /* Begin loading content */
        val dialog = LoadingDialog.newInstance()
        dialog.isCancelable = false
        dialog.show(fragmentManager, "LoadingDialog")
        /* Back to previous fragment */
        btnWAEntCancel.setOnClickListener {
            beginPopStack()
        }
        /* Call api_register product to register product */
        btnWAEntConfirm.setOnClickListener {
            val args = Bundle()
            val registerDialog = RegisterDialog.newInstance()
            registerDialog.arguments = setBundle(args)
            beginShowDialog(registerDialog, "RegisterDialog")
        }
        /* Call api_getProductStatus to get scanned product's status */
        val getProductStatus =
                HttpClient.getInstance().api.getProductStatus(productID, locationID, locationName)
        getProductStatus.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    when {
                        response.isSuccessful -> {
                            dialog.dismiss()
                            if (response.body() != null) {
                                productStatus = response.body()!!.string()
                                Log.e("Product Status", productStatus)
                                tvWAEntProductStatus.text = productStatus
                                if (productStatus != "Waiting List") {
                                    btnWAEntConfirm.visibility = View.GONE
                                }
                            }
                            dialog.dismiss()
                        }
                        response.code() == 404 -> if (response.errorBody() != null) {
                            dialog.dismiss()
                            Log.e("404", "Error 404")
                            beginScanProduct()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                when {
                    t.message != null -> {
                        Log.e("Error", t.message!!)
                        dialog.dismiss()
                        beginScanProduct()
                    }
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

    private fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("ProductID", this.productID)
        bundle.putString("LocationID", this.locationID)
        bundle.putString("LocationName", this.locationName)
        bundle.putString("ProductionName", this.productionName)
        return bundle
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.productID = bundle.getString("ProductID")!!
            this.locationID = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!
            this.productionName = bundle.getString("ProductionName")!!
        }
    }

    private fun beginScanProduct() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ScanProdFragment", 1)
        }
    }

    private fun beginPopStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack()
        }
    }

    private fun beginShowDialog(dialogFragment: DialogFragment, tag: String) {
        if (fragmentManager != null) {
            dialogFragment.show(fragmentManager!!, tag)
        }
    }

    private fun setTimer(time: Long, count: Long, dialog: DialogFragment) {
        timer = object : CountDownTimer(time, count) {
            override fun onTick(p0: Long) {
                Log.e("Time", (p0 / 1000).toString())
            }

            override fun onFinish() {
                dialog.dismiss()
                timer.cancel()
            }
        }
    }

    private fun loadingProduct(dialog: DialogFragment) {
        dialog.show(fragmentManager, "Loading")
    }

    companion object {

        fun newInstance(): WAEntranceStateFragment {
            val fragment = WAEntranceStateFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
