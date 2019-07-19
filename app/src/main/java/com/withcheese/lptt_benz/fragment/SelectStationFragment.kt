package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.select_station_fragment.view.*
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class SelectStationFragment : Fragment(), View.OnClickListener {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var stationName: String
    private lateinit var outcomeStatus: String
    /* Views */
    private lateinit var btnSSRollerPit: Button
    private lateinit var btnSSUdbCheck: Button
    private lateinit var btnSSISTKEngine: Button
    private lateinit var btnSSHud: Button
    private lateinit var btnSSTestTrack: Button
    private lateinit var tvSSThisLocation: TextView
    private lateinit var tvSSProductID: TextView
    private lateinit var itemProcessSelectStation: ThisProcessItem
    /* Collections */
    private var listStation: MutableList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.select_station_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnSSISTKEngine = rootView.btnSSISTKEngine
        btnSSHud = rootView.btnSSHud
        btnSSRollerPit = rootView.btnSSRollerPit
        btnSSTestTrack = rootView.btnSSTestTrack
        itemProcessSelectStation = rootView.itemProcessSelectStation
        itemProcessSelectStation.setProcessItem(productID, productionName)

        listStation.add(btnSSISTKEngine)
        listStation.add(btnSSHud)
        listStation.add(btnSSRollerPit)
        listStation.add(btnSSTestTrack)

        for (station in listStation) {
            station.setOnClickListener(this)
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
            R.id.btnSSHud -> {
                val args = Bundle()
                val jobTaskFragment = JobTaskFragment.newInstance()
                jobTaskFragment.arguments = setBundle(args, "HUD", outcomeStatus, "HUD")
                beginTransaction(jobTaskFragment)
            }
            R.id.btnSSTestTrack -> {
                val args = Bundle()
                val jobTaskFragment = JobTaskFragment.newInstance()
                jobTaskFragment.arguments = setBundle(args, "TestTrack", outcomeStatus, "TestTrack")
                beginTransaction(jobTaskFragment)
            }
            R.id.btnSSISTKEngine -> {
                val args = Bundle()
                val jobTaskFragment = JobTaskFragment.newInstance()
                jobTaskFragment.arguments = setBundle(args, "EGC", outcomeStatus, "EGC")
                beginTransaction(jobTaskFragment)
            }
            R.id.btnSSRollerPit -> {
                val args = Bundle()
                val jobTaskFragment = JobTaskFragment.newInstance()
                jobTaskFragment.arguments = setBundle(args, "RRP", outcomeStatus, "RRP")
                beginTransaction(jobTaskFragment)
            }
        }
    }

    fun setBundle(bundle: Bundle, stationName: String, OutcomeStatus: String?, processTask: String): Bundle {
        bundle.putString("ProductID", this.productID)
        bundle.putString("LocationID", this.locationID)
        bundle.putString("LocationName", this.locationName)
        bundle.putString("ProductionName", this.productionName)
        bundle.putString("StationName", stationName)
        bundle.putString("OutcomeStatus", OutcomeStatus)
        bundle.putString("ProcessTask", processTask)
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

    companion object {

        fun newInstance(): SelectStationFragment {
            val fragment = SelectStationFragment()
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
            productionName = bundle.getString("ProductionName")!!
            stationName = bundle.getString("StationName")!!
            outcomeStatus = bundle.getString("OutcomeStatus")!!
        }
    }
}
