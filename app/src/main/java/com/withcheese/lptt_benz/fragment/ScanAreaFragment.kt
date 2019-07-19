package com.withcheese.lptt_benz.fragment

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.inthecheesefactory.thecheeselibrary.manager.Contextor
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.scan_area_fragment.view.*
import java.util.*

/**
 * Created by nuuneoi on 11/16/2014.
 */
class ScanAreaFragment : Fragment() {
    /*--- Variables ---*/
    private lateinit var locationID: String
    private lateinit var arrLocationID: Array<String>
    private lateinit var locationUtils: LocationUtils
    private lateinit var wifiManager: WifiManager
    /*--- List's location id ---*/
    private lateinit var listLocationID: List<String>
    private lateinit var hashLocationId: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>
    private lateinit var hashLocationCode: HashMap<String, String>
    /*--- Views ---*/
    private lateinit var editBuildingID: EditText
    private lateinit var editAreaID: EditText
    private lateinit var btnLogin: Button
    /* Updater */
    private lateinit var appUpdater: AppUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*---  Get location utils ---*/
        locationUtils = LocationUtils.instance()
        /*--- Get list's location id ---*/
        listLocationID = locationUtils.listLocationID()
        /*--- Get location collections ---*/
        hashLocationId = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        hashLocationCode = locationUtils.hashLocationCode()
        wifiManager = Contextor.getInstance().context!!.getSystemService(Context.WIFI_SERVICE) as WifiManager
        onRecieve(wifiManager)
        appUpdater = AppUpdater(activity)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("toeyWithcheez1103", "Lptt_benz")
        appUpdater.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.scan_area_fragment, container, false)
        initInstances(rootView)
        return rootView
    }


    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        editBuildingID = rootView.editBuildingID
        editAreaID = rootView.editAreaID
        btnLogin = rootView.btnLogin
        /*--- Set focus to scan first ---*/
        editBuildingID.requestFocus()
        /*--- Handle scanning event ---*/
        editBuildingID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                when {
                    /*--- When incoming text equal 4 ---*/
                    s.length == 4 -> {
                        /*--- Get location id from scanning ---*/
                        locationID = s.toString()
                        /*--- Get corresponding location id from scanning ---*/
                        getCorrespondLocation()
                    }
                    /*--- When incoming text more than 5 ---*/
                    s.length > 5 -> {
                        /*--- Clear scanning input ---*/
                        s.clear()
                        editAreaID.setText("")
                    }
                }
            }
        })
        /*--- Log in into location ---*/
        btnLogin.setOnClickListener {
            when {
                /*---  If building && area don't empty ---*/
                editBuildingID.length() != 0 && editAreaID.length() != 0 -> {
                    val args = Bundle()
                    when (locationID) {
                        "EL-1", "EL-2", "SW-1", "SW-2" -> {
                            Log.e("LocationID", locationID)
                            val showWaitingListFragment = ShowWaitingListFragment.newInstance()
                            showWaitingListFragment.arguments = setBundle(args)
                            beginTransaction(showWaitingListFragment)
                            setEmpty()
                        }
                        else -> {
                            Log.e("P", "T")
                            val scanProdFragment = ScanProdFragment.newInstance()
                            scanProdFragment.arguments = setBundle(args)
                            beginTransaction(scanProdFragment)
                            setEmpty()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
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

    private fun setLocationCode(locationID: String) {
        arrLocationID = hashLocationCode[locationID]!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        editBuildingID.setText(arrLocationID[0])
        editAreaID.setText(arrLocationID[1])
    }

    private fun beginTransaction(targetFragment: Fragment) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, targetFragment)
                    .addToBackStack("ScanAreaFragment")
                    .commit()
        }
    }

    private fun setEmpty() {
        editAreaID.setText("")
        editBuildingID.setText("")
    }

    private fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("LocationID", locationID)
        return bundle
    }

    private fun getCorrespondLocation() {
        for (location in listLocationID) {
            if (location == locationID) {
                setLocationCode(location)
            }
        }
    }

    fun onRecieve(wifiManager: WifiManager) {
        val numberOfLevels = 5
        val wifiInfo = wifiManager.connectionInfo
        val level = WifiManager.calculateSignalLevel(wifiInfo.rssi, numberOfLevels)
        Log.e("Signal","Bars = $level")
        Toast.makeText(context, "$level",Toast.LENGTH_SHORT).show()
    }


    companion object {

        fun newInstance(): ScanAreaFragment {
            val fragment = ScanAreaFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}

