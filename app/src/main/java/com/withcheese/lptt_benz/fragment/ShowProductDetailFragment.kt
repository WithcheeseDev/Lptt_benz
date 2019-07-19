package com.withcheese.lptt_benz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.withcheese.lptt_benz.R
import com.withcheese.lptt_benz.dao.ProductInfoDao.ProductInfoCollectionDao
import com.withcheese.lptt_benz.manager.HttpClient
import com.withcheese.lptt_benz.view.ProductAttrItem
import kotlinx.android.synthetic.main.show_product_detail_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by nuuneoi on 11/16/2014.
 */
class ShowProductDetailFragment : Fragment() {
    /* Vars */
    private lateinit  var locationID: String
    private lateinit  var locationName: String
    private lateinit var productID: String
    /* Views */
    lateinit var tvSearchThisLocation: TextView
    lateinit var tvSearchProductID: TextView
    lateinit var tvSearchGermanID: TextView
    lateinit var tvShowNowLocation: TextView
    lateinit var productAttrGermanProd: ProductAttrItem
    lateinit var productAttrVinNo: ProductAttrItem
    lateinit var productAttrCommNo: ProductAttrItem
    lateinit var productAttrEngineNo: ProductAttrItem
    lateinit var productAttrColorCode: ProductAttrItem
    lateinit var productAttrTrimCode: ProductAttrItem
    lateinit var productAttrPM: ProductAttrItem
    lateinit var productAttrSet: ProductAttrItem
    /* Collections */
    private var listProductAttrItem: MutableList<ProductAttrItem> = ArrayList()
    private var hashProductInfoTitle = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Get arguments */
        val args = arguments
        getBundle(args)
        Log.e("ProductID", productID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.show_product_detail_fragment, container, false)
        initInstances(rootView)
        return rootView
    }

    private fun initInstances(rootView: View) {

        // Init 'View' instance(s) with rootView.findViewById here
        tvSearchThisLocation = rootView.tvSearchThisLocation
        tvSearchProductID = rootView.tvSearchProductID
        tvSearchGermanID = rootView.tvSearchGermanID
        tvShowNowLocation = rootView.tvShowNowLocation
        productAttrGermanProd = rootView.productAttrGermanProd
        productAttrVinNo = rootView.productAttrVinNo
        productAttrCommNo = rootView.productAttrCommNo
        productAttrEngineNo = rootView.productAttrEngineNo
        productAttrColorCode = rootView.productAttrColorCode
        productAttrTrimCode = rootView.productAttrTrimCode
        productAttrPM = rootView.productAttrPM
        productAttrSet = rootView.productAttrSet
        tvSearchProductID.text = productID
        tvSearchThisLocation.text = locationName

        listProductAttrItem.add(productAttrGermanProd)
        listProductAttrItem.add(productAttrVinNo)
        listProductAttrItem.add(productAttrCommNo)
        listProductAttrItem.add(productAttrEngineNo)
        listProductAttrItem.add(productAttrColorCode)
        listProductAttrItem.add(productAttrTrimCode)
        listProductAttrItem.add(productAttrPM)
        listProductAttrItem.add(productAttrSet)

        hashProductInfoTitle[productAttrGermanProd.tag.toString()] = "German No."
        hashProductInfoTitle[productAttrVinNo.tag.toString()] = "VIN No."
        hashProductInfoTitle[productAttrCommNo.tag.toString()] = "Comm. No."
        hashProductInfoTitle[productAttrEngineNo.tag.toString()] = "Engine No."
        hashProductInfoTitle[productAttrColorCode.tag.toString()] = "Color No."
        hashProductInfoTitle[productAttrTrimCode.tag.toString()] = "Trim No."
        hashProductInfoTitle[productAttrPM.tag.toString()] = "PM"
        hashProductInfoTitle[productAttrSet.tag.toString()] = "Set"

        for (item in listProductAttrItem) {
            item.setTvProductAttrTitle(hashProductInfoTitle[item.tag.toString()])
        }

        val getProductInfo = HttpClient.getInstance().api.getProductInfo(productID)
        getProductInfo.enqueue(object : Callback<ProductInfoCollectionDao> {
            override fun onResponse(call: Call<ProductInfoCollectionDao>, response: Response<ProductInfoCollectionDao>) {
                if (response.isSuccessful) {
                    val dao = response.body()
                    if (dao != null) {
                        productAttrGermanProd.setTvProductAttrDetail(dao.product!![0]!!.germanProdNo)
                        productAttrVinNo.setTvProductAttrDetail(dao.product[0]!!.vinNo)
                        productAttrCommNo.setTvProductAttrDetail(dao.product[0]!!.commNo)
                        productAttrEngineNo.setTvProductAttrDetail(dao.product[0]!!.engineNo)
                        productAttrColorCode.setTvProductAttrDetail(dao.product[0]!!.colorCode)
                        productAttrTrimCode.setTvProductAttrDetail(dao.product[0]!!.trimCode)
                        productAttrPM.setTvProductAttrDetail(dao.product[0]!!.pm)
                        productAttrSet.setTvProductAttrDetail(dao.product[0]!!.setNo)
                        tvShowNowLocation.text = dao.product[0]!!.stayLocation.toString()
                        tvSearchGermanID.text = dao.product[0]!!.model.toString()
                    }
                } else if (response.code() == 404) {
                    if (response.errorBody() != null) {
                        Log.e("404", response.errorBody()!!.toString())
                        beginPopBackStack()
                    }
                }
            }

            override fun onFailure(call: Call<ProductInfoCollectionDao>, t: Throwable) {
                Log.e("Error", t.message!!)
                beginPopBackStack()
            }
        })
    }

    /*
     * Restore Instance State Here
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private fun getBundle(bundle: Bundle?) {
        if (bundle != null) {
            this.locationID = bundle.getString("LocationID")!!
            this.locationName = bundle.getString("LocationName")!!
            this.productID = bundle.getString("ProductID")!!
        }
    }

    private fun beginPopBackStack() {
        if (fragmentManager != null) {
            fragmentManager!!.popBackStack()
        }
    }

    companion object {

        fun newInstance(): ShowProductDetailFragment {
            val fragment = ShowProductDetailFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
