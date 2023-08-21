package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import androidx.paging.PagedListAdapter
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wholesale.jewels.fauxiq.baheekhata.BR
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.karigar.ItemKarigarList
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.KarigarItemInterface
import com.wholesale.jewels.fauxiq.baheekhata.utils.Utils
import kotlinx.android.synthetic.main.item_karigar_account_summary_invoice.view.*


class CloseItemListRecyclerViewAdapter(
    private var view: KarigarItemInterface
) : PagedListAdapter<ItemKarigarList, CloseItemListRecyclerViewAdapter.ViewHolder>(ItemKarigarList.DIFF_CALLBACK
) {
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContext = parent.context

        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.item_karigar_account_summary_invoice, parent, false)
        return ViewHolder(view)
    }


    private val onClickKarigarImage = View.OnClickListener {
        if (it.tag != null) {
            Utils.showCustomerDialog(it.tag as Bitmap, context = mContext)
        }
    }

    private val onKarigarEditListener = View.OnClickListener {
        view.onClickKarigarEdit(it.tag as ItemKarigarList)
    }

    private val onKarigarReturnListener = View.OnClickListener {
        view.onClickKarigarReturn(it.tag as ItemKarigarList)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item,onClickKarigarImage, onKarigarEditListener, onKarigarReturnListener)
        }
            updateMargins(holder.mView, position)
    }

    private fun updateMargins(view: View, position: Int) {

        val layoutParams: androidx.recyclerview.widget.RecyclerView.LayoutParams = view.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams

        layoutParams.leftMargin = Utils.getPx(mContext, 8)
        layoutParams.rightMargin = Utils.getPx(mContext, 8)

        layoutParams.topMargin = when (isFirstItem(position)) {
            true -> Utils.getPx(mContext, 8)
            false -> Utils.getPx(mContext, 4)
        }

        layoutParams.bottomMargin = when (isLastItem(position)) {
            true -> Utils.getPx(mContext, 88)
            false -> Utils.getPx(mContext, 4)
        }

        view.layoutParams = layoutParams
    }

    private fun isFirstItem(position: Int): Boolean {
        return position == 0
    }

    private fun isLastItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    inner class ViewHolder(val mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {

        fun bind(
            itemKarigarList: ItemKarigarList,
            onClickKarigarImage: View.OnClickListener,
            onKarigarEditListener: View.OnClickListener,
            onKarigarReturnListener: View.OnClickListener
        ) {
            binding.setVariable(BR.itemKarigarList, itemKarigarList)
            binding.setVariable(BR.onImageClickListener, onClickKarigarImage)
            binding.setVariable(BR.onKarigarEditListener, onKarigarEditListener)
            binding.setVariable(BR.onKarigarReturnListener, onKarigarReturnListener)
            binding.executePendingBindings()
            if (itemKarigarList.open_close == 1) {
                if(itemKarigarList.state.id == "R"){
                    mView.return_icon.visibility = View.GONE
                }else {
                    mView.return_icon.visibility = View.VISIBLE
                }
            }
        }

        val binding: ViewDataBinding = DataBindingUtil.bind(mView)!!
    }
}
