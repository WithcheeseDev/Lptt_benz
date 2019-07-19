package com.withcheese.lptt_benz.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.dialog.RectTaskDialog


/**
 * Created by nuuneoi on 11/16/2014.
 */
class RectTaskFragment : Fragment(), View.OnClickListener {

    private lateinit var productID: String
    private lateinit var locationID: String
    private lateinit var locationName: String
    private lateinit var productionName: String
    private lateinit var stationName: String
    private lateinit var task: String

    private lateinit var btnRectTaskConfirm: Button
    private lateinit var btnRectTaskCancel: Button
    private lateinit var tvRectTaskThisLocation: TextView
    private lateinit var tvRectTaskProductID: TextView
    private lateinit var tvRectTask: TextView
    private lateinit var editRectTaskDetail: EditText
    private lateinit var rdRectStation: RadioGroup
    private lateinit var rdRectTaskMechStation: RadioButton
    private lateinit var rdRectTaskEEStation: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments
        getBundle(args!!)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.rect_job_task_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        btnRectTaskCancel = rootView.findViewById(R.id.btnRectTaskCancel)
        btnRectTaskConfirm = rootView.findViewById(R.id.btnRectTaskConfirm)
        tvRectTaskProductID = rootView.findViewById(R.id.tvRectTaskProductID)
        tvRectTaskThisLocation = rootView.findViewById(R.id.tvRectTaskThisLocation)
        tvRectTask = rootView.findViewById(R.id.tvRectTask)
        editRectTaskDetail = rootView.findViewById(R.id.editRectTaskDetail)
        rdRectStation = rootView.findViewById(R.id.rdRectStation)
        rdRectTaskMechStation = rdRectStation.findViewById(R.id.rdRectTaskMechStation)
        rdRectTaskEEStation = rdRectStation.findViewById(R.id.rdRectTaskEEStation)

        stationName = "Mechanic"

        tvRectTaskThisLocation.text = locationName
        tvRectTaskProductID.text = productID

        rdRectStation.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rdRectTaskMechStation -> {
                    stationName = "Mechanic"
                    Log.e("STATION", stationName)
                }
                R.id.rdRectTaskEEStation -> {
                    stationName = "EE"
                    Log.e("STATION", stationName)
                }
            }
        }

        editRectTaskDetail.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editRectTaskDetail.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })

        btnRectTaskConfirm.setOnClickListener(this)
        btnRectTaskCancel.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
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
        if (v.id == R.id.btnRectTaskCancel) {
            if (fragmentManager != null) {
                fragmentManager!!.popBackStack()
            }
        }
        if (v.id == R.id.btnRectTaskConfirm) {
            task = editRectTaskDetail.text.toString()
            val args = Bundle()
            args.putString("ProductID", productID)
            args.putString("LocationID", locationID)
            args.putString("LocationName", locationName)
            args.putString("ProductionName", productionName)
            args.putString("StationName", stationName)
            args.putString("ProcessTask", task)
            val rectTaskDialog = RectTaskDialog.newInstance()
            rectTaskDialog.arguments = args
            if (fragmentManager != null) {
                rectTaskDialog.show(fragmentManager!!, "RectTaskDialog")
            }
        }
    }

    companion object {

        fun newInstance(): RectTaskFragment {
            val fragment = RectTaskFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getBundle(bd: Bundle): Bundle {
        this.productID = bd.getString("ProductID")!!
        this.locationID = bd.getString("LocationID")!!
        this.locationName = bd.getString("LocationName")!!
        this.productionName = bd.getString("ProductionName")!!
        return bd
    }
}
