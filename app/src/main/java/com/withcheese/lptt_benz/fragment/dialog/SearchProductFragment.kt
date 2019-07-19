package com.withcheese.lptt_benz.fragment.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.fragment.ShowProductDetailFragment


/**
 * Created by nuuneoi on 11/16/2014.
 */
class SearchProductFragment : DialogFragment() {

    lateinit var editSearchProduct: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.search_product_dialog, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {
        // Init 'View' instance(s) with rootView.findViewById here
        setBackgroundTransparent()

        editSearchProduct = rootView.findViewById(R.id.editSearchProduct)
        editSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (editSearchProduct.length() == 7) {
                    val showProductDetailFragment = ShowProductDetailFragment.newInstance()
                    beginDismiss()
                    beginTransaction(showProductDetailFragment)
                }
            }
        })
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

    private fun setBackgroundTransparent() {
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun beginDismiss() {
        if (dialog != null)
            dialog.dismiss()
    }

    private fun beginTransaction(fm: Fragment) {
        if (fragmentManager != null) {
            fragmentManager!!.beginTransaction()
                    .replace(R.id.contentContainer, fm)
                    .addToBackStack(null)
                    .commit()
        }
    }

    companion object {

        fun newInstance(): SearchProductFragment {
            val fragment = SearchProductFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
