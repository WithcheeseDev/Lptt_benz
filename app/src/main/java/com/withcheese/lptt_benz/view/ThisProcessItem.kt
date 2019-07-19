package com.withcheese.lptt_benz.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.view.state.BundleSavedState

/**
 * Created by nuuneoi on 11/16/2014.
 */
class ThisProcessItem : BaseCustomViewGroup {

    private lateinit var tvThisLocationName: TextView
    private lateinit var tvThisProductID: TextView

    constructor(context: Context) : super(context) {
        initInflate()
        initInstances()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initInflate()
        initInstances()
        initWithAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initInflate() {
        // var viewGroup : ViewGroup = View.inflate(context, R.layout.this_process_item,this) as ViewGroup
        View.inflate(context, R.layout.this_process_item, this)
    }

    private fun initInstances() {
        // findViewById here
        tvThisLocationName = findViewById(R.id.tvThisLocationName)
        tvThisProductID = findViewById(R.id.tvThisProductID)
    }

    private fun initWithAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

// Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return BundleSavedState(superState!!)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as BundleSavedState
        super.onRestoreInstanceState(ss.superState)

        val bundle = ss.bundle
        // Restore State from bundle here
    }

    fun setProcessItem(productID: String, locationName: String) {
        tvThisProductID.text = productID
        tvThisLocationName.text = locationName
    }

    fun setVisibleLocationName(state: Int?) {
        tvThisLocationName.visibility = state!!
    }

    fun setVisibleProductID(state: Int?) {
        tvThisProductID.visibility = state!!
    }
}
