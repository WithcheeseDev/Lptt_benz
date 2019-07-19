package com.withcheese.lptt_benz.view.state

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View.BaseSavedState

/**
 * Created by nuuneoi on 1/9/2016.
 */
class BundleSavedState : BaseSavedState {

    var bundle = Bundle()

    constructor(source: Parcel) : super(source) {
        bundle = source.readBundle(ClassLoader.getSystemClassLoader())!!
    }

    constructor(superState: Parcelable) : super(superState) {}

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeBundle(bundle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BaseSavedState> = object : Parcelable.Creator<BaseSavedState> {
            override fun createFromParcel(source: Parcel): BaseSavedState {
                return BundleSavedState(source)
            }

            override fun newArray(size: Int): Array<BaseSavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

}
