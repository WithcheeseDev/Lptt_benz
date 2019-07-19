package com.withcheese.lptt_benz.dao.waitingProductDao

import com.google.gson.annotations.SerializedName

import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
class ProductWaitingCollectionDao {

    @SerializedName("product")
    var product: List<ProductItem>? = null

    override fun toString(): String {
        return "Response{" +
                "product = '" + product + '\''.toString() +
                "}"
    }
}