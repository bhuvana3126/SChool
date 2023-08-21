package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import androidx.paging.PagedListAdapter
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wholesale.jewels.fauxiq.baheekhata.BR
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.dto.report.PCSPPaymentListing
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.PCSPSummaryRecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.utils.Utils


class AccountSummaryRecyclerViewPagedListAdapter(
    private val mView: PCSPSummaryRecyclerView
) : PagedListAdapter<PCSPPaymentListing, AccountSummaryRecyclerViewPagedListAdapter.ViewHolder>(PCSPPaymentListing.DIFF_CALLBACK
) {
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContext = parent.context

        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.item_account_summary_invoice, parent, false)
        return ViewHolder(view)
    }
    private val onClickPurchaseItem = View.OnClickListener {
        mView.onClickPCSPItem(it.tag as PCSPPaymentListing)

    }

    private val onClickPurchaseEdit = View.OnClickListener {
        mView.onClickPCSPEdit(it.tag as PCSPPaymentListing)


    }

    private val onClickPurchaseDelete = View.OnClickListener {
        mView.onClickPCSPDelete(it.tag as PCSPPaymentListing)

    }

    private val onClickPurchasePay = View.OnClickListener {
        mView.onClickPCSPPay(it.tag as PCSPPaymentListing)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item,onClickPurchaseItem, onClickPurchaseEdit, onClickPurchaseDelete, onClickPurchasePay)
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
            accountsummary: PCSPPaymentListing,
            onPurchaseItemListener: View.OnClickListener,
            onPurchaseEditListener: View.OnClickListener,
            onPurchaseDeleteListener: View.OnClickListener,
            onPurchasePayListener: View.OnClickListener
        ) {
            binding.setVariable(BR.accountsummary, accountsummary)
            binding.setVariable(BR.onPurchaseItemListener, onPurchaseItemListener)
            binding.setVariable(BR.onPurchaseEditListener, onPurchaseEditListener)
            binding.setVariable(BR.onPurchaseDeleteListener, onPurchaseDeleteListener)
            binding.setVariable(BR.onPurchasePayListener, onPurchasePayListener)
            binding.executePendingBindings()
        }

        val binding: ViewDataBinding = DataBindingUtil.bind(mView)!!
    }
}
