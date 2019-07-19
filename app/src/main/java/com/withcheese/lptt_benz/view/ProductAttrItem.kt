package com.withcheese.lptt_benz.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState
import com.withcheese.lptt_benz.R

/**
 * Created by nuuneoi on 11/16/2014.
 */
class ProductAttrItem : BaseCustomViewGroup {

    lateinit var tvProductAttrTitle: TextView
    lateinit var tvProductAttrDetail: TextView

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
        View.inflate(context, R.layout.product_attr_item, this)
    }

    private fun initInstances() {
        // findViewById here
        tvProductAttrTitle = findViewById(R.id.tvProductAttrTitle)
        tvProductAttrDetail = findViewById(R.id.tvProductAttrDetail)
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

        return BundleSavedState(superState)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as BundleSavedState
        super.onRestoreInstanceState(ss.superState)

        val bundle = ss.bundle
        // Restore State from bundle here
    }

    fun setTvProductAttrTitle(title: String?) {
        tvProductAttrTitle.text = title
    }

    fun setTvProductAttrDetail(detail: String?) {
        tvProductAttrDetail.text = detail
    }
}
