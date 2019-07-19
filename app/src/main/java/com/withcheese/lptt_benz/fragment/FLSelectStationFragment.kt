package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.dialog.NioDialog
import kotlinx.android.synthetic.main.fl_select_station_fragment.view.*
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class FLSelectStationFragment : Fragment(), View.OnClickListener {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var stationName: String
    /* Views */
    private lateinit var btnFQG : Button
    private lateinit var btnWSA : Button
    private lateinit var btnPreBuy : Button
    private lateinit var tvFLSsThisLocation: TextView
    private lateinit var tvFLSsProductID: TextView
    /* Collections */
    private var listStation: MutableList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args: Bundle? = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fl_select_station_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnFQG = rootView.btnFQG
        btnWSA = rootView.btnWSA
        btnPreBuy = rootView.btnPreBuy
        tvFLSsProductID = rootView.tvFLSsProductID
        tvFLSsThisLocation = rootView.tvFLSsThisLocation

        tvFLSsThisLocation.text = locationName
        tvFLSsProductID.text = productID

        listStation.add(btnFQG)
        listStation.add(btnWSA)
        listStation.add(btnPreBuy)

        for (station in listStation) {
            station.setOnClickListener(this)
        }
    }

    /*
     * Restore Instance State HereH
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            val nioDialog = NioDialog.newInstance()
            nioDialog.arguments = setBundle(locationID, locationName, productID, v.tag.toString(), "")
            if (fragmentManager != null) {
                nioDialog.show(fragmentManager!!, "NioDialog")
            }
        }
    }

    fun setBundle(locationId: String?, locationName: String?, productId: String?,
                 stationName: String, finishLocation: String): Bundle {
        val args = Bundle()
        args.putString("ProductID", productId)
        args.putString("LocationID", locationId)
        args.putString("LocationName", locationName)
        args.putString("StationName", stationName)
        args.putString("FinishLocation", finishLocation)
        return args
    }

    companion object {

        fun newInstance(): FLSelectStationFragment {
            val fragment = FLSelectStationFragment()
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
            when{
                bundle.getString("StationName") != null -> this.stationName = bundle.getString("StationName")!!
                else -> this.stationName = ""
            }
        }
    }
}
