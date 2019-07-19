package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.dao.ParkingArea.KeepingAreaStatus
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.view.RadioParkingZone
import kotlinx.android.synthetic.main.keeping_area_fragment.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class KeepingAreaFragment : Fragment(), RadioGroup.OnCheckedChangeListener{
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    /* Views*/
    private lateinit var btnKAConfirm: Button
    private lateinit var btnKACancel: Button
    private lateinit var tvKAThisArea: TextView
    private lateinit var tvKAProdCode: TextView
    private lateinit var tvBookingZone: TextView
    private lateinit var rdParkingZoneP: RadioParkingZone
    private lateinit var rdParkingZoneO: RadioParkingZone
    private lateinit var rdParkingZoneN: RadioParkingZone
    private lateinit var rdParkingZoneM: RadioParkingZone
    private lateinit var rdParkingZoneL: RadioParkingZone
    private lateinit var rdParkingZoneK: RadioParkingZone
    private lateinit var rdParkingZoneJ: RadioParkingZone
    private lateinit var rdParkingZoneI: RadioParkingZone
    private lateinit var rdParkingZoneH: RadioParkingZone
    private lateinit var rdParkingZoneG: RadioParkingZone
    private lateinit var rdParkingZoneF: RadioParkingZone
    private lateinit var rdParkingZoneE: RadioParkingZone
    private lateinit var rdParkingZoneD: RadioParkingZone
    private lateinit var rdParkingZoneC: RadioParkingZone
    private lateinit var rdParkingZoneB: RadioParkingZone
    private lateinit var rdParkingZoneA: RadioParkingZone
    /* Collections */
    private var hashParkingAreas = HashMap<String, RadioParkingZone>()
    private var hashParkingZones = HashMap<String, RadioGroup>()
    private var hashParkingLots = HashMap<String, RadioButton>()
    private var parkingTitle: MutableList<String> = ArrayList()
    private var listParkingArea: MutableList<RadioParkingZone> = ArrayList()
    private var listParkingZone: MutableList<RadioGroup> = ArrayList()
    private var listParkingLot: MutableList<RadioButton> = ArrayList()
    private lateinit var parkingZone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.keeping_area_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnKAConfirm = rootView.btnKAConfirm
        btnKACancel = rootView.btnKACancel
        tvKAProdCode = rootView.tvKAProdCode
        tvKAThisArea = rootView.tvKAThisArea
        tvBookingZone = rootView.tvBookingZone
        rdParkingZoneP = rootView.rdParkingZoneP
        rdParkingZoneO = rootView.rdParkingZoneO
        rdParkingZoneN = rootView.rdParkingZoneN
        rdParkingZoneM = rootView.rdParkingZoneM
        rdParkingZoneL = rootView.rdParkingZoneL
        rdParkingZoneK = rootView.rdParkingZoneK
        rdParkingZoneJ = rootView.rdParkingZoneJ
        rdParkingZoneI = rootView.rdParkingZoneI
        rdParkingZoneH = rootView.rdParkingZoneH
        rdParkingZoneG = rootView.rdParkingZoneG
        rdParkingZoneF = rootView.rdParkingZoneF
        rdParkingZoneE = rootView.rdParkingZoneE
        rdParkingZoneD = rootView.rdParkingZoneD
        rdParkingZoneC = rootView.rdParkingZoneC
        rdParkingZoneB = rootView.rdParkingZoneB
        rdParkingZoneA = rootView.rdParkingZoneA
        tvKAThisArea.text = productionName
        tvKAProdCode.text = productID
        btnKAConfirm.setOnClickListener {
            val keepingProduct =
                    HttpClient.getInstance().api.keepingVehicle(productID, tvBookingZone.text.toString(),
                            locationID, "false")
            keepingProduct.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    try {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                Log.e("Result", response.body()!!.string())
                                val goToKA =
                                        HttpClient.getInstance().api.goToKA(productID, locationName, locationID)
                                goToKA.enqueue(object : Callback<ResponseBody> {
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
                        } else if (response.code() == 404) {
                            Log.e("404", "Error 404")
                            beginScanProduct()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Result", t.message!!)
                    beginScanProduct()
                }
            })
        }
        btnKACancel.setOnClickListener{
            beginPopStack()
        }

        parkingTitle.add(0, "A")
        parkingTitle.add(1, "B")
        parkingTitle.add(2, "C")
        parkingTitle.add(3, "D")
        parkingTitle.add(4, "E")
        parkingTitle.add(5, "F")
        parkingTitle.add(6, "G")
        parkingTitle.add(7, "H")
        parkingTitle.add(8, "I")
        parkingTitle.add(9, "J")
        parkingTitle.add(10, "K")
        parkingTitle.add(11, "L")
        parkingTitle.add(12, "M")
        parkingTitle.add(13, "N")
        parkingTitle.add(14, "O")
        parkingTitle.add(15, "P")

        listParkingArea.add(0, rdParkingZoneA)
        listParkingArea.add(1, rdParkingZoneB)
        listParkingArea.add(2, rdParkingZoneC)
        listParkingArea.add(3, rdParkingZoneD)
        listParkingArea.add(4, rdParkingZoneE)
        listParkingArea.add(5, rdParkingZoneF)
        listParkingArea.add(6, rdParkingZoneG)
        listParkingArea.add(7, rdParkingZoneH)
        listParkingArea.add(8, rdParkingZoneI)
        listParkingArea.add(9, rdParkingZoneJ)
        listParkingArea.add(10, rdParkingZoneK)
        listParkingArea.add(11, rdParkingZoneL)
        listParkingArea.add(12, rdParkingZoneM)
        listParkingArea.add(13, rdParkingZoneN)
        listParkingArea.add(14, rdParkingZoneO)
        listParkingArea.add(15, rdParkingZoneP)

        for (i in listParkingArea.indices) {
            listParkingArea[i].areaTitle = parkingTitle[i]
            listParkingArea[i].setParkingLotTitles(parkingTitle[i])
            listParkingArea[i].setParkingAreaTag(parkingTitle[i])
        }
        for (i in listParkingArea.indices) {
            hashParkingAreas[parkingTitle[i]] = listParkingArea[i]
        }
        for (ParkingAreas in listParkingArea) {
            for (radioGroup in ParkingAreas.parkingZone) {
                hashParkingZones[radioGroup.tag.toString()] = radioGroup
                listParkingZone.add(radioGroup)
            }
            for (radioButton in ParkingAreas.parkingLot) {
                hashParkingLots[radioButton.text.toString()] = radioButton
                listParkingLot.add(radioButton)
            }
        }
        for (parkingZone in listParkingZone) {
            parkingZone.setOnCheckedChangeListener(this)
        }

        val getParkingStatus = HttpClient.getInstance().api.parkingStatus
        getParkingStatus.enqueue(object : Callback<KeepingAreaStatus> {
            override fun onResponse(call: Call<KeepingAreaStatus>, response: Response<KeepingAreaStatus>) {
                if (response.isSuccessful) {
                    val keepingAreaStatus = response.body()
                    for ((i, parkingLot) in listParkingLot.withIndex()) {
                        val parkingLotTitle = keepingAreaStatus!!.data!!.parkingLot!![i].id
                        val parkingLotStatus = keepingAreaStatus.data!!.parkingLot!![i].parkingStatus
                        hashParkingLots[parkingLotTitle]!!.isEnabled = java.lang.Boolean.parseBoolean(parkingLotStatus)
                    }
                } else if (response.code() == 404) {
                    Log.e("404", "Error 404")
                    beginScanProduct()
                }
            }

            override fun onFailure(call: Call<KeepingAreaStatus>, t: Throwable) {

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

    private fun uncheckAnotherZone(parkingZoneList: List<RadioParkingZone>, selected: RadioParkingZone, clearFirstZone: Boolean?) {
        for (parkingZone in parkingZoneList) {
            if (parkingZone.id == selected.id)
                selected.clearFirstZone(clearFirstZone!!)
            else
                parkingZone.clearChecked()
        }
        //Log.e("Selected Parking Zone", Integer.toString(selected.getId()));
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        val thisParkingLot = group.findViewById<RadioButton>(checkedId)
        val thisParkingArea = group.parent.parent as RadioParkingZone
        if (thisParkingLot != null && thisParkingLot.isChecked) {
            val firstOrSecond = group.tag.toString()[0]
            var clearFirstZone = false
            clearFirstZone = firstOrSecond != 'F'
            //        Log.e("ParkingArea", thisParkingArea.getAreaTitle());
            uncheckAnotherZone(listParkingArea, thisParkingArea, clearFirstZone)
            tvBookingZone.text = thisParkingLot.text
        }
    }

    companion object {

        fun newInstance(): KeepingAreaFragment {
            val fragment = KeepingAreaFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            locationName = bundle.getString("LocationName")!!
            productID = bundle.getString("ProductID")!!
            locationID = bundle.getString("LocationID")!!
            productionName = bundle.getString("ProductionName")!!
        }
    }

    fun beginScanProduct() {
        fragmentManager!!.popBackStack("ScanProductFragment", 1)
    }

    fun beginPopStack(){
        fragmentManager!!.popBackStack()
    }
}
