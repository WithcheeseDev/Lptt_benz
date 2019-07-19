package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.withcheese.lptt_benz.R


/**
 * Created by nuuneoi on 11/16/2014.
 */
class FragmentTemplate : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
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

    companion object {

        fun newInstance(): FragmentTemplate {
            val fragment = FragmentTemplate()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
