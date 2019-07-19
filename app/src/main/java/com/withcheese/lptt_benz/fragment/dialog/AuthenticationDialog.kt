package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.util.LocationUtils
import kotlinx.android.synthetic.main.authentication_task_dialog.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class AuthenticationDialog : DialogFragment(), TextWatcher {
    /* Vars */
    private lateinit var productId: String
    private lateinit var locationId: String
    private lateinit var locationName: String
    private lateinit var stationName: String
    private lateinit var outcomeStatus: String
    private lateinit var processTask: String
    private lateinit var eventCode: String
    private lateinit var empID: String
    /* Views */
    private lateinit var btnAuthCancel: Button
    private lateinit var btnAuthConfirm: Button
    private lateinit var editAuth1stPosBit: EditText
    private lateinit var editAuth2ndPosBit: EditText
    private lateinit var editAuth1stUserBit: EditText
    private lateinit var editAuth2ndUserBit: EditText
    private lateinit var editAuth3rdUserBit: EditText
    private lateinit var tvAuthLo2Lo: TextView
    private lateinit var tvAuthIoOrNio: TextView
    /* Collections */
    private lateinit var locationUtils: LocationUtils
    private lateinit var hashLocationID: HashMap<String, String>
    private lateinit var hashLocationName: HashMap<String, String>
    private var listEditBit: MutableList<EditText> = ArrayList()
    private var listEmp: MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get location collections */
        locationUtils = LocationUtils.instance()
        hashLocationID = locationUtils.hashLocationID()
        hashLocationName = locationUtils.hashLocationName()
        /* Get arguments */
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.authentication_task_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnAuthCancel = rootView.btnAuthCancel
        btnAuthConfirm = rootView.btnAuthConfirm
        tvAuthIoOrNio = rootView.tvAuthIoOrNio
        tvAuthLo2Lo = rootView.tvAuthLo2Lo
        editAuth1stPosBit = rootView.editAuth1stPosBit
        editAuth2ndPosBit = rootView.editAuth2ndPosBit
        editAuth1stUserBit = rootView.editAuth1stUserBit
        editAuth2ndUserBit = rootView.editAuth2ndUserBit
        editAuth3rdUserBit = rootView.editAuth3rdUserBit

        btnAuthCancel.setOnClickListener {
            beginDismiss()
        }
        btnAuthConfirm.setOnClickListener {
            val userInput: String = getEmpID()
            for (id in listEmp) {
                if (id == userInput) {
                    when (eventCode) {
                        "EL2SW" -> {
                            transferWithAuth(hashLocationID["SW-1"].toString(), hashLocationName["SW-1"].toString(),
                                    "I.O.", hashLocationName["EL-1"].toString(), "", getEmpID(), "")
                        }
                        "TT2RT" -> {
                            transferWithAuth(hashLocationID["03-R"].toString(), hashLocationName["03-R"].toString(),
                                    "n.I.O.", hashLocationName["EL-1"].toString(), "TestTrack", getEmpID(), processTask)
                        }
                        "EGC2RT" -> {
                            transferWithAuth(hashLocationID["03-R"].toString(), hashLocationName["03-R"].toString(),
                                    "n.I.O.", hashLocationName["EL-1"].toString(), "EGC", getEmpID(), processTask)
                        }
                        "HUD2RT" -> {
                            transferWithAuth(hashLocationID["03-R"].toString(), hashLocationName["03-R"].toString(),
                                    "n.I.O.", hashLocationName["EL-1"].toString(), "HUD", getEmpID(), processTask)
                        }
                        "RRP2RT" -> {
                            transferWithAuth(hashLocationID["03-R"].toString(), hashLocationName["03-R"].toString(),
                                    "n.I.O.", hashLocationName["EL-1"].toString(), "RRP", getEmpID(), processTask)
                        }
                        "SW2FL" -> {
                            transferWithAuth(hashLocationID["FL-1"].toString(), hashLocationName["FL-1"].toString(),
                                    "I.O.", hashLocationName["SW-1"].toString(), "", getEmpID(), "")
                        }
                        "SW2RT" -> {
                            transferWithAuth(hashLocationID["03-R"], hashLocationName["03-R"].toString(),
                                    "n.I.O.", hashLocationName["SW-1"].toString(), "", getEmpID(), processTask)
                        }
                    }
                    break
                }
                else {
                    editAuth1stPosBit.setText("")
                    editAuth2ndPosBit.setText("")
                    editAuth1stUserBit.setText("")
                    editAuth2ndUserBit.setText("")
                    editAuth3rdUserBit.setText("")
                    editAuth1stPosBit.requestFocus()
                }
            }
        }

        setBackgroundTransparent()
        showKeyboard()
        setTargetLocation()
        empList()

        listEditBit.add(editAuth1stPosBit)
        listEditBit.add(editAuth2ndPosBit)
        listEditBit.add(editAuth1stUserBit)
        listEditBit.add(editAuth2ndUserBit)
        listEditBit.add(editAuth3rdUserBit)

        for (bit in listEditBit) {
            bit.addTextChangedListener(this)
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

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        if (editAuth1stPosBit.length() == 1)
            editAuth2ndPosBit.requestFocus()
        if (editAuth2ndPosBit.length() == 1)
            editAuth1stUserBit.requestFocus()
        if (editAuth1stUserBit.length() == 1)
            editAuth2ndUserBit.requestFocus()
        if (editAuth2ndUserBit.length() == 1)
            editAuth3rdUserBit.requestFocus()
    }

    private fun setTargetLocation() {
        when (locationName) {
            "End of Line" -> {
                when (outcomeStatus) {
                    "io" -> {
                        setProcessResult("io", "EL2SW", "EOL to Shower Test")
                    }
                    "nio" -> {
                        when (stationName) {
                            "TestTrack" -> {
                                setProcessResult("nio", "TT2RT", "Test Track to Rectification")
                            }
                            "EGC" -> {
                                setProcessResult("nio", "EGC2RT", "Engine Check to Rectification")
                            }
                            "HUD" -> {
                                setProcessResult("nio", "HUD2RT", "HUD calibration to Rectification")
                            }
                            "RRP" -> {
                                setProcessResult("nio", "RRP2RT", "Roller Pit to Rectification")
                            }
                        }
                    }
                }
            }
            "Shower test" -> {
                when (outcomeStatus) {
                    "io" -> {
                        setProcessResult("io", "SW2FL", "Shower to FNL")
                    }
                    "nio" -> {
                        setProcessResult("nio", "SW2RT", "Shower to Rectification")
                    }
                }
            }
        }
    }

    private fun transferWithAuth(nextLocationID: String?, nextLocationName: String?, outcomeStatus: String?,
                                 mainLocationName: String?, nioStation: String?, empID: String?, task: String?) {
        when (outcomeStatus) {
            "io" -> this.outcomeStatus = "I.O."
            "nio" -> this.outcomeStatus = "n.I.O."
        }

        val transferWithAuth =
                HttpClient.getInstance().api.transferProduct(this.productId, this.locationId, this.locationName,
                        nextLocationID, nextLocationName, outcomeStatus, mainLocationName, nioStation, empID, task)
        transferWithAuth.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                when{
                    response.isSuccessful -> {
                        beginPopStack()
                    }
                    response.code() == 404 -> {
                        beginPopStack()
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                beginPopStack()
            }
        })
    }

    private fun beginPopStack() {
        dialog.dismiss()
        fragmentManager!!.popBackStack("ShowWaitingList", 1)
    }

    private fun setProcessResult(outcomeStatus: String, eventCode: String, target: String) {
        if (outcomeStatus == "io")
            tvAuthIoOrNio.text = "I.O."
        else
            tvAuthIoOrNio.text = "n.I.O."
        tvAuthLo2Lo.text = target
        this.eventCode = eventCode
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.productId = bundle.getString("ProductID")!!
            this.locationId = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!
            when {
                bundle.getString("StationName") != null -> this.stationName = bundle.getString("StationName")!!
                else -> this.stationName = ""
            }
            when {
                bundle.getString("OutcomeStatus") != null -> this.outcomeStatus = bundle.getString("OutcomeStatus")!!
                else -> this.outcomeStatus = ""
            }
            when {
                bundle.getString("ProcessTask") != null -> this.processTask = bundle.getString("ProcessTask")!!
                else -> this.processTask = ""
            }
        }
    }

    private fun empList() {
        when (locationName) {
            "End of Line" -> {
                this.listEmp.add("03140")
                this.listEmp.add("03606")
                this.listEmp.add("03581")
                this.listEmp.add("03386")
                this.listEmp.add("03288")
            }
            "Shower test" -> {
                this.listEmp.add("03870")
                this.listEmp.add("03391")
                this.listEmp.add("03622")
                this.listEmp.add("03604")
            }
        }
    }

    companion object {

        fun newInstance(): AuthenticationDialog {
            val fragment = AuthenticationDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    private fun getEmpID(): String {
        val firstPosBit = editAuth1stPosBit.text.toString()
        val secondPosBit = editAuth2ndPosBit.text.toString()
        val firstUserBit = editAuth1stUserBit.text.toString()
        val secondUserBit = editAuth2ndUserBit.text.toString()
        val thirdUserBit = editAuth3rdUserBit.text.toString()
        empID = "$firstPosBit$secondPosBit$firstUserBit$secondUserBit$thirdUserBit"
        return empID
    }

    private fun setBackgroundTransparent() {
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showKeyboard() {
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun beginDismiss() {
        dialog.dismiss()
    }
}
