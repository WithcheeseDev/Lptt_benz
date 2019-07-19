package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.withcheese.lptt_benz.R


/**
 * Created by nuuneoi on 11/16/2014.
 */
class LoadingDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.loading_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        setBackgroundMatchParent()
        setBackgroundTransparentBackground()
        dialog.setCancelable(false)

        val fadeIn : Animation =  AnimationUtils.loadAnimation(rootView.context, R.anim.fade_in)
        rootView.startAnimation(fadeIn)

        dialog.setOnDismissListener {
            val fadeOut =  AnimationUtils.loadAnimation(rootView.context, R.anim.fade_out)
            rootView.startAnimation(fadeOut)
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

    private fun setBackgroundMatchParent() {
        if (dialog.window != null)
            dialog.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    private fun setBackgroundTransparentBackground() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {

        fun newInstance(): LoadingDialog {
            val fragment = LoadingDialog()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
