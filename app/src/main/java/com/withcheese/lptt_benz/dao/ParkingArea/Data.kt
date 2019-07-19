package com.withcheese.lptt_benz.dao.ParkingArea

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class Data {

    @SerializedName("ParkingLot")
    var parkingLot: List<ParkingLotItem>? = null

    override fun toString(): String {
        return "Data{" +
                "parkingLot = '" + parkingLot + '\''.toString() +
                "}"
    }
}