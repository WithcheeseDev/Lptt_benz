package com.withcheese.lptt_benz.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.inthecheesefactory.thecheeselibrary.manager.Contextor
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.scan_prod_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

/**
 * Created by nuuneoi on 11/16/2014.
 */
class ScanProdFragment : Fragment() {
    /* Vars */
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var productID: String
    private lateinit var productStatus: String
    /* Views */
    private lateinit var imgBtnScanner: ImageButton
    private lateinit var editScanProduct: EditText
    private lateinit var tvThisLocation: TextView
    private lateinit var editSearchProduct: EditText
    private lateinit var btnSearchProduct: Button
    /* Location Utils */
    private lateinit var locationUtils: LocationUtils
    private lateinit var listLocation: List<String>
    /* Location ID & Name */
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>
    /* Production Line */
    private lateinit var hashMainProductionLine: HashMap<String, String>
    private lateinit var hashEntranceProductionLine: HashMap<String, String>
    private lateinit var hashExitProductionLine: HashMap<String, String>
    /* Location Entrance & Exit fragment */
    private lateinit var hashEntLocationFragment: HashMap<String, Fragment>
    private lateinit var hashExitLocationFragment: HashMap<String, Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get argument */
        val args = arguments
        getBundle(args)
        /* Get location utils */
        locationUtils = LocationUtils.instance()
        listLocation = locationUtils.listLocationID()
        /* Get location name & id */
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        /* Get production line */
        hashMainProductionLine = locationUtils.hashMainProductionLine()
        hashEntranceProductionLine = locationUtils.hashEntranceProductionLine()
        hashExitLocationFragment = locationUtils.hashExitLocationFragment()
        /* Get location fragment */
        hashEntLocationFragment = locationUtils.hashEntranceLocationFragment()
        hashExitProductionLine = locationUtils.hashExitProductionLine()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.scan_prod_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        tvThisLocation = rootView.tvThisLocation
        editScanProduct = rootView.editScanProduct
        imgBtnScanner = rootView.imgBtnScanning
        editSearchProduct = rootView.editSearchProduct
        btnSearchProduct = rootView.btnSearchProduct
        /* Handle event to scan product */
        imgBtnScanner.setOnClickListener {
            if (!editScanProduct.requestFocus())
                imgBtnScanner.setBackgroundResource(R.drawable.scanner_icon_ready)
        }
        /* Set focus to scanner first */
        editScanProduct.requestFocus()
        /* Handle scanner background when focus changed */
        editScanProduct.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (context != null) {
                    val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    imgBtnScanner.setImageResource(R.drawable.scanner_icon_ready)
                }
            } else
                imgBtnScanner.setImageResource(R.drawable.scanner_icon_not_ready)
        }
        /* Get corresponding location information */
        for (location in listLocation) {
            if (location == locationID) {
                locationName = hashLocationName[location]!!
                tvThisLocation.text = hashMainProductionLine[location]
                Log.e("LOCATION_NAME", locationName)
                break
            }
        }
        /* Handle event when pressed log in button */
        editScanProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                when {
                    editScanProduct.length() == 7 || editScanProduct.length() == 8 -> {
                        val exceptWord = editScanProduct.text[2]
                        when{
                            exceptWord != '-' -> {
                                Log.e("Product ID", s.toString())
                                productID = editScanProduct.text.toString()
                                newProduct(productID, locationID, locationName)
                            }
                            else -> editScanProduct.setText("")
                        }
                    }
                    editScanProduct.length() > 9 -> editScanProduct.setText("")
                }
            }
        })

        btnSearchProduct.setOnClickListener {
            when {
                editSearchProduct.length() != 0 -> {
                    val args = Bundle()
                    productID = editSearchProduct.text.toString()
                    args.putString("ProductID", productID)
                    args.putString("LocationID", "")
                    args.putString("LocationName", "")
                    val fm = ShowProductDetailFragment.newInstance()
                    fm.arguments = args
                    editSearchProduct.setText("")
                    beginTransaction(fm)
                }
            }
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

    /* Begin fragment's transaction */
    private fun beginTransaction(fragment: Fragment?) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fragment!!)
                    .addToBackStack("ScanProductFragment")
                    .commit()
        }
    }

    /* Set location bundle */
    private fun setBundle(fragment: Fragment?, bundle: Bundle) {
        if (fragment != null) {
            bundle.putString("LocationID", this.locationID)
            bundle.putString("LocationName", this.locationName)
            bundle.putString("ProductID", this.productID)
            bundle.putString("ProductionName", this.productionName)
            fragment.arguments = bundle
        }
    }

    /* Get location bundle */
    private fun getBundle(bundle: Bundle?) {
        locationID = bundle!!.getString("LocationID")!!
    }

    /* Begin call api_addProduct */
    private fun newProduct(productID: String?, locationID: String?, locationName: String?) {
        val newProduct =
                HttpClient.getInstance().api.addProduct(productID, locationID, locationName)
        newProduct.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when {
                    response.isSuccessful -> try {
                        if (response.body() != null) {
                            productStatus = response.body()!!.string()
                            Log.e("Product Status", productStatus)
                        }
                        val args = Bundle()
                        when (productStatus) {
                            "Waiting List", "INSERTED" -> {
                                val targetLocation = hashEntLocationFragment[locationID]
                                productionName = hashEntranceProductionLine[locationID]!!
                                setBundle(targetLocation, args)
                                beginTransaction(targetLocation)
                                editScanProduct.setText("")
                            }
                            else -> {
                                val targetLocation = hashExitLocationFragment[locationID]
                                productionName = hashExitProductionLine[locationID]!!
                                setBundle(targetLocation, args)
                                beginTransaction(targetLocation)
                                editScanProduct.setText("")
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    response.code() == 404 -> {
                        when {
                            response.errorBody() != null -> {
                                Toast.makeText(Contextor.getInstance().context,
                                        response.errorBody()!!.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                editScanProduct.setText("")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(Contextor.getInstance().context,
                        t.message,
                        Toast.LENGTH_SHORT).show()
                editScanProduct.setText("")
            }
        })
    }

    companion object {
        fun newInstance(): ScanProdFragment {
            val fragment = ScanProdFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
