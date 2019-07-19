package com.withcheese.lptt_benz.dao.ParkingArea

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class ParkingLotItem {

    @SerializedName("TimeIn")
    var timeIn: String? = null

    @SerializedName("TimeOut")
    var timeOut: String? = null

    @SerializedName("ParkingStatus")
    var parkingStatus: String? = null

    @SerializedName("ProductID")
    var productID: String? = null

    @SerializedName("ID")
    var id: String? = null

    @SerializedName("From")
    var from: String? = null

    @SerializedName("To")
    var to: String? = null

    override fun toString(): String {
        return "ParkingLotItem{" +
                "timeIn = '" + timeIn + '\''.toString() +
                ",timeOut = '" + timeOut + '\''.toString() +
                ",parkingStatus = '" + parkingStatus + '\''.toString() +
                ",productID = '" + productID + '\''.toString() +
                ",iD = '" + id + '\''.toString() +
                ",from = '" + from + '\''.toString() +
                ",to = '" + to + '\''.toString() +
                "}"
    }
}