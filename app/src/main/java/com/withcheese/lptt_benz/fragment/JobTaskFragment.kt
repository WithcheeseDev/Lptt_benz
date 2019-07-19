package com.withcheese.lptt_benz.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.dialog.AuthenticationDialog
import com.withcheese.lptt_benz.fragment.dialog.RectTaskDialog
import com.withcheese.lptt_benz.view.ThisProcessItem
import kotlinx.android.synthetic.main.job_task_fragment.view.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class JobTaskFragment : Fragment() {
    /* Vars */
    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var stationName: String
    private lateinit var outcomeStatus: String
    private lateinit var processTask: String
    private lateinit var eventCode: String
    /* Views */
    private lateinit var btnJobTaskCancel: Button
    private lateinit var btnJobTaskConfirm: Button
    private lateinit var tvJobTaskThisLocation: TextView
    private lateinit var tvJobTaskProductID: TextView
    private lateinit var tvStationJobTask: TextView
    private lateinit var editJobTaskDetail: EditText
    private lateinit var itemProcessJobTask: ThisProcessItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args!!)
        Log.e("STATION", stationName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.job_task_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnJobTaskCancel = rootView.btnJobTaskCancel
        btnJobTaskConfirm = rootView.btnJobTaskConfirm
        itemProcessJobTask = rootView.itemProcessJobTask
        tvStationJobTask = rootView.tvStationJobTask
        editJobTaskDetail = rootView.editJobTaskDetail
        itemProcessJobTask.setProcessItem(productID, productionName)
        setProcessTitle(processTask)
        btnJobTaskConfirm.setOnClickListener{
            val args = Bundle()
            if (eventCode == "Paint") {
                val dialog = RectTaskDialog.newInstance()
                dialog.arguments = setBundle(args)
                showDialog(dialog, "RectTaskDialog")
            } else {
                val dialog = AuthenticationDialog.newInstance()
                dialog.arguments = setBundle(args)
                showDialog(dialog, "AuthenticationDialog")
            }
        }
        btnJobTaskCancel.setOnClickListener{
            beginPopStack()
        }
        editJobTaskDetail.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editJobTaskDetail.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
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

        fun newInstance(): JobTaskFragment {
            val fragment = JobTaskFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setProcessTitle(processTask: String?) {
        when (locationName) {
            "Paint rect" -> {
                tvStationJobTask.text = "Progress information"
                eventCode = "Paint"
            }
            "End of Line" -> {
                when (processTask) {
                    "TestTrack" -> {
                        setTitleAndTag("Test Track fault information", "TT")
                    }
                    "EGC" -> {
                        setTitleAndTag("Engine Check fault information", "EGC")
                    }
                    "RRP" -> {
                        setTitleAndTag("Roller Pit fault information", "RRP")
                    }
                    "HUD" -> {
                        setTitleAndTag("HUD calibration fault information", "HUD")
                    }
                }
            }
            "Shower test" -> {
                setTitleAndTag("Shower Test fault information", "SWT")
            }
        }
    }

    fun setBundle(bundle: Bundle): Bundle {
        processTask = editJobTaskDetail.text.toString()
        bundle.putString("ProductID", productID)
        bundle.putString("LocationID", locationID)
        bundle.putString("LocationName", locationName)
        bundle.putString("ProductionName", productionName)
        bundle.putString("StationName", stationName)
        bundle.putString("OutcomeStatus", "nio")
        bundle.putString("ProcessTask", processTask)
        return bundle
    }

    private fun getBundle(bundle: Bundle) {
        this.locationID = bundle.getString("LocationID")!!
        this.locationName = bundle.getString("LocationName")!!
        this.productID = bundle.getString("ProductID")!!
        this.productionName = bundle.getString("ProductionName")!!
        when{
            bundle.getString("OutcomeStatus") != null -> this.outcomeStatus = bundle.getString("OutcomeStatus")!!
            else -> this.outcomeStatus = ""
        }
        when{
            bundle.getString("StationName") != null -> this.stationName = bundle.getString("StationName")!!
            else -> this.stationName = ""
        }
        when{
            bundle.getString("ProcessTask") != null -> this.processTask = bundle.getString("ProcessTask")!!
            else -> this.processTask = ""
        }
    }

    private fun showDialog(dialog: DialogFragment, tag: String) {
        if (fragmentManager != null) {
            dialog.show(fragmentManager!!, tag)
        }
    }

    private fun setTitleAndTag(title: String, tag: String) {
        tvStationJobTask.text = title
        eventCode = tag
    }

    private fun beginPopStack(){
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack()
        }
    }
}
