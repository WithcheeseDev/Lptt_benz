package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class RegisterDialog : DialogFragment(), View.OnClickListener {
    /* Views */
    lateinit var btnRGCancel: Button
    lateinit var btnRGTransfer: Button
    lateinit var btnRGRegister: Button
    /* Vars */
    lateinit var productID: String
    lateinit var locationID: String
    lateinit var locationName: String
    /* Utils */
    lateinit var locationUtils: LocationUtils
    lateinit var hashLocationID: HashMap<String, String>
    lateinit var hashLocationName: HashMap<String, String>
    private lateinit var hashMainLocationID: HashMap<String, String>
    private lateinit var hashMainLocationName: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        hashMainLocationID = locationUtils.hashMainLocationID()
        hashMainLocationName = locationUtils.hashMainLocationName()
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.register_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        setBackgroundTransparent()

        btnRGCancel = rootView.findViewById(R.id.btnRGCancel)
        btnRGTransfer = rootView.findViewById(R.id.btnRGTransfer)
        btnRGRegister = rootView.findViewById(R.id.btnRGRegister)

        btnRGRegister.setOnClickListener {
            val registerProduct = HttpClient.getInstance().api.registerProduct(productID, locationID, locationName)
            registerProduct.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    try {
                        when {
                            response.isSuccessful -> {
                                val status = response.body()!!.string()
                                Log.e("S", status)
                                beginDismiss()
                                dialog.dismiss()
                                fragmentManager!!.popBackStack("ScanProductFragment", 1)
                            }
                            response.code() == 404 -> if (response.errorBody() != null) {
                                Log.e("404", response.errorBody()!!.toString())
                            }
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("E", t.message!!)
                    beginDismiss()
                    beginPopBackStack()
                }
            })
        }
        btnRGTransfer.setOnClickListener(this)
        btnRGCancel.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        //setStyle(STYLE_NO_INPUT, android.R.style.Theme)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setLayout(width, height)
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
            R.id.btnRGRegister -> {

            }
            R.id.btnRGTransfer -> {
                val transfer = HttpClient.getInstance().api.transferProduct(productID, locationID, locationName,
                        hashLocationID["03-R"], hashLocationName["03-R"],
                        "n.I.O.", hashLocationName[locationID], "", "", "")
                transfer.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        beginDismiss()
                        beginPopBackStack()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        beginDismiss()
                        beginPopBackStack()
                    }
                })
            }
            R.id.btnRGCancel -> {
                dialog.dismiss()
            }
        }
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.productID = bundle.getString("ProductID")!!
            this.locationID = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!
        }
    }

    private fun beginDismiss() {
        if (dialog != null)
            dialog.dismiss()
    }

    private fun beginPopBackStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ScanProductFragment", 1)
        }
    }

    private fun setBackgroundTransparent() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {

        fun newInstance(): RegisterDialog {
            val fragment = RegisterDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
