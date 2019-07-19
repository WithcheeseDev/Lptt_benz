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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.adapters.WaitingProductEvenAdapter
import com.withcheese.lptt_benz.adapters.WaitingProductOddAdapter
import com.withcheese.lptt_benz.dao.waitingProductDao.ProductWaitingCollectionDao
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.manager.LocationManager
import com.withcheese.lptt_benz.manager.ProductItemManager
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.show_waiting_list_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

/**
 * Created by nuuneoi on 11/16/2014.
 */
class ShowWaitingListFragment : Fragment() {
    /* Vars */
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productID: String
    private lateinit var productionName: String
    /* Views */
    private lateinit var listViewWaitingListOdd: ListView
    private lateinit var listViewWaitingListEven: ListView
    private lateinit var tvSpecialThisLocation: TextView
    private lateinit var editSpecialScanProductID: EditText
    private lateinit var imgBtnScanning: ImageButton
    /* Waiting item adapters */
    lateinit var waitingProductEvenAdapter: WaitingProductEvenAdapter
    lateinit var waitingProductOddAdapter: WaitingProductOddAdapter
    /* Location utils & manager */
    private lateinit var locationManager: LocationManager
    private lateinit var locationUtils: LocationUtils
    /* Location's list*/
    private var listLocation: List<String> = ArrayList()
    /* Location ID & Name */
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>
    /* Location entrance & exit fragment*/
    private var hashEntLocationFragment = HashMap<String, Fragment>()
    private var hashExitLocationFragment = HashMap<String, Fragment>()
    /* Production Line */
    private lateinit var hashMainProductionLine: HashMap<String, String>
    private lateinit var hashEntranceProductionLine: HashMap<String, String>
    private lateinit var hashExitProductionLine: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments*/
        val args = arguments
        getBundle(args!!)
        Log.e("LocationID", locationID)
        /* Get location collections */
        locationUtils = LocationUtils.instance()
        locationManager = LocationManager.getInstance()
        /* Get location's list */
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
        val rootView = inflater.inflate(R.layout.show_waiting_list_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        listViewWaitingListOdd = rootView.listViewWaitingListOdd
        listViewWaitingListEven = rootView.listViewWaitingListEven
        tvSpecialThisLocation = rootView.tvSpecialThisLocation
        editSpecialScanProductID = rootView.editSpecialScanProductID
        imgBtnScanning = rootView.imgBtnScanning
        /* Call adapters */
        waitingProductOddAdapter = WaitingProductOddAdapter()
        waitingProductEvenAdapter = WaitingProductEvenAdapter()
        /* Feed item from adapters to both list view  */
        listViewWaitingListOdd.adapter = waitingProductOddAdapter
        listViewWaitingListEven.adapter = waitingProductEvenAdapter
        /* Set focus to scan first */
        editSpecialScanProductID.requestFocus()
        /* Handle scanning event */
        imgBtnScanning.setOnClickListener {
            if (!editSpecialScanProductID.requestFocus()) {
                imgBtnScanning.setBackgroundResource(R.drawable.scanner_icon_ready)
            }
        }
        /* Handle event when focus changed */
        editSpecialScanProductID.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (context != null) {
                    val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    imgBtnScanning.setImageResource(R.drawable.scanner_icon_ready)
                }
            } else
                imgBtnScanning.setImageResource(R.drawable.scanner_icon_not_ready)
        }
        /* Get corresponding location information */
        for (location in listLocation) {
            if (location == locationID) {
                locationName = hashLocationName[location]!!
                tvSpecialThisLocation.text = hashMainProductionLine[location]
                Log.e("LOCATION_NAME", locationName)
                break
            }
        }
        /* Get waiting items : Odd item */
        val getWaitingItemsOdd =
                HttpClient.getInstance().api.getWaitingItemsOdd(locationName)
        getWaitingItemsOdd.enqueue(object : Callback<ProductWaitingCollectionDao> {
            override fun onResponse(call: Call<ProductWaitingCollectionDao>, response: Response<ProductWaitingCollectionDao>) {
                if (response.isSuccessful) {
                    val dao = response.body()
                    ProductItemManager.getInstance().daoOdd = dao
                    waitingProductOddAdapter.notifyDataSetChanged()
                    /* Get waiting items : Even item */
                    val getWaitingItemsEven =
                            HttpClient.getInstance().api.getWaitingItemsEven(locationName)
                    getWaitingItemsEven.enqueue(object : Callback<ProductWaitingCollectionDao> {
                        override fun onResponse(call: Call<ProductWaitingCollectionDao>, response: Response<ProductWaitingCollectionDao>) {
                            val dao = response.body()
                            ProductItemManager.getInstance().daoEven = dao
                            waitingProductEvenAdapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<ProductWaitingCollectionDao>, t: Throwable) {
                            if (t.message != null) {
                                Log.e("Error", t.message!!)
                                beginScanLocation()
                            }
                        }
                    })
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanLocation()
                }
            }

            override fun onFailure(call: Call<ProductWaitingCollectionDao>, t: Throwable) {
                if (t.message != null) {
                    Log.e("Error", t.message!!)
                    beginScanLocation()
                }
            }
        })
        /* Being transaction when scan product */
        editSpecialScanProductID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                when {
                    editSpecialScanProductID.text.length == 7 || editSpecialScanProductID.text.length == 8 -> {
                        productID = editSpecialScanProductID.text.toString()
                        val addProduct =
                                HttpClient.getInstance().api.addProduct(productID, locationID, locationName)
                        addProduct.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                if (response.isSuccessful) {
                                    try {
                                        val productStatus = response.body()!!.string()
                                        Log.e("ShowWaitingList", productStatus)
                                        val args = Bundle()
                                        if (productStatus == "Waiting List") {
                                            val targetLocation = hashEntLocationFragment[locationID]
                                            productionName = hashEntranceProductionLine[locationID]!!
                                            targetLocation!!.arguments = setBundle(args)
                                            beginTransaction(targetLocation)
                                            editSpecialScanProductID.setText("")
                                        } else {
                                            val targetLocation = hashExitLocationFragment[locationID]
                                            productionName = hashExitProductionLine[locationID]!!
                                            targetLocation!!.arguments = setBundle(args)
                                            beginTransaction(targetLocation)
                                            editSpecialScanProductID.setText("")
                                        }
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                } else if (response.code() == 404) {
                                    Log.e("404", "Error 404")
                                    beginScanLocation()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                if (t.message != null) {
                                    Log.e("Error", t.message!!)
                                    beginScanLocation()
                                }
                            }
                        })
                    }
                    editSpecialScanProductID.length() > 9 -> editSpecialScanProductID.setText("")
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

    companion object {

        fun newInstance(): ShowWaitingListFragment {
            val fragment = ShowWaitingListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun beginTransaction(fm: Fragment) {
        fragmentManager!!.beginTransaction()
                .replace(R.id.contentContainer, fm)
                .addToBackStack("ShowWaitingList")
                .commit()
    }

    private fun setBundle(bd: Bundle): Bundle {
        bd.putString("LocationID", locationID)
        bd.putString("LocationName", locationName)
        bd.putString("ProductID", productID)
        bd.putString("ProductionName", productionName)
        return bd
    }

    private fun getBundle(bd: Bundle) {
        this.locationID = bd.getString("LocationID")!!
    }

    private fun beginScanLocation() {
        fragmentManager!!.popBackStack()
    }
}
