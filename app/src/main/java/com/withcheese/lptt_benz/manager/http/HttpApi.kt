package com.withcheese.lptt_benz.manager.http

import com.withcheese.lptt_benz.dao.ParkingArea.KeepingAreaStatus
import com.withcheese.lptt_benz.dao.ProductInfoDao.ProductInfoCollectionDao
import com.withcheese.lptt_benz.dao.waitingProductDao.ProductWaitingCollectionDao

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpApi {

    @get:GET("/product/showParkingLots")
    val parkingStatus: Call<KeepingAreaStatus>

    /****** ADD PRODUCT  */
    @GET("/product/addProduct")
    fun addProduct(
            @Query("product_id") productID: String?,
            @Query("location_id") locationID: String?,
            @Query("location_name") locationName: String?
    ): Call<ResponseBody>

    /****** IN PROGRESS PRODUCT  */
    @GET("/product/registerProduct")
    fun registerProduct(
            @Query("product_id") productID: String?,
            @Query("location_id") locationID: String?,
            @Query("location_name") locationName: String?
    ): Call<ResponseBody>

    /****** TRANSFER PRODUCT  */
    @GET("/product/transfer")
    fun transferProduct(
            @Query("product_id") productID: String?,
            @Query("location_id") locationID: String?,
            @Query("location_name") locationName: String?,
            @Query("next_location_id") nextLocationID: String?,
            @Query("next_location_name") nextLocationName: String?,
            @Query("outcome_status") outcomeStatus: String?,
            @Query("main_location_name") mainLocationName: String?,
            @Query("nio_station") nioStation: String?,
            @Query("emp_id") empID: String?,
            @Query("job_task") jobTask: String?
    ): Call<ResponseBody>

    @GET("/product/terminateProcess")
    fun terminateProcess(
            @Query("product_id") productID: String?,
            @Query("location_id") locationID: String?,
            @Query("terminated_location_id") terminatedLocationID: String?
    ): Call<ResponseBody>

    /****** GO TO KA  */
    @GET("/product/go2ka")
    fun goToKA(
            @Query("product_id") productID: String?,
            @Query("location_name") locationName: String?,
            @Query("base_location_id") baseLocationID: String?
    ): Call<ResponseBody>

    /****** INSERT RECT TASK PRODUCT  */
    @GET("/product/insertRectTask")
    fun insertRectTask(
            @Query("product_id") productID: String,
            @Query("location_id") locationID: String,
            @Query("location_name") locationName: String,
            @Query("to_location") toLocation: String,
            @Query("nio_station") nioStation: String,
            @Query("job_task") jobTask: String
    ): Call<ResponseBody>

    /***** GET PRODUCT STATUS  */
    @GET("/product/getProductStatus")
    fun getProductStatus(
            @Query("product_id") productID: String?,
            @Query("location_id") locationID: String?,
            @Query("location_name") locationName: String?
    ): Call<ResponseBody>

    /***** KEEPING PRODUCT  */
    @GET("/product/keepProduct")
    fun keepingVehicle(
            @Query("product_id") productId: String?,
            @Query("parking_id") parkingId: String?,
            @Query("from_station") fromStation: String?,
            @Query("parking_status") parkingStatus: String?
    ): Call<ResponseBody>

    @GET("/product/showWaitingItemEven")
    fun getWaitingItemsEven(
            @Query("location_name") locationName: String?
    ): Call<ProductWaitingCollectionDao>

    @GET("/product/showWaitingItemOdd")
    fun getWaitingItemsOdd(
            @Query("location_name") locationName: String?
    ): Call<ProductWaitingCollectionDao>


    @GET("/product/showSpecifyProduct")
    fun getProductInfo(
            @Query("product_id") productID: String
    ): Call<ProductInfoCollectionDao>

    @GET("/product/backToWA")
    fun backToWA(
            @Query("product_id") productID: String?,
            @Query("location_id") locationID: String?,
            @Query("location_name") locationName: String?,
            @Query("outcome_status") outcomeStatus: String?
    ): Call<ResponseBody>

    @GET("/product/getPreviousLocation")
    fun getPreviousLocation(
            @Query("product_id") productID: String?
    ): Call<ResponseBody>
}
