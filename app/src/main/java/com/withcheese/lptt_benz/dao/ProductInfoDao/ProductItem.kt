package com.withcheese.lptt_benz.dao.ProductInfoDao

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ProductItem(

	@field:SerializedName("comm_no")
	val commNo: String? = null,

	@field:SerializedName("german_prod_no")
	val germanProdNo: String? = null,

	@field:SerializedName("vin_no")
	val vinNo: String? = null,

	@field:SerializedName("engine_no")
	val engineNo: String? = null,

	@field:SerializedName("stay_location")
	val stayLocation: Any? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("trim_code")
	val trimCode: String? = null,

	@field:SerializedName("set_no")
	val setNo: String? = null,

	@field:SerializedName("pm")
	val pm: String? = null,

	@field:SerializedName("color_code")
	val colorCode: String? = null
)