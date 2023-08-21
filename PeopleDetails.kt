package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.databinding.BalanceItemCustomerFineWeightBinding
import com.wholesale.jewels.fauxiq.baheekhata.databinding.ItemCustomerFineWeightBinding
import com.wholesale.jewels.fauxiq.baheekhata.databinding.ViewpagerBalanceSummaryBinding
import com.wholesale.jewels.fauxiq.baheekhata.databinding.ViewpagerPeopleDetailsHomeBinding
import com.wholesale.jewels.fauxiq.baheekhata.dto.*
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.oldentry.OldEntry
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.payment.FineIO
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.payment.MoneyIO
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.payment.PurchasePayment
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.CheckoutBalance
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.People
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.PeopleBalanceConversion
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.PeoplesFineWeight
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.sales.SalePayment
import com.wholesale.jewels.fauxiq.baheekhata.dto.report.PCSPPaymentListing
import com.wholesale.jewels.fauxiq.baheekhata.dto.report.PaymentInfo
import com.wholesale.jewels.fauxiq.baheekhata.enums.*
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.*
import com.wholesale.jewels.fauxiq.baheekhata.print.Invoice
import com.wholesale.jewels.fauxiq.baheekhata.print.PrintInvoiceActivity
import com.wholesale.jewels.fauxiq.baheekhata.repo.*
import com.wholesale.jewels.fauxiq.baheekhata.room.AppDatabase
import com.wholesale.jewels.fauxiq.baheekhata.ui.components.ProgressDialog
import com.wholesale.jewels.fauxiq.baheekhata.ui.oldentry.oldentry_list.OldEntryRecyclerViewPagedListAdapter
import com.wholesale.jewels.fauxiq.baheekhata.ui.payment.FineIOFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.payment.MoneyIOFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.payment.fine_io_list.FineIORecyclerViewPagedListAdapter
import com.wholesale.jewels.fauxiq.baheekhata.ui.payment.money_io_list.MoneyIORecyclerViewPagedListAdapter
import com.wholesale.jewels.fauxiq.baheekhata.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_edit_stock.*
import kotlinx.android.synthetic.main.fragment_checkout_balance.*
import kotlinx.android.synthetic.main.fragment_people_details.*
import kotlinx.android.synthetic.main.item_checkout_listing.*
import kotlinx.android.synthetic.main.viewpager_account_summary.view.*
import kotlinx.android.synthetic.main.viewpager_balance_summary.view.*
import kotlinx.android.synthetic.main.viewpager_checkout_balance.view.*
import kotlinx.android.synthetic.main.viewpager_conversion_balance.view.*
import kotlinx.android.synthetic.main.viewpager_old_balance_record.view.*
import kotlinx.android.synthetic.main.viewpager_payment.view.*
import kotlinx.android.synthetic.main.viewpager_people_details_home.*
import kotlinx.android.synthetic.main.viewpager_people_details_home.view.*
import kotlinx.android.synthetic.main.viewpager_udhaar_account_summary.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.nio.charset.Charset

private const val ARG_CUSTOMER = "customer"

class CustomerDetails : androidx.fragment.app.Fragment(), CheckOutBalanceRecyclerView, OldEntryRecyclerView, UdhaarSummaryRecyclerView,PCSPSummaryRecyclerView,MoneyIORecyclerView,FineIORecyclerView {
    private lateinit var metalTypes: ArrayList<MetalType>
    private lateinit var mPeople: People
    private var checkoutBalance: CheckoutBalance = CheckoutBalance()
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewPagerAdapter: PeopleDetailsPagerAdapter
    private lateinit var checkoutAdapter: CheckoutRecyclerViewPagedListAdapter
    private var checkoutListing: LiveData<PagedList<CheckoutBalance>>? = null

    private lateinit var oldEntryAdapter: OldEntryRecyclerViewPagedListAdapter
    private var oldEntryListing: LiveData<PagedList<OldEntry>>? = null

    private lateinit var conversionAdapter: ConversionRecyclerViewPagedListAdapter
    private var conversionListing: LiveData<PagedList<PeopleBalanceConversion>>? = null

    private lateinit var accountSummaryAdapter: AccountSummaryRecyclerViewPagedListAdapter
    private var accountSummaryListing: LiveData<PagedList<PCSPPaymentListing>>? = null
    private var checkoutBalanceACList: CheckoutBalance? = null

    private lateinit var paymentMoneyIOAdapter: MoneyIORecyclerViewPagedListAdapter
    private lateinit var paymentFineIOAdapter: FineIORecyclerViewPagedListAdapter

    //private var paymentsSummaryListing: LiveData<PagedList<PaymentInfo>>? = null
    private var paymentsSummaryList: List<PaymentInfo>? = null

    private var purchasepayment: Double = 0.0
    private var purchasweight: SumPeopleFineBalance? = null
    private var balanceSummaryList: List<BalFineWeight>? = null

    private var peopleBalAmount: BalAmount? = null

    private lateinit var udhaarAcSummaryAdapter: UdhaarACRecyclerViewPagedListAdapter
    private var udhaarSummaryACList: LiveData<PagedList<UdhaarSummaryForListing>>? = null

    private var paymentsSummaryListing: LiveData<PagedList<MoneyIOForListing>>? = null
    private var paymentsFineIOSummaryListing: LiveData<PagedList<FineIOForListing>>? = null



    private var fromDate: Long = 0L
    private var toDate: Long = 0L


    val database: AppDatabase by inject(AppDatabase.CURRENT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPeople = it.getParcelable(ARG_CUSTOMER)!!
        }

    }

    private lateinit var binding: ViewpagerPeopleDetailsHomeBinding
    private lateinit var checkoutView: View
    private lateinit var oldEntryView: View
    private lateinit var conversionView: View
    private lateinit var accountSummaryView: View
    private lateinit var balanceSummaryView: View
    private lateinit var paymentView: View
    private lateinit var paymentFineIOView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_people_details, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // createMetalList()
        println("@@@fromDate")
        setViewPager()
        //getViewPagerDBData()
        // setCheckoutBalancePage()

    }

    private val onMobileNumberClickListener: View.OnClickListener = View.OnClickListener {
        if (it.tag != null && (it.tag as Long) != 0L) {
            Utils.dialPhoneNumber(
                context = context!!,
                phoneNumber = "+91${it.tag as Long}"
            )
        }
    }

    private val onAlterMobileNumberClickListener: View.OnClickListener = View.OnClickListener {
        if (it.tag != null && (it.tag as Long) != 0L) {
            Utils.dialPhoneNumber(
                context = context!!,
                phoneNumber = "+91${it.tag as Long}"
            )
        }
    }

    private val onEmailClickListener: View.OnClickListener = View.OnClickListener {
        if (it.tag != null && (it.tag as String).isNotEmpty()) {
            Utils.sendEmail(
                context = context!!,
                addresses = arrayOf(it.tag as String),
                subject = "Baheekhata: "
            )
        }
    }

    private val onEditCustomerClickListener: View.OnClickListener = View.OnClickListener {
        listener?.showCustomer(it.tag as People, this)
    }

    private val onConvertMoneyToFineClickListener: View.OnClickListener = View.OnClickListener {

        if (mPeople.getTotalAmount().isZero()) return@OnClickListener

        metalTypes = Metal.instance.metalTypes

        AlertMessages.messageWithEditTextAndSpinner(
            context = context!!,
            title = context!!.getString(
                R.string.rupee_xx_converted_to_wt_xx,
                mPeople.getTotalAmount().toString(2),
                0.0.toString(3)
            ),
            message = "Converting to fine",
            value = "Rate",
            metalTypes = metalTypes,
            positiveButton = "YES",
            negativeButton = "NO",
            isCancellable = true,
            listener = object : AlertDialogInterfaceWithMetalType {
                override fun onProceed(dialog: Dialog, message: String, metalType: MetalType) {
                    dialog.dismiss()

                    val progressDialog = ProgressDialog(context!!)
                    progressDialog.show()

                    val fine =
                        Formulas.calcConvertToFine(amount = mPeople.getTotalAmount(), rate = message.getDouble())

                    GlobalScope.launch(Dispatchers.Main) {

                        PeopleDbRepo.convertMoneyToFine(
                            database = database,
                            peopleId = mPeople.id,
                            metal = metalType.id,
                            amount = mPeople.getTotalAmount(),
                            rate = message.getDouble(),
                            result = fine
                        )

                        mPeople = PeopleDbRepo.get(database = database, peopleId = mPeople.id)

                        createMetalList(binding.root)
                        binding.customer = mPeople
                        binding.executePendingBindings()
                        progressDialog.dismiss()
                    }
                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            },
            textChangeListener = object : OnTextViewTextChangeListener {
                override fun onTextChange(view: TextView, text: CharSequence) {
                    val fine =
                        Formulas.calcConvertToFine(amount = mPeople.getTotalAmount(), rate = text.getDouble())
                    view.text = context!!.getString(
                        R.string.rupee_xx_converted_to_wt_xx,
                        mPeople.getTotalAmount().toString(2),
                        fine.toString(3)
                    )
                }
            }
        ).show()
    }

    private fun onConvertFineToMoneyClickListener(metal: String) {

        val fineWeight = mPeople.getFineWeight(metal = metal)

        if (fineWeight.isZero()) return

        AlertMessages.messageWithEditText(
            context = context!!,
            title = context!!.getString(
                R.string.wt_xx_converted_to_rupee_xx,
                fineWeight.toString(3),
                0.0.toString(2)
            ),
            message = "Converting to amount",
            value = "Rate",
            positiveButton = "YES",
            negativeButton = "NO",
            isCancellable = true,
            listener = object : AlertDialogInterface {
                override fun onProceed(dialog: Dialog, message: String) {
                    dialog.dismiss()

                    val progressDialog = ProgressDialog(context!!)
                    progressDialog.show()

                    val amount = Formulas.calcConvertToRate(fine = fineWeight, rate = message.getDouble())

                    GlobalScope.launch(Dispatchers.Main) {

                        PeopleDbRepo.convertFineToMoney(
                            database = database,
                            metal = metal,
                            peopleId = mPeople.id,
                            fine = fineWeight,
                            rate = message.getDouble(),
                            result = amount
                        )
                        mPeople = PeopleDbRepo.get(database = database, peopleId = mPeople.id)

                        createMetalList(binding.root)
                        binding.customer = mPeople
                        binding.executePendingBindings()
                        progressDialog.dismiss()
                    }
                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            },
            textChangeListener = object : OnTextViewTextChangeListener {
                override fun onTextChange(view: TextView, text: CharSequence) {
                    val amount = Formulas.calcConvertToRate(fine = fineWeight, rate = text.getDouble())
                    view.text = context!!.getString(
                        R.string.wt_xx_converted_to_rupee_xx,
                        fineWeight.toString(3),
                        amount.toString(2)
                    )
                }
            }
        ).show()
    }

    private val onAccountLockClickListener: View.OnClickListener = View.OnClickListener {

        AlertMessages.alert(
            context = context!!,
            title = "Are you sure ?",
            message = "Once locked, You cannot edit Weight and Amount as balance summary.",
            positiveButton = "YES",
            negativeButton = "NO",
            isCancellable = true,
            listener = object : AlertDialogInterface {
                override fun onProceed(dialog: Dialog, message: String) {
                    dialog.dismiss()

                    TransitionManager.beginDelayedTransition(customer_details_layout as ConstraintLayout)
                    customer_details_account_lock.visibility = View.GONE

                    GlobalScope.launch(Dispatchers.IO) {
                        mPeople.account_lock = true
                        PeopleDbRepo.lockAccount(database, mPeople)
                    }
                    checkOutBalance()
                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()
    }

    private val onSwitchActiveCustomer = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

        buttonView.setText(if (isChecked) R.string.active else R.string.inactive)
        buttonView.alpha = if (isChecked) 0.96f else 0.72f

        GlobalScope.launch(Dispatchers.IO) {
            PeopleDbRepo.switchCustomer(database, mPeople.id, isChecked)
        }
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

    private fun setViewPager() {

            viewPagerAdapter = PeopleDetailsPagerAdapter()

            println("@@count" + viewPagerAdapter.count)

            viewPagerAdapter.addView(createHomeView(), 0)
            // viewPagerAdapter.addView(balanceSummaryPage(), 1)
            viewPagerAdapter.addView(accountSummaryPage(), 1)
            viewPagerAdapter.addView(PaymentMoneyIOPage(), 2)
            viewPagerAdapter.addView(PaymentFineIOPage(), 3)
            viewPagerAdapter.addView(checkoutBalancePage(), 4)
            viewPagerAdapter.addView(conversionBalancePage(), 5)
            viewPagerAdapter.addView(oldBalanceRecordPage(), 6)

            viewpager_people_details.adapter = viewPagerAdapter

            viewpager_people_details.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p0: Int) {

                }
            })





    }

    private fun createHomeView(): View {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.viewpager_people_details_home,
            container,
            false
        )

        val myView = binding.root

        binding.customer = mPeople

        binding.onEditCustomerClickListener = onEditCustomerClickListener
        binding.onSwitchActiveCustomer = onSwitchActiveCustomer

        binding.onMobileNumberClickListener = onMobileNumberClickListener
        binding.onAlterMobileNumberClickListener = onAlterMobileNumberClickListener
        binding.onEmailClickListener = onEmailClickListener
        binding.onAccountLockClickListener = onAccountLockClickListener
        binding.onConvertMoneyToFineClickListener = onConvertMoneyToFineClickListener

        binding.executePendingBindings()

        createMetalList(binding.root)

        return myView
    }

    // Start Account Page
    private fun accountSummaryPage(): View {

        if (mPeople.people_kind.id == PeopleType.OTHER.id) {

            udhaarAcSummaryAdapter = UdhaarACRecyclerViewPagedListAdapter(this)
            accountSummaryView = LayoutInflater.from(context).inflate(R.layout.viewpager_udhaar_account_summary, null)

            GlobalScope.launch(Dispatchers.Main) {

                setUdhaarACSummaryPage()
            }

        } else {
            accountSummaryAdapter = AccountSummaryRecyclerViewPagedListAdapter(this)
            accountSummaryView = LayoutInflater.from(context).inflate(R.layout.viewpager_account_summary, null)

            GlobalScope.launch(Dispatchers.Main) {

                checkoutBalanceACList = CheckoutDbRepo.getAllCheckout(database = database, id = mPeople.id)
                //database.checkOutDao.getLastCheckout(id = mPeople.id)

                checkoutBalanceACList?.let {

                    println("@checkoutBalance" + checkoutBalanceACList)
                    fromDate = checkoutBalanceACList!!.checkout_date.startDateInMillis
                    //fromDate = DateUtils.currentDate.startDateInMillis
                    toDate = DateUtils.currentDate.endDateInMillis
                    println("@3")

                    //getpaylist()
                }


                setAccountSummaryPage()
            }


        }
        return accountSummaryView


    }

    private fun setAccountSummaryPage() {

        if (accountSummaryListing != null) {
            accountSummaryListing!!.removeObservers(this)
        }

        accountSummaryListing = ItemPurchaseDbRepo.getPCAccountSummaryPS(database, "%${mPeople.id}%", fromDate, toDate)

        println("@accountSummaryListing" + accountSummaryListing.toString())


        accountSummaryListing!!.observe(this, Observer { pagedList ->
            accountSummaryAdapter.submitList(pagedList)
            accountSummaryAdapter.notifyDataSetChanged()

        })
        //setEmptyView()
        setAccountSummaryList()
        println("@3")
    }

    private fun setAccountSummaryList() {

        accountSummaryView.account_summary_invoice_list.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        accountSummaryView.account_summary_invoice_list.adapter = accountSummaryAdapter
        accountSummaryAdapter.registerAdapterDataObserver(observer)

    }

    private suspend fun setUdhaarACSummaryPage() {

        if (udhaarSummaryACList != null) {
            udhaarSummaryACList!!.removeObservers(this)
        }

        udhaarSummaryACList = UdhaarDbRepo.udhaarAcSummaryListing(database = database, id = mPeople.id)

        udhaarSummaryACList!!.observe(this, Observer { pagedList ->
            udhaarAcSummaryAdapter.submitList(pagedList)
            udhaarAcSummaryAdapter.notifyDataSetChanged()

        })
        setUdhaarACSummaryList()
    }

    private fun setUdhaarACSummaryList() {

        accountSummaryView.udhaar_account_summary_invoice_list.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        accountSummaryView.udhaar_account_summary_invoice_list.adapter = udhaarAcSummaryAdapter
        udhaarAcSummaryAdapter.registerAdapterDataObserver(observer)

    }

    //End Account Page

    private fun balanceSummaryPage(): View {


        val amountBinding: ViewpagerBalanceSummaryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.viewpager_balance_summary, layout_new_stock_weights, false
        )


        val myView = amountBinding.root

        GlobalScope.launch(Dispatchers.Main) {

            if (mPeople.people_kind.id == PeopleType.CUSTOMER.id) {

                peopleBalAmount = ItemSaleDbRepo.getBalAmountSumFromSale(database = database, customerId = mPeople.id)
                amountBinding.balanceAmount = peopleBalAmount

                ItemSaleDbRepo.getBalFineWtSale(database = database, customerId = mPeople.id)
                    .filter {
                        it.fine_wt.isNotZero() || it.interest_fine_wt.isNotZero()
                    }
                    .also {
                        mPeople.balanceSumFineWeights = it as ArrayList<BalFineWeight>
                        binding.executePendingBindings()
                    }.forEach {

                        it.metal = getMetalTypes(it.metal_type)
                        getBalance(view = view!!, fineWeight = it)
                    }

            } else if (mPeople.people_kind.id == PeopleType.PARTY.id) {

                peopleBalAmount =
                    ItemPurchaseDbRepo.getBalAmountSumFromPurchase(database = database, customerId = mPeople.id)
                amountBinding.balanceAmount = peopleBalAmount

                ItemPurchaseDbRepo.getBalFineWtPurchase(database = database, customerId = mPeople.id)
                    .filter {
                        it.fine_wt.isNotZero() || it.interest_fine_wt.isNotZero()
                    }
                    .also {
                        mPeople.balanceSumFineWeights = it as ArrayList<BalFineWeight>
                        binding.executePendingBindings()
                    }.forEach {

                        it.metal = getMetalTypes(it.metal_type)
                        getBalance(view = view!!, fineWeight = it)
                    }

            } else if (mPeople.people_kind.id == PeopleType.KARIGAR.id) {

            } else {

            }


        }

        amountBinding.executePendingBindings()
        return myView


    }

    private fun getBalance(view: View, fineWeight: BalFineWeight) {

        val balanceBinding: BalanceItemCustomerFineWeightBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.balance_item_customer_fine_weight, layout_new_stock_weights, false
        )

        view.balance_metal_list.addView(balanceBinding.root)
        balanceBinding.metal = fineWeight

        balanceBinding.executePendingBindings()

        val layoutParams: LinearLayout.LayoutParams = balanceBinding.root.layoutParams as LinearLayout.LayoutParams
        layoutParams.leftMargin = Utils.getPx(context = context, dp = 8)
        layoutParams.rightMargin = Utils.getPx(context = context, dp = 8)
        layoutParams.topMargin = Utils.getPx(context = context, dp = 8)
        layoutParams.bottomMargin = Utils.getPx(context = context, dp = 8)

        balanceBinding.root.layoutParams = layoutParams

    }

    // Start Payment moenyio Page
    private fun PaymentMoneyIOPage(): View {

        paymentMoneyIOAdapter = MoneyIORecyclerViewPagedListAdapter(this,MoneyIORecyclerViewPagedListAdapter.Type.SECONDARY_ID_LISTING)
        paymentView = LayoutInflater.from(context).inflate(R.layout.viewpager_payment, null)

        GlobalScope.launch(Dispatchers.Main) {

            checkoutBalanceACList = CheckoutDbRepo.getAllCheckout(database = database, id = mPeople.id)

            checkoutBalanceACList?.let {

                //println("@checkoutBalance" + checkoutBalanceACList)
                fromDate = checkoutBalanceACList!!.checkout_date.startDateInMillis
                //fromDate = DateUtils.currentDate.startDateInMillis
                toDate = DateUtils.currentDate.endDateInMillis
                //println("@2")

            }
            setPaymetsMoneyIOSummaryPage()
        }

        return paymentView
    }

    private fun setPaymetsMoneyIOSummaryPage() {
        if (paymentsSummaryListing != null) {
            paymentsSummaryListing!!.removeObservers(this)
        }

        //paymentsSummaryListing = PaymentDbRepo.getAllPaymentSummary(database, "%${mPeople.id}%", fromDate, toDate)

        paymentsSummaryListing = PaymentDbRepo.peopleMoneyIOListing(database, "%${mPeople.id}%", fromDate, toDate)

        paymentsSummaryListing!!.observe(this, Observer { pagedList ->
            paymentMoneyIOAdapter.submitList(pagedList)
            paymentMoneyIOAdapter.notifyDataSetChanged()

        })

        setPaymentMoneyIOList()
    }

    private fun setPaymentMoneyIOList() {

        paymentView.payment_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        paymentView.payment_list.adapter = paymentMoneyIOAdapter
        paymentMoneyIOAdapter.registerAdapterDataObserver(observer)

    }
    // End Payment moneyio Page

    // Start Payment fineio Page
    private fun PaymentFineIOPage(): View {

        paymentFineIOAdapter = FineIORecyclerViewPagedListAdapter(this,FineIORecyclerViewPagedListAdapter.Type.SECONDARY_ID_LISTING)
        paymentFineIOView = LayoutInflater.from(context).inflate(R.layout.viewpager_fineio_payment, null)

        GlobalScope.launch(Dispatchers.Main) {

            checkoutBalanceACList = CheckoutDbRepo.getAllCheckout(database = database, id = mPeople.id)

            checkoutBalanceACList?.let {

                fromDate = checkoutBalanceACList!!.checkout_date.startDateInMillis
                //fromDate = DateUtils.currentDate.startDateInMillis
                toDate = DateUtils.currentDate.endDateInMillis

            }
            setPaymetsFineIOSummaryPage()
        }

        return paymentFineIOView
    }

    private fun setPaymetsFineIOSummaryPage() {
        if (paymentsFineIOSummaryListing != null) {
            paymentsFineIOSummaryListing!!.removeObservers(this)
        }

        //paymentsFineIOSummaryListing = PaymentDbRepo.getAllPaymentSummary(database, "%${mPeople.id}%", fromDate, toDate)

        paymentsFineIOSummaryListing = PaymentDbRepo.peopleFineIOListing(database, "%${mPeople.id}%", fromDate, toDate)

        paymentsFineIOSummaryListing!!.observe(this, Observer { pagedList ->
            paymentFineIOAdapter.submitList(pagedList)
            paymentFineIOAdapter.notifyDataSetChanged()

        })
        setPaymentFineIOList()
    }

    private fun setPaymentFineIOList() {

        paymentFineIOView.payment_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        paymentFineIOView.payment_list.adapter = paymentFineIOAdapter
        paymentFineIOAdapter.registerAdapterDataObserver(observer)

    }
    // End Payment fineio Page



    // Start Checkout Page
    private fun checkoutBalancePage(): View {

        checkoutAdapter = CheckoutRecyclerViewPagedListAdapter(this)
        checkoutView = LayoutInflater.from(context).inflate(R.layout.viewpager_checkout_balance, null)

        setCheckoutBalancePage()
        return checkoutView
    }

    private fun setCheckoutBalancePage() {

        if (checkoutListing != null) {
            checkoutListing!!.removeObservers(this)
        }

        checkoutListing = CheckoutDbRepo.getAllCheckoutForPeople(database, "%${mPeople.id}%")

        checkoutListing!!.observe(this, Observer { pagedList ->
            checkoutAdapter.submitList(pagedList)
            checkoutAdapter.notifyDataSetChanged()

        })

        //setEmptyView()
        setCheckoutBalanceList()

    }

    private fun setCheckoutBalanceList() {

        checkoutView.checkout_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        checkoutView.checkout_list.adapter = checkoutAdapter
        checkoutAdapter.registerAdapterDataObserver(observer)
    }
    // End Checkout Page

    // Start Conversion Page
    private fun conversionBalancePage(): View {

        conversionAdapter = ConversionRecyclerViewPagedListAdapter()
        conversionView = LayoutInflater.from(context).inflate(R.layout.viewpager_conversion_balance, null)

        setConversionBalancePage()
        return conversionView
    }

    //setConversionBalancePage

    private fun setConversionBalancePage() {

        if (conversionListing != null) {
            conversionListing!!.removeObservers(this)
        }

        conversionListing = PeopleDbRepo.getAllConversionForPeople(database, "%${mPeople.id}%")

        conversionListing!!.observe(this, Observer { pagedList ->
            conversionAdapter.submitList(pagedList)
            conversionAdapter.notifyDataSetChanged()

        })

        //setEmptyView()
        setConversionList()
    }

    private fun setConversionList() {

        conversionView.conversion_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        conversionView.conversion_list.adapter = conversionAdapter
        conversionAdapter.registerAdapterDataObserver(observer)
    }
    // End Conversion Page

    // Start OldBalance Page
    private fun oldBalanceRecordPage(): View {

        oldEntryAdapter = OldEntryRecyclerViewPagedListAdapter(this)
        oldEntryView = LayoutInflater.from(context).inflate(R.layout.viewpager_old_balance_record, null)

        setOldEntryPage()
        return oldEntryView
    }

    //OldEntryView

    private fun setOldEntryPage() {

        if (oldEntryListing != null) {
            oldEntryListing!!.removeObservers(this)
        }

        oldEntryListing = OldEntryDbRepo.getOldEntryListing(database, "%${mPeople.id}%")

        println("@olentryListing" + oldEntryListing.toString())


        oldEntryListing!!.observe(this, Observer { pagedList ->
            oldEntryAdapter.submitList(pagedList)
            oldEntryAdapter.notifyDataSetChanged()

        })

        //setEmptyView()
        setOldEntryList()
    }

    private fun setOldEntryList() {

        oldEntryView.old_entry_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context) as androidx.recyclerview.widget.RecyclerView.LayoutManager?
        oldEntryView.old_entry_list.adapter = oldEntryAdapter
        oldEntryAdapter.registerAdapterDataObserver(observer)
    }
    // End OldBalance Page

    private val observer = object : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (mPeople.people_kind.id == PeopleType.OTHER.id) {
                checkWhetherListEmptyUdhaarAClist()
            } else {
                checkWhetherListEmptyAClist()
            }
            checkWhetherListEmpty()
            checkWhetherOldEntryListEmpty()
            checkWhetherConversionListEmpty()
            checkWhetherListEmptyMoneyIOPaymentlist()
            checkWhetherListFineIOEmptyPaymentlist()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (mPeople.people_kind.id == PeopleType.OTHER.id) {
                checkWhetherListEmptyUdhaarAClist()
            } else {
                checkWhetherListEmptyAClist()
            }
            checkWhetherListEmpty()
            checkWhetherOldEntryListEmpty()
            checkWhetherConversionListEmpty()
            checkWhetherListEmptyMoneyIOPaymentlist()
            checkWhetherListFineIOEmptyPaymentlist()

        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (mPeople.people_kind.id == PeopleType.OTHER.id) {
                checkWhetherListEmptyUdhaarAClist()
            } else {
                checkWhetherListEmptyAClist()
            }
            checkWhetherListEmpty()
            checkWhetherOldEntryListEmpty()
            checkWhetherConversionListEmpty()
            checkWhetherListEmptyMoneyIOPaymentlist()
            checkWhetherListFineIOEmptyPaymentlist()

        }
    }

    private fun checkWhetherListEmpty() {

        val empty: Boolean = checkoutAdapter.itemCount == 0
        checkoutView.empty_view_checkout_list.visibility = if (empty) View.VISIBLE else View.GONE
        checkoutView.checkout_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun checkWhetherListEmptyAClist() {

        val empty: Boolean = accountSummaryAdapter.itemCount == 0
        accountSummaryView.empty_view_ac_list.visibility = if (empty) View.VISIBLE else View.GONE
        accountSummaryView.account_summary_invoice_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun checkWhetherListEmptyUdhaarAClist() {

        val empty: Boolean = udhaarAcSummaryAdapter.itemCount == 0
        accountSummaryView.udhaar_empty_view_ac_list.visibility = if (empty) View.VISIBLE else View.GONE
        accountSummaryView.udhaar_account_summary_invoice_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun checkWhetherListEmptyMoneyIOPaymentlist() {

        val empty: Boolean = paymentMoneyIOAdapter.itemCount == 0
        paymentView.empty_view_payment_list.visibility = if (empty) View.VISIBLE else View.GONE
        paymentView.payment_list.visibility = if (empty) View.GONE else View.VISIBLE
    }
    private fun checkWhetherListFineIOEmptyPaymentlist() {

        val empty: Boolean = paymentFineIOAdapter.itemCount == 0
        paymentFineIOView.empty_view_payment_list.visibility = if (empty) View.VISIBLE else View.GONE
        paymentFineIOView.payment_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun checkWhetherOldEntryListEmpty() {

        val empty: Boolean = oldEntryAdapter.itemCount == 0
        oldEntryView.empty_view_old_entry_list.visibility = if (empty) View.VISIBLE else View.GONE
        oldEntryView.old_entry_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun checkWhetherConversionListEmpty() {

        val empty: Boolean = conversionAdapter.itemCount == 0
        conversionView.empty_view_conversion_list.visibility = if (empty) View.VISIBLE else View.GONE
        conversionView.conversion_list.visibility = if (empty) View.GONE else View.VISIBLE
    }


    private lateinit var toolbarContent: ToolbarContent

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbarContent = ViewModelProviders.of(activity!!).get(ToolbarContent::class.java)
        toolbarContent.addToBackStack(CLASS_SIMPLE_NAME)
        toolbarContent.currentTitle.value = (CLASS_SIMPLE_NAME)
        toolbarContent.showToolbarDoneButton.value = false
        toolbarContent.showOptionsMenu.value = false
        toolbarContent.showBackButton.value = true
    }


    private fun createMetalList(view: View) {
        println("CustomerDetails.createMetalList")

        GlobalScope.launch(Dispatchers.Main) {

            view.metal_list.removeAllViews()

            /* metal_list?.let {
                 metal_list.removeAllViews()

             }*/

            PeopleDbRepo.getFineWt(database = database, customerId = mPeople.id)
                .filter {
                    it.fine_wt.isNotZero() || it.interest_fine_wt.isNotZero()
                }
                .also {
                    mPeople.fineWeights = it as ArrayList<PeoplesFineWeight>
                    binding.customer = mPeople
                    binding.executePendingBindings()
                }.forEach {

                    it.metal = getMetalTypes(it.metal_type)
                    addMetal(view = view, fineWeight = it)
                }
        }
    }

    private fun addMetal(view: View, fineWeight: PeoplesFineWeight) {

        val metalItemBinding: ItemCustomerFineWeightBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_customer_fine_weight,
            layout_new_stock_weights,
            false
        )
        view.metal_list.addView(metalItemBinding.root)

        metalItemBinding.metal = fineWeight

        metalItemBinding.onClick = View.OnClickListener { view ->
            onConvertFineToMoneyClickListener(view.tag as String)
        }
        metalItemBinding.executePendingBindings()

        val layoutParams: LinearLayout.LayoutParams = metalItemBinding.root.layoutParams as LinearLayout.LayoutParams
        layoutParams.leftMargin = Utils.getPx(context = context, dp = 8)
        layoutParams.rightMargin = Utils.getPx(context = context, dp = 8)
        layoutParams.topMargin = Utils.getPx(context = context, dp = 8)
        layoutParams.bottomMargin = Utils.getPx(context = context, dp = 8)

        metalItemBinding.root.layoutParams = layoutParams
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        toolbarContent.popBackStack()
        listener = null
    }

    fun updateCustomer(people: People) {
        mPeople = people
        view?.let {
            binding.customer = mPeople
            binding.executePendingBindings()
            createMetalList(binding.root)
        }
    }

    fun deleteCustomer() {

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
                        if (mPeople.account_lock.not()) {
                            PeopleDbRepo.delete(database, mPeople)
                            listener?.onBackPressed()
                        } else {
                            toast(context = context!!, message = "it's Locked! can't delete")
                        }
                    }
                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()


    }

    fun balanceSummaryPrint() {

        startActivityForResult(
            Intent(activity!!.applicationContext, PrintInvoiceActivity::class.java).apply {
                putExtra(PrintInvoiceActivity.BALANCE_SUMMARY_PRINT, mPeople)
                putExtra(PrintInvoiceActivity.INVOICE_TYPE, Invoice.invoices.peopleBalanceSummary.name)

            }, 101
        )
    }

    fun detailsBalanceSummaryPrint() {

        startActivityForResult(
            Intent(activity!!.applicationContext, PrintInvoiceActivity::class.java).apply {
                putExtra(PrintInvoiceActivity.DETAILS_BALANCE_SUMMARY_PRINT, mPeople)
                putExtra(PrintInvoiceActivity.INVOICE_TYPE, Invoice.invoices.peopleDetailsBalanceSummary.name)

            }, 101
        )
    }

    fun accountSummaryPrint() {
        println("Inside accountSummaryPrint")

        startActivityForResult(
            Intent(activity!!.applicationContext, PrintInvoiceActivity::class.java).apply {
                putExtra(PrintInvoiceActivity.ACCOUNT_SUMMARY_PRINT, mPeople)
                putExtra(PrintInvoiceActivity.INVOICE_TYPE, Invoice.invoices.peopleAccountSummary.name)

            }, 101
        )
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

                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()
    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         listener?.onBackPressed()
     }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_DATABASE_FILE_REQUEST_CODE -> {

                    if (data != null && data.data != null) {
                        println("data=$data")
                        println("dataData=" + data.data)

                        val split = data.data?.path?.split(":")

                        val path: String = if (split != null && split.size > 1) split[1] else "unknown path"
                        println("path=$path")

                        if (path.substring(path.length - 5) != ".json") {
                            toast(context = context!!, message = "Not a valid import file")
                        } else {

                            val list = path.split('/')

                            val message = "${list[list.size - 1]} is importing to ${mPeople.id}"
                            val spanString = SpannableString(message)
                            spanString.setSpan(StyleSpan(Typeface.BOLD), 0, list[list.size - 1].length, 0)
                            spanString.setSpan(
                                StyleSpan(Typeface.BOLD),
                                message.length - (mPeople.id.length ?: 0),
                                message.length,
                                0
                            )

                            AlertMessages.alert(
                                context = context!!,
                                title = "Are you sure ?",
                                message = message,
                                positiveButton = "YES",
                                negativeButton = "NO",
                                isCancellable = true,
                                listener = object : AlertDialogInterface {
                                    override fun onProceed(dialog: Dialog, message: String) {
                                        dialog.dismiss()

                                        /* DbHelper(activity!!).importDb(
                                             path = path,
                                             database = loginDetails.currentDatabaseName
                                         )*/
                                        importJSONOldData(path)
                                    }

                                    override fun onCancel(dialog: Dialog) {
                                        dialog.dismiss()
                                    }
                                },
                                span = spanString
                            ).show()

                        }
                    }
                }
            }
        }
    }

    private lateinit var progressDialog: ProgressDialog

    private fun importJSONOldData(path: String) {
        if (::progressDialog.isInitialized.not()) {
            progressDialog = ProgressDialog(context!!)
        }
        progressDialog.show()

        GlobalScope.launch(Dispatchers.IO) {

            OldEntryDbRepo.addOldEntry(
                database = database,
                oldEntry = getOldEntryData(context = context!!, path = path)
            )
            //PeopleDbRepo.addEditCustomer(database = database, people = getCustomers(context = context!!), type = PeopleType.CUSTOMER)

            launch(Dispatchers.Main) {
                progressDialog.dismiss()
                listener?.onBackPressed()
            }
        }

    }

    fun getOldEntryData(context: Context, path: String): ArrayList<OldEntry> {
        val entryList = ArrayList<OldEntry>()

        val array = JSONArray(loadJSONFromFile(context = context!!, path = path))

        println("###JSON:" + array.toString())
        for (d in 0 until array.length()) {
            addToList(array.getJSONObject(d), entryList)
        }

        return entryList
    }

    fun loadJSONFromFile(context: Context, path: String): String? {

        val dbFile = File(
            "${Environment.getExternalStorageDirectory()}${File.separator}$path"
        ).also { println("dbFile=$it") }
        val stream = FileInputStream(dbFile)
        var jString: String? = null
        try {

            val fc = stream.getChannel()
            val bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
            /* Instead of using default, pass in a decoder. */
            jString = Charset.defaultCharset().decode(bb).toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            stream.close()
        }

        return jString

    }

    private fun addToList(
        jsonObject: JSONObject,
        entryList: ArrayList<OldEntry>
    ) {

        if (mPeople.id.startsWith('P')) {
            entryList.add(
                OldEntry(
                    id = UNKNOWN,
                    people_id = mPeople.id,
                    old_people_name = jsonObject.getString("old_people_name"),
                    total_bill_fine = jsonObject.getDouble("total_bill_fine"),
                    balance_fine = jsonObject.getDouble("balance_fine"),
                    bill_number = jsonObject.getString("bill_number"),
                    bill_date = jsonObject.getLong("bill_date"),
                    total_bill_amount = jsonObject.getDouble("total_bill_amount"),
                    balance_amount = jsonObject.getDouble("balance_amount"),
                    date = Utils.getCurrentDate()

                )
            )
        } else {
            entryList.add(
                OldEntry(
                    id = UNKNOWN,
                    people_id = mPeople.id,
                    old_people_name = jsonObject.getString("old_people_name"),
                    total_bill_fine = jsonObject.getDouble("total_bill_fine").positive(),
                    balance_fine = jsonObject.getDouble("balance_fine").positive(),
                    bill_number = jsonObject.getString("bill_number"),
                    bill_date = jsonObject.getLong("bill_date"),
                    total_bill_amount = jsonObject.getDouble("total_bill_amount").positive(),
                    balance_amount = jsonObject.getDouble("balance_amount").positive(),
                    date = Utils.getCurrentDate()

                )
            )
        }


    }

    fun checkOutBalance() {
        checkoutBalance.fine_wt = mPeople.getTotalWeight()
        checkoutBalance.amount = mPeople.getTotalAmount()
        checkoutBalance.people_id = mPeople.id
        checkoutBalance.people_kind = mPeople.people_kind
        checkoutBalance.checkout_date = DateUtils.currentDate

        GlobalScope.launch(Dispatchers.IO) {
            CheckoutDbRepo.addCheckoutCustomer(
                database = database, checkoutBalance = checkoutBalance
            )
            println("" + checkoutBalance.fine_wt)

        }
        toast(context!!, "Checkout All Balance")
    }

    fun importOldData() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        activity!!.startActivityForResult(intent, PICK_DATABASE_FILE_REQUEST_CODE)
        toast(context!!, "import old data")
    }


    override fun onClickEdit(oldEntry: OldEntry) {
    }

    override fun onClickDelete(oldEntry: OldEntry) {
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

                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()
    }

    override fun onClickUdhaarItem(itemUdhaar: UdhaarSummaryForListing) {
        listener?.udhaarDetails(itemUdhaar)
    }

    override fun onClickUdhaarEdit(itemUdhaar: UdhaarSummaryForListing) {
        listener?.editUdhaar(oldUdhaarSummary = itemUdhaar)
    }

    override fun onClickUdhaarDelete(itemUdhaar: UdhaarSummaryForListing) {
        progressDialog.show()

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
                        //            CommonFunctionsDbRepo.deleteUdhaar(database = database, udhaarSummary = itemUdhaar)
                        progressDialog.dismiss()
                    }
                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()

    }

    override fun onClickUdhaarPay(itemUdhaar: UdhaarSummaryForListing) {
        listener?.udhaarPay(udhaar = itemUdhaar)
    }

    //purchasebookpay
    override fun onClickPCSPItem(itemPCSP: PCSPPaymentListing) {
        if (itemPCSP.id.startsWith("S"))  {
            println("S")

            val saleSummaryForListing: SaleSummaryForListing = itemPCSP.getsaleSummaryForListing()
            listener?.saleDetails(saleSummaryForListing)
        }else {
            println("P")

            val purchaseSummaryForListing: PurchaseSummaryForListing = itemPCSP.getpurchaseSummaryForListing()
            listener?.purchaseDetails(purchaseSummaryForListing)
        }
    }

    override fun onClickPCSPEdit(itemPCSP: PCSPPaymentListing) {
        /*if (mPeople.people_kind.id == PeopleType.CUSTOMER.id) {

            val saleSummaryForListing: SaleSummaryForListing = itemPCSP.getsaleSummaryForListing()
            listener?.editSale(oldSaleSummary = saleSummaryForListing)

        }else{

            val purchaseSummaryForListing: PurchaseSummaryForListing = itemPCSP.getpurchaseSummaryForListing()
            listener?.editPurchase(oldPurchaseSummary = purchaseSummaryForListing)
        }*/
        if (itemPCSP.id.startsWith("S")) {
            println("S")
            val saleSummaryForListing: SaleSummaryForListing = itemPCSP.getsaleSummaryForListing()
            listener?.editSale(oldSaleSummary = saleSummaryForListing)

        }else{
            println("P")
            val purchaseSummaryForListing: PurchaseSummaryForListing = itemPCSP.getpurchaseSummaryForListing()
            listener?.editPurchase(oldPurchaseSummary = purchaseSummaryForListing)
        }
    }

    override fun onClickPCSPDelete(itemPCSP: PCSPPaymentListing) {

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

                    if (itemPCSP.id.startsWith("S"))  {
                        val progressDialog = ProgressDialog(context!!)
                        progressDialog.show()
                        val saleSummaryForListing: SaleSummaryForListing = itemPCSP.getsaleSummaryForListing()
                        GlobalScope.launch(Dispatchers.Main) {
                            CommonFunctionsDbRepo.deleteSale(database = database, saleSummary = saleSummaryForListing)
                            progressDialog.dismiss()
                        }
                    }else{
                        val progressDialog = ProgressDialog(context!!)
                        progressDialog.show()
                        val purchaseSummaryForListing: PurchaseSummaryForListing = itemPCSP.getpurchaseSummaryForListing()
                        GlobalScope.launch(Dispatchers.Main) {
                            CommonFunctionsDbRepo.deletePurchase(database = database, purchaseSummary = purchaseSummaryForListing)
                            progressDialog.dismiss()
                        }

                    }

                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()



    }

    override fun onClickPCSPPay(itemPCSP: PCSPPaymentListing) {

        if (itemPCSP.id.startsWith("S"))  {
            val progressDialog = ProgressDialog(context!!)
            progressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {

                val salePayment =
                    ItemSaleDbRepo.getSalePayment(database = database, saleSummaryId = itemPCSP.id)
                salePayment.fineWeights =
                    ItemSaleDbRepo.getSalePaymentFineWeights(
                        database = database,
                        saleSummaryId = itemPCSP.id
                    )

                salePayment.party_name = itemPCSP.customer_name

                when (itemPCSP.bill_type) {
                    BillType.FINE_BASED.id -> listener?.showFineIO(
                        salePayment,
                        FineIOFragment.MODE_SP_DONT_SAVE_PAYMENT
                    )
                    BillType.RATE_BASED.id -> listener?.showMoneyIO(
                        salePayment,
                        MoneyIOFragment.MODE_SP_DONT_SAVE_PAYMENT
                    )
                }
                progressDialog.dismiss()
            }

        }else {

            val progressDialog = ProgressDialog(context!!)
            progressDialog.show()

            GlobalScope.launch(Dispatchers.Main) {

                val purchasePayment =
                    ItemPurchaseDbRepo.getPurchasePayment(database = database, purchaseSummaryId = itemPCSP.id)
                purchasePayment.fineWeightPurchases =
                    ItemPurchaseDbRepo.getPurchasePaymentFineWeights(
                        database = database,
                        purchaseSummaryId = itemPCSP.id
                    )

                purchasePayment.party_name = itemPCSP.customer_name

                when (itemPCSP.bill_type) {
                    BillType.FINE_BASED.id -> listener?.showFineIO(
                        purchasePayment,
                        FineIOFragment.MODE_SP_DONT_SAVE_PAYMENT
                    )
                    BillType.RATE_BASED.id -> listener?.showMoneyIO(
                        purchasePayment,
                        MoneyIOFragment.MODE_SP_DONT_SAVE_PAYMENT
                    )
                }
                progressDialog.dismiss()
            }
        }
    }

    override fun onClickPay(money: MoneyIOForListing) {
        listener?.showMoneyIO(money.createMoneyIO(), MoneyIOFragment.MODE_PAY)
    }
    override fun onClickEdit(money: MoneyIOForListing) {
        listener?.moneyIODetails(money)
    }

    override fun onClickDelete(money: MoneyIOForListing) {
        toast(context = context!!, message = "on tap delete${money.id}")
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

                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()

    }
    override fun onClickEdit(fine: FineIOForListing) {
        listener?.fineIODetails(fine)
    }

    override fun onClickDelete(fine: FineIOForListing) {
        toast(context = context!!, message = "on tap delete")
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

                }

                override fun onCancel(dialog: Dialog) {
                    dialog.dismiss()
                }
            }
        ).show()
    }

    override fun onClickPay(fine: FineIOForListing) {
        listener?.showFineIO(fine.createFineIO(), FineIOFragment.MODE_PAY)
    }
    override fun onListClick(checkoutBalance: CheckoutBalance) {
        //println("####checkoutBalance"+checkoutBalance)
        /*val fDate: Long = checkoutBalance.checkout_date
        val tDate: Long = 1562144830088
        println("tDate"+tDate)
        println("fDate"+fDate)

        *//*accountSummaryAdapter = AccountSummaryRecyclerViewPagedListAdapter(this)
        accountSummaryView = LayoutInflater.from(context).inflate(R.layout.viewpager_account_summary, null)*//*
        //viewPagerAdapter.removeView(viewpager_people_details,accountSummaryPage(fromDate = fDate, toDate = tDate))


        *//*viewPagerAdapter.addView(accountSummaryPage(fromDate = fDate, toDate = tDate), 1)
        viewpager_people_details.adapter = viewPagerAdapter

        viewPagerAdapter.notifyDataSetChanged()
        viewpager_people_details.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {

            }
        })*//*
        //accountSummaryPage(fromDate = fDate, toDate = tDate)

        //accountSummaryListing!!.removeObservers(this)
        //accountSummaryAdapter.registerAdapterDataObserver(observer)

        if (accountSummaryAdapter != null) {
            accountSummaryListing!!.removeObservers(this)
        }

        accountSummaryListing = ItemPurchaseDbRepo.getPCAccountSummaryPS(database, "%${mPeople.id}%", fDate, tDate)

        println("@accountSummaryListing" + accountSummaryListing.toString())


        accountSummaryListing!!.observe(this, Observer { pagedList ->
            accountSummaryAdapter.submitList(pagedList)
            accountSummaryAdapter.notifyDataSetChanged()

        })

        //setEmptyView()
        setAccountSummaryList()*/

    }

    interface OnFragmentInteractionListener {
        fun onBackPressed()
        fun showCustomer(people: People, target: androidx.fragment.app.Fragment?)
        fun editUdhaar(oldUdhaarSummary: UdhaarSummaryForListing)
        fun udhaarDetails(udhaarSummaryForListing: UdhaarSummaryForListing)
        fun udhaarPay(udhaar: UdhaarSummaryForListing)
        fun editPurchase(oldPurchaseSummary: PurchaseSummaryForListing)
        fun editSale(oldSaleSummary: SaleSummaryForListing)
        fun showFineIO(purchasePayment: PurchasePayment, mode: Int)
        fun showMoneyIO(purchasePayment: PurchasePayment, mode: Int)
        fun showFineIO(salePayment: SalePayment, mode: Int)
        fun showMoneyIO(salePayment: SalePayment, mode: Int)
        fun saleDetails(saleSummaryForListing: SaleSummaryForListing)
        fun purchaseDetails(purchaseSummaryForListing: PurchaseSummaryForListing)
        fun showMoneyIO(money: MoneyIO, mode: Int)
        fun moneyIODetails(money: MoneyIOForListing)
        fun showFineIO(fine: FineIO, mode: Int)
        fun fineIODetails(fine: FineIOForListing)

    }

    companion object {
        const val CLASS_SIMPLE_NAME: String = "People Details"
        const val ARG_PEOPLE_PAGES = "PeopleDetailPages"
        const val PICK_DATABASE_FILE_REQUEST_CODE = 1234

        @JvmStatic
        fun newInstance(people: People) =
            CustomerDetails().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CUSTOMER, people)
                }
            }
    }
}
