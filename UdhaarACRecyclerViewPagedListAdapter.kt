package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import androidx.paging.PagedListAdapter
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wholesale.jewels.fauxiq.baheekhata.BR
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.dto.UdhaarSummaryForListing
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.UdhaarSummaryRecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.utils.Utils

class UdhaarACRecyclerViewPagedListAdapter(
    private val mView: UdhaarSummaryRecyclerView
) : PagedListAdapter<UdhaarSummaryForListing, UdhaarACRecyclerViewPagedListAdapter.ViewHolder>(UdhaarSummaryForListing.DIFF_CALLBACK) {

    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContext = parent.context
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_udhaar_while_listing, parent, false))
    }

    private val onClickUdhaarItem = View.OnClickListener {
        mView.onClickUdhaarItem(it.tag as UdhaarSummaryForListing)
    }

    private val onClickUdhaarEdit = View.OnClickListener {
        mView.onClickUdhaarEdit(it.tag as UdhaarSummaryForListing)
    }

    private val onClickUdhaarDelete = View.OnClickListener {
        mView.onClickUdhaarDelete(it.tag as UdhaarSummaryForListing)
    }

    private val onClickUdhaarPay = View.OnClickListener {
        mView.onClickUdhaarPay(it.tag as UdhaarSummaryForListing)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onClickUdhaarItem, onClickUdhaarEdit, onClickUdhaarDelete, onClickUdhaarPay)
        }

        val layoutParams: androidx.recyclerview.widget.RecyclerView.LayoutParams = holder.mView.layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams

        layoutParams.leftMargin = Utils.getPx(mContext, 8)
        layoutParams.rightMargin = Utils.getPx(mContext, 8)

        layoutParams.topMargin = when (isFirstItem(position)) {
            true -> Utils.getPx(mContext, 8)
            false -> Utils.getPx(mContext, 4)
        }

        layoutParams.bottomMargin = when (isLastItem(position)) {
            true -> Utils.getPx(mContext, 8)
            false -> Utils.getPx(mContext, 4)
        }

        holder.mView.layoutParams = layoutParams
    }

    private fun isFirstItem(position: Int): Boolean {
        return position == 0
    }

    private fun isLastItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    inner class ViewHolder(val mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {

        fun bind(
            itemUdhaar: UdhaarSummaryForListing,
            onUdhaarItemListener: View.OnClickListener,
            onUdhaarEditListener: View.OnClickListener,
            onUdhaarDeleteListener: View.OnClickListener,
            onUdhaarPayListener: View.OnClickListener
        ) {
            binding.setVariable(BR.itemUdhaar, itemUdhaar)
            binding.setVariable(BR.onUdhaarItemListener, onUdhaarItemListener)
            binding.setVariable(BR.onUdhaarEditListener, onUdhaarEditListener)
            binding.setVariable(BR.onUdhaarDeleteListener, onUdhaarDeleteListener)
            binding.setVariable(BR.onUdhaarPayListener, onUdhaarPayListener)
            binding.executePendingBindings()
        }

        val binding: ViewDataBinding = DataBindingUtil.bind(mView)!!
    }
}
