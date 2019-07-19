package com.withcheese.lptt_benz.dao.waitingProductDao

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class ProductItem {

    @SerializedName("location_name")
    var locationName: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("location_id")
    var locationId: String? = null

    @SerializedName("status")
    var status: String? = null

    override fun toString(): String {
        return "ProductItem{" +
                "location_name = '" + locationName + '\''.toString() +
                ",id = '" + id + '\''.toString() +
                ",location_id = '" + locationId + '\''.toString() +
                ",status = '" + status + '\''.toString() +
                "}"
    }
}