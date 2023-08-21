package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import android.app.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.dto.ToolbarContent
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.CheckoutBalance
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.AlertDialogInterface
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.CheckOutBalanceRecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.repo.CheckoutDbRepo
import com.wholesale.jewels.fauxiq.baheekhata.room.AppDatabase
import com.wholesale.jewels.fauxiq.baheekhata.ui.components.ProgressDialog
import com.wholesale.jewels.fauxiq.baheekhata.utils.AlertMessages
import com.wholesale.jewels.fauxiq.baheekhata.utils.gone
import com.wholesale.jewels.fauxiq.baheekhata.utils.toast
import kotlinx.android.synthetic.main.fragment_checkout_balance.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class CheckoutBalanceFragment : androidx.fragment.app.Fragment(),CheckOutBalanceRecyclerView {

    private var listener: OnFragmentInteractionListener? = null
    val database: AppDatabase by inject(AppDatabase.CURRENT)
    private lateinit var progressDialog: ProgressDialog
    private var checkoutListing: LiveData<PagedList<CheckoutBalance>>? = null
    private var mMode: Int = 0
    private lateinit var checkoutAdapter : CheckoutRecyclerViewPagedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mMode = it.getInt(CHECKOUT_BALANCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkoutAdapter = CheckoutRecyclerViewPagedListAdapter(this)

        return inflater.inflate(R.layout.fragment_checkout_balance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUi()
        getLastCheckoutBalance()
    }

    private fun getLastCheckoutBalance() {

        GlobalScope.launch(Dispatchers.IO) {
            database.checkOutDao.getLastCheckout(id = "C0003").also { println(it) }
        }
    }

    private fun iniUi() {
        search_checkout_list_search_with.setOnSearchButtonClickListener(View.OnClickListener {
            searchWith()
        })

        search_checkout_list_search_with.performSearchButtonClick()

        setEmptyView()
        setExpenseList()

    }
    private val observer = object : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkWhetherListEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkWhetherListEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkWhetherListEmpty()
        }
    }
    private fun checkWhetherListEmpty() {

        val empty: Boolean = checkoutAdapter.itemCount == 0
        empty_view_checkout_list.visibility = if (empty) View.VISIBLE else View.GONE
        checkout_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun setExpenseList() {
        checkout_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        checkout_list.adapter = checkoutAdapter
        checkoutAdapter.registerAdapterDataObserver(observer)
    }

    private fun setEmptyView() {

        val tap = ""
        val text = "\n$tap Empty View\n"

        val ss = SpannableString(text)

        val selectStocks = object : ClickableSpan() {
            override fun onClick(view: View) {
                (view as TextView).highlightColor = Color.TRANSPARENT
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.isFakeBoldText = true
            }
        }

        ss.setSpan(
            selectStocks,
            text.indexOf(tap),
            text.indexOf(tap) + tap.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        empty_view_checkout_list.gone()
        empty_view_checkout_list.text = ss
        empty_view_checkout_list.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun searchWith() {
        if (::progressDialog.isInitialized.not()) {
            progressDialog = ProgressDialog(context!!)
        }
        progressDialog.show()

        val text: String = search_checkout_list_search_with.text.toString()

        if (checkoutListing != null) {
            checkoutListing!!.removeObservers(this)
        }

        checkoutListing = CheckoutDbRepo.getCheckoutBalanceListing(database, "%$text%")

        checkoutListing!!.observe(this, Observer { pagedList ->
            checkoutAdapter.submitList(pagedList)
            checkoutAdapter.notifyDataSetChanged()
            progressDialog.dismiss()
        })
    }

    private lateinit var toolbarContent: ToolbarContent

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarContent = ViewModelProviders.of(activity!!).get(ToolbarContent::class.java)
        toolbarContent.addToBackStack(CLASS_SIMPLE_NAME)
        toolbarContent.setToolbar(CLASS_SIMPLE_NAME, true, false)

    }
    override fun onClickDelete(checkoutBalance: CheckoutBalance) {


        AlertMessages.alertDelete(
            context = context!!,
            title = "Delete ?",
            message = "Once deleted, cannot get back !",
            positiveButton = "YES",
            negativeButton = "NO",
            isCancellable = true,
            listener = object : AlertDialogInterface {
                override fun onProceed(dialog: Dialog, message: String) {
                    dialog.dismiss()
                    GlobalScope.launch(Dispatchers.Main) {
                        CheckoutDbRepo.delete(database, checkoutBalance)
                    }
                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()

    }

    override fun onListClick(checkoutBalance: CheckoutBalance) {
    }
        override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {

    }
    companion object {
        const val CLASS_SIMPLE_NAME: String = "CheckOut Balance"
        const val CHECKOUT_BALANCE = "CHECKOUT_BALANCE"

        @JvmStatic
        fun newInstance(mode:Int) =
            CheckoutBalanceFragment().apply {
                arguments = Bundle().apply {
                    putInt(CHECKOUT_BALANCE,mode)
                }
            }
    }
}
