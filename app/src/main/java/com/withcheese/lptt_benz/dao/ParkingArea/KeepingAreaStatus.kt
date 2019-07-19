package com.withcheese.lptt_benz.dao.ParkingArea

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class KeepingAreaStatus {

    @SerializedName("result")
    var result: String? = null

    @SerializedName("data")
    var data: Data? = null

    override fun toString(): String {
        return "KeepingAreaStatus{" +
                "result = '" + result + '\''.toString() +
                ",data = '" + data + '\''.toString() +
                "}"
    }
}