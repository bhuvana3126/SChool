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

import com.wholesale.jewels.fauxiq.baheekhata.dto.report.PaymentInfo
import com.wholesale.jewels.fauxiq.baheekhata.utils.Utils


class PaymentRecyclerViewPagedListAdapter(
) : PagedListAdapter<PaymentInfo, PaymentRecyclerViewPagedListAdapter.ViewHolder>(PaymentInfo.DIFF_CALLBACK
) {
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContext = parent.context

        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.item_payment_people_detail, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
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
            payment: PaymentInfo
        ) {
            binding.setVariable(BR.payment, payment)
            binding.executePendingBindings()
        }

        val binding: ViewDataBinding = DataBindingUtil.bind(mView)!!
    }
}
