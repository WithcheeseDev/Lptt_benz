package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.select_location_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class SelectLocationFragment : Fragment(), View.OnClickListener {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var stationName: String
    private lateinit var processTask: String
    private lateinit var outcomeStatus: String
    private lateinit var empID: String
    /* Views */
    private lateinit var btnRect2WA1: Button
    private lateinit var btnRect2WA2: Button
    private lateinit var btnRect2EOL: Button
    private lateinit var btnRect2SW: Button
    private lateinit var btnRect2FL: Button
    private lateinit var btnRect2KA: Button
    private lateinit var btnRect2OtherRect: Button
    private lateinit var btnRect2PAVoCA: Button
    private lateinit var itemProcessSelectLocation: ThisProcessItem
    /* Collections */
    private lateinit var locationUtils: LocationUtils
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>

    private var listLocation: MutableList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get location collections */
        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        /* Get arguments */
        val args = arguments
        getBundle(args!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.select_location_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnRect2WA1 = rootView.btnRect2WA1
        btnRect2WA2 = rootView.btnRect2WA2
        btnRect2EOL = rootView.btnRect2EOL
        btnRect2SW = rootView.btnRect2SW
        btnRect2FL = rootView.btnRect2FL
        btnRect2KA = rootView.btnRect2KA
        btnRect2OtherRect = rootView.btnRect2OtherRect
        btnRect2PAVoCA = rootView.btnRect2PAVoCA
        itemProcessSelectLocation = rootView.itemProcessSelectLocation
        itemProcessSelectLocation.setProcessItem(productID, productionName)

        listLocation.add(btnRect2WA1)
        listLocation.add(btnRect2WA2)
        listLocation.add(btnRect2EOL)
        listLocation.add(btnRect2SW)
        listLocation.add(btnRect2FL)
        listLocation.add(btnRect2KA)
        listLocation.add(btnRect2OtherRect)
        listLocation.add(btnRect2PAVoCA)

        for (location in listLocation) {
            location.setOnClickListener(this)
        }
        if (locationID == "02-R") {
            btnRect2WA1.visibility = View.GONE
            btnRect2WA2.visibility = View.GONE
            val params = btnRect2EOL.layoutParams as LinearLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            btnRect2EOL.layoutParams = params
            btnRect2OtherRect.text = "Return\nME/EE rect."
        }

        val getPreviousLocation =
                HttpClient.getInstance().api.getPreviousLocation(productID)
        getPreviousLocation.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                beginScanProduct()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when {
                    response.isSuccessful -> {
                        val preLocation = response.body()!!.string()
                        Log.e("PL", preLocation)
                        when (preLocation[0]) {
                            'W' -> {
                                btnRect2EOL.visibility = View.GONE
                                btnRect2SW.visibility = View.GONE
                                btnRect2FL.visibility = View.GONE
                            }
                            'E' -> {
                                btnRect2SW.visibility = View.GONE
                                btnRect2FL.visibility = View.GONE
                            }
                            'S' -> {
                                btnRect2FL.visibility = View.GONE
                            }
                            'P' -> {
                                btnRect2WA2.visibility = View.GONE
                                btnRect2WA1.visibility = View.GONE
                                btnRect2EOL.visibility = View.GONE
                                btnRect2SW.visibility = View.GONE
                                btnRect2FL.visibility = View.GONE
                            }
                        }
                    }
                    response.code() == 404 -> {
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnRect2WA1 -> {
                outcomeStatus = "I.O."
                val backToWA =
                        HttpClient.getInstance().api.backToWA(productID, locationID, locationName, "I.O.")
                backToWA.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        beginScanProduct()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        when {
                            response.isSuccessful -> {
                                beginScanProduct()
                            }
                            response.code() == 404 -> {
                                beginScanProduct()
                            }
                        }
                    }

                })
            }
            R.id.btnRect2WA2 -> {
                outcomeStatus = "I.O."
                transferProduct(productID, locationID, locationName,
                        hashLocationID["WA-4"], hashLocationName["WA-4"],
                        outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
            }
            R.id.btnRect2EOL -> {
                outcomeStatus = "I.O."
                transferProduct(productID, locationID, locationName,
                        hashLocationID["EL-1"], hashLocationName["EL-1"],
                        outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
            }
            R.id.btnRect2SW -> {
                outcomeStatus = "I.O."
                transferProduct(productID, locationID, locationName,
                        hashLocationID["SW-1"], hashLocationName["SW-1"],
                        outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
            }
            R.id.btnRect2FL -> {
                outcomeStatus = "I.O."
                transferProduct(productID, locationID, locationName,
                        hashLocationID["FL-1"], hashLocationName["FL-1"],
                        outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
            }
            R.id.btnRect2PAVoCA -> {
                outcomeStatus = "I.O."
                transferProduct(productID, locationID, locationName,
                        "PAVOCA", "PAVOCA",
                        outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
            }
            R.id.btnRect2KA -> {
                val args = Bundle()
                val keepingAreaFragment = KeepingAreaFragment.newInstance()
                keepingAreaFragment.arguments = setBundle(args)
                beginTransaction(keepingAreaFragment)
            }
            R.id.btnRect2OtherRect -> {
                if (locationID == "03-R") {
                    outcomeStatus = "n.I.O."
                    transferProduct(productID, locationID, locationName,
                            hashLocationID["02-R"], hashLocationName["02-R"],
                            outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
                } else if (locationID == "02-R") {
                    outcomeStatus = "n.I.O."
                    transferProduct(productID, locationID, locationName,
                            hashLocationID["03-R"], hashLocationName["03-R"],
                            outcomeStatus, hashLocationName[locationID], stationName, empID, processTask)
                }
            }
        }
    }

    private fun transferProduct(productID: String?, locationID: String?, locationName: String?, nextLocationID: String?,
                                nextLocationName: String?, outcomeStatus: String?, mainLocationName: String?,
                                nioStation: String?, empID: String?, processTask: String?) {
        val transferProduct =
                HttpClient.getInstance().api.transferProduct(productID, locationID, locationName,
                        nextLocationID, nextLocationName, outcomeStatus, mainLocationName, nioStation, empID, processTask)
        transferProduct.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    beginScanProduct()
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanProduct()
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

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.locationID = bundle.getString("LocationID")!!
            this.productID = bundle.getString("ProductID")!!
            this.locationName = bundle.getString("LocationName")!!
            this.productionName = bundle.getString("ProductionName")!!
            this.stationName = when {
                bundle.getString("StationName") != null -> this.stationName = bundle.getString("StationName")!!
                else -> this.stationName = ""
            }.toString()
            this.processTask = when {
                bundle.getString("ProcessTask") != null -> this.processTask = bundle.getString("ProcessTask")!!
                else -> this.processTask = ""
            }.toString()
            empID = if (bundle.getString("EmpID") != null)
                bundle.getString("EmpID")!!
            else
                ""
        }
    }

    private fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("ProductID", productID)
        bundle.putString("LocationID", locationID)
        bundle.putString("LocationName", locationName)
        bundle.putString("ProductionName", productionName)
        return bundle
    }

    private fun beginTransaction(fragment: Fragment) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun beginPopStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack("ScanProductFragment", 1)
        }
    }

    private fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }

    companion object {

        fun newInstance(): SelectLocationFragment {
            val fragment = SelectLocationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
