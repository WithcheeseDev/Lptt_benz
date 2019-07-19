package com.withcheese.lptt_benz.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.withcheese.lptt_benz.manager.ProductItemManager
import com.withcheese.lptt_benz.view.WaitingEvenItem

class WaitingProductEvenAdapter : BaseAdapter() {
    override fun getCount(): Int {
        if (ProductItemManager.getInstance().daoEven == null)
            return 0
        return if (ProductItemManager.getInstance().daoEven!!.product == null) 0 else ProductItemManager.getInstance().daoEven!!.product!!.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item: WaitingEvenItem = if (convertView != null)
            convertView as WaitingEvenItem
        else
            WaitingEvenItem(parent.context)

        val dao= ProductItemManager.getInstance().daoEven!!.product!![position]
        item.setText(dao.id)

        return item

    }
}
