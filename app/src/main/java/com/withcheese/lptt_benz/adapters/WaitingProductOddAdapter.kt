package com.withcheese.lptt_benz.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.withcheese.lptt_benz.manager.ProductItemManager
import com.withcheese.lptt_benz.view.WaitingOddItem

class WaitingProductOddAdapter : BaseAdapter() {
    override fun getCount(): Int {
        if (ProductItemManager.getInstance().daoOdd == null)
            return 0
        return if (ProductItemManager.getInstance().daoOdd!!.product == null) 0 else ProductItemManager.getInstance().daoOdd!!.product!!.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item: WaitingOddItem = if (convertView != null)
            convertView as WaitingOddItem
        else {
            WaitingOddItem(parent.context)
        }

        val dao = ProductItemManager.getInstance().daoOdd!!.product!![position]
        item.setText(dao.id)
        return item
    }
}
