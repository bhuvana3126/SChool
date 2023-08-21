package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import androidx.paging.PagedListAdapter
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wholesale.jewels.fauxiq.baheekhata.BR
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.expense.Expense
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.CheckoutBalance
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.CheckOutBalanceRecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.ExpenseRecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.utils.Utils
import com.wholesale.jewels.fauxiq.baheekhata.utils.toast
import kotlinx.android.synthetic.main.item_checkout_listing.*
import kotlinx.android.synthetic.main.item_checkout_listing.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckoutRecyclerViewPagedListAdapter(
    val mView: CheckOutBalanceRecyclerView
) : PagedListAdapter<CheckoutBalance, CheckoutRecyclerViewPagedListAdapter.ViewHolder>(CheckoutBalance.DIFF_CALLBACK
) {
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        mContext = parent.context

        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.item_checkout_listing, parent, false)
        return ViewHolder(view)
    }

    private val onClickCheckoutBalanceDelete = View.OnClickListener {
        mView.onClickDelete(it.tag as CheckoutBalance)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onClickCheckoutBalanceDelete,onListClickCheckoutBalanceDelete)
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

    inner class ViewHolder(var mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {

        fun bind(
            checkoutBalance: CheckoutBalance,
            onDeleteClickListener: View.OnClickListener,
            onListClickListener: View.OnClickListener
        ) {
            binding.setVariable(BR.checkoutBalance, checkoutBalance)
            binding.setVariable(BR.onDeleteListener, onDeleteClickListener)
            binding.setVariable(BR.onListListener, onListClickListener)
            binding.executePendingBindings()
        }

        val binding: ViewDataBinding = DataBindingUtil.bind(mView)!!
    }
    private val onListClickCheckoutBalanceDelete = View.OnClickListener {
        toast(mContext,"listClick")
       //mView.checkout_cardview.setCardBackgroundColor(Color.parseColor("#1d9c5a"))
        mView.onListClick(it.tag as CheckoutBalance)
    }
}
