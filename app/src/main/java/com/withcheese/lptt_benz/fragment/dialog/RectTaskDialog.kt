package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.SelectLocationFragment
import com.withcheese.lptt_benz.manager.HttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by nuuneoi on 11/16/2014.
 */
class RectTaskDialog : DialogFragment(), View.OnClickListener {

    lateinit var productID: String
    lateinit var locationID: String
    lateinit var locationName: String
    private lateinit var productionName: String
    lateinit var stationName: String
    lateinit var outcomeStatus: String
    lateinit var processTask: String
    lateinit var EVENT_CODE: String
    lateinit var btnRectDialogCancel: Button
    lateinit var btnRectDialogInProgress: Button
    lateinit var btnRectDialogDone: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        getBundle(args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.rect_task_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        setBackgroundTransparent()
        setBackgroundMatchParent()

        btnRectDialogCancel = rootView.findViewById(R.id.btnRectDialogCancel)
        btnRectDialogInProgress = rootView.findViewById(R.id.btnRectDialogInProgress)
        btnRectDialogDone = rootView.findViewById(R.id.btnRectDialogDone)

        btnRectDialogCancel.setOnClickListener(this)
        btnRectDialogInProgress.setOnClickListener(this)
        btnRectDialogDone.setOnClickListener(this)
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
        when (v.id) {
            R.id.btnRectDialogCancel -> {
                beginDismiss()
            }
            R.id.btnRectDialogInProgress -> {
                val insertRectTask = HttpClient.getInstance().api.insertRectTask(productID, locationID, locationName,
                        "", stationName, processTask)
                insertRectTask.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        beginTransaction()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        beginTransaction()
                    }
                })
            }
            R.id.btnRectDialogDone -> {
                val args = Bundle()
                val selectLocationFragment = SelectLocationFragment.newInstance()
                selectLocationFragment.arguments = setBundle(args)
                beginDismiss()
                beginSelectLocation(selectLocationFragment)
            }
        }
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.productID = bundle.getString("ProductID")!!
            this.locationID = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!
            this.productionName = bundle.getString("ProductionName")!!
            this.stationName = bundle.getString("StationName")!!
            this.processTask = bundle.getString("ProcessTask")!!
        }
    }

    private fun setBundle(bundle: Bundle): Bundle {
        bundle.putString("ProductID", productID)
        bundle.putString("LocationID", locationID)
        bundle.putString("LocationName", locationName)
        bundle.putString("ProductionName", productionName)
        bundle.putString("StationName", stationName)
        bundle.putString("ProcessTask", processTask)
        return bundle
    }

    private fun beginTransaction() {
        if (fragmentManager != null && dialog != null) {
            dialog.dismiss()
            fragmentManager!!.popBackStack("ScanProductFragment", 1)
        }
    }

    private fun beginSelectLocation(fm: Fragment) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fm)
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun beginDismiss() {
        if (dialog != null)
            dialog.dismiss()
    }

    private fun setBackgroundTransparent() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setBackgroundMatchParent() {
        if (dialog.window != null)
            dialog.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    companion object {

        fun newInstance(): RectTaskDialog {
            val fragment = RectTaskDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
