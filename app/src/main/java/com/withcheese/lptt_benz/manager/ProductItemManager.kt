package com.withcheese.lptt_benz.manager

import android.content.Context

import com.inthecheesefactory.thecheeselibrary.manager.Contextor
import com.withcheese.lptt_benz.dao.waitingProductDao.ProductWaitingCollectionDao

/**
 * Created by nuuneoi on 11/16/2014.
 */
class ProductItemManager private constructor() {

    private val mContext: Context
    var daoOdd: ProductWaitingCollectionDao? = null
    var daoEven: ProductWaitingCollectionDao? = null

    init {
        mContext = Contextor.getInstance().context
    }

    companion object {

        private var instance: ProductItemManager? = null

        fun getInstance(): ProductItemManager {
            if (instance == null)
                instance = ProductItemManager()
            return instance as ProductItemManager
        }
    }

}
