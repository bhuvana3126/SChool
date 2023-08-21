package com.wholesale.jewels.fauxiq.baheekhata.ui.customer.customer_list

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.dto.ToolbarContent
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.People
import com.wholesale.jewels.fauxiq.baheekhata.enums.PeopleType
import com.wholesale.jewels.fauxiq.baheekhata.enums.getPeopleType
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.PeopleRecyclerView
import com.wholesale.jewels.fauxiq.baheekhata.repo.PeopleDbRepo
import com.wholesale.jewels.fauxiq.baheekhata.room.AppDatabase
import com.wholesale.jewels.fauxiq.baheekhata.ui.Bank.AddEditBankAccount
import com.wholesale.jewels.fauxiq.baheekhata.ui.components.ProgressDialog
import com.wholesale.jewels.fauxiq.baheekhata.ui.expense.ExpenseFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.karigar.karigarAdd.ItemKarigarFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.notification.addNew.AddNotificationFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.oldentry.OldEntryFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.payment.FineIOFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.payment.MoneyIOFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.purchase.AddEditStock.Companion.ARG_MODE
import com.wholesale.jewels.fauxiq.baheekhata.ui.purchase.new_purchase.ItemPurchaseFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.report.DownloadReportFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.returnpurchase.ReturnItemPurchaseFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.returnsale.ReturnItemSaleFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.sales.new_sale.ItemSalesFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.udhaar.UdhaarFineFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.udhaar.UdhaarMoneyFragment
import com.wholesale.jewels.fauxiq.baheekhata.utils.*
import kotlinx.android.synthetic.main.fragment_customer_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PeopleList : androidx.fragment.app.Fragment(), PeopleRecyclerView {

    override fun onSelectPeople(people: People) {
        if (mMode == MODE_SELECT || mMode == MODE_SELECT_FROM_REPORTS) {

            if (people.account_lock.not()) {
                toast(context!!, "Please lock to select")
                println("Please lock to select")
                return
            }

            if (people.active.not()) {
                toast(context!!, "Please make it active first")
                println("Please make it active first")
                return
            }

            when {
                mMode == MODE_SELECT_FROM_REPORTS -> {
                    listener?.onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is ItemPurchaseFragment -> {
                    (targetFragment as ItemPurchaseFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is ItemSalesFragment -> {
                    (targetFragment as ItemSalesFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is MoneyIOFragment -> {
                    (targetFragment as MoneyIOFragment).onPeopleChooses(
                        people = people,
                        peopleKind = mPeopleType.id
                    )
                    listener?.onBackPressed()
                }
                targetFragment is AddEditBankAccount -> {
                    (targetFragment as AddEditBankAccount).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is FineIOFragment -> {
                    (targetFragment as FineIOFragment).onPeopleChooses(
                        people = people,
                        peopleKind = mPeopleType.id
                    )
                    listener?.onBackPressed()
                }
                targetFragment is OldEntryFragment -> {
                    (targetFragment as OldEntryFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is FineIOFragment -> {
                    (targetFragment as FineIOFragment).onPeopleChooses(
                        people = people,
                        peopleKind = mPeopleType.id
                    )
                    listener?.onBackPressed()
                }
                targetFragment is DownloadReportFragment -> {
                    (targetFragment as DownloadReportFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is UdhaarMoneyFragment -> {
                    (targetFragment as UdhaarMoneyFragment).onPeopleChooses(
                        people = people,
                        creditDebit = creditDebit
                    )
                    listener?.onBackPressed()
                }
                targetFragment is UdhaarFineFragment -> {
                    (targetFragment as UdhaarFineFragment).onPeopleChooses(
                        people = people,
                        creditDebit = creditDebit
                    )
                    listener?.onBackPressed()
                }
                targetFragment is ExpenseFragment -> {
                    (targetFragment as ExpenseFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is ReturnItemSaleFragment -> {
                    (targetFragment as ReturnItemSaleFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is ReturnItemPurchaseFragment -> {
                    (targetFragment as ReturnItemPurchaseFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is ItemKarigarFragment -> {
                    (targetFragment as ItemKarigarFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
                targetFragment is AddNotificationFragment -> {
                    (targetFragment as AddNotificationFragment).onPeopleChooses(people)
                    listener?.onBackPressed()
                }
            }

        }
    }

    override fun onClickPeopleDetails(people: People) {
        //   Utils.hideKeyboard(this)
        listener?.onCustomerTapped(people)
    }

    private var listener: OnFragmentInteractionListener? = null

    val database: AppDatabase by inject(AppDatabase.CURRENT)

    private var mMode: Int = 0
    private var peopleActive: Boolean = true

    private lateinit var mPeopleType: PeopleType

    private lateinit var creditDebit: String
    private lateinit var customerDataList: PagedList<People>
    var cusNameList = JSONArray()
    var jsonFilename = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mMode = it.getInt(ARG_MODE)
            mPeopleType =
                getPeopleType(it.getString(ARG_PEOPLE_TYPE)!!).also { println("PeopleType = $it") }
            creditDebit = it.getString(ARG_CREDIT_TYPE)!!.also { println("creditDebit = $it") }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        peopleListAdapter = PeopleRecyclerViewPagedListAdapter(this)
        return inflater.inflate(
            com.wholesale.jewels.fauxiq.baheekhata.R.layout.fragment_customer_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private lateinit var toolbarContent: ToolbarContent


    private fun initUi() {

        search_customer_search_with.setOnSearchButtonClickListener(View.OnClickListener {
            searchWith(false)
        })

        add_new_customer.setOnClickListener {
            listener?.showCustomer(People(mPeopleType), null)
        }

        search_customer_search_with.performSearchButtonClick()

        setPurchaseList()
        setEmptyView()
    }

    private fun searchWith(showProgress: Boolean = true) {

        if (::progressDialog.isInitialized.not()) {
            progressDialog = ProgressDialog(context!!)
        }

        if (showProgress) {
            progressDialog.show()
        }

        val text: String = search_customer_search_with.text.toString()


        if (searchPeople != null) {
            searchPeople!!.removeObservers(this)
        }

        searchPeople = PeopleDbRepo.searchPeople(database, "%$text%", mPeopleType, peopleActive)

        searchPeople!!.observe(this, Observer { pagedList ->
            customerDataList = pagedList
            peopleListAdapter.submitList(pagedList)
            peopleListAdapter.notifyDataSetChanged()
            customer_list.post {
                progressDialog.dismiss()
            }
        })
    }

    private fun setPurchaseList() {

        customer_list.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?
        customer_list.adapter = peopleListAdapter
        peopleListAdapter.registerAdapterDataObserver(observer)
    }

    private lateinit var peopleListAdapter: PeopleRecyclerViewPagedListAdapter

    private val observer = object : RecyclerView.AdapterDataObserver() {
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

        val empty: Boolean = peopleListAdapter.itemCount == 0
        empty_view_new_customer.visibility = if (empty) View.VISIBLE else View.GONE
        customer_list.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun setEmptyView() {

        val tap = "TAP +"
        val text = "\n$tap to create new customer\n"

        val ss = SpannableString(text)

        val selectStocks = object : ClickableSpan() {
            override fun onClick(view: View) {
                listener?.showCustomer(People(mPeopleType), null)
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

        empty_view_new_customer.gone()
        empty_view_new_customer.text = ss
        empty_view_new_customer.movementMethod = LinkMovementMethod.getInstance()
    }


    private lateinit var progressDialog: ProgressDialog

    private var searchPeople: LiveData<PagedList<People>>? = null


    override fun onDetach() {
        super.onDetach()
        toolbarContent.popBackStack()
        peopleListAdapter.unregisterAdapterDataObserver(observer)
        listener = null
    }

    fun showInactiveCustomers() {
        peopleActive = false
        search_customer_search_with.clearText()
        toolbarContent.setToolbar(getToolbarTitle(), true, false)
    }

    fun showActiveCustomers() {
        peopleActive = true
        search_customer_search_with.clearText()
        toolbarContent.setToolbar(getToolbarTitle(), true, false)
    }

    fun showExportPeoples() {
        /*when (mPeopleType){
            PeopleType.CUSTOMER -> showExportPeople()
            PeopleType.PARTY -> showExportPeopleParty()
            else -> showExportPeople()
        }*/

//        showExportPeople()
        showExportPeopleParty()

        toolbarContent.setToolbar(getToolbarTitle(), true, false)
    }

    private fun getToolbarTitle(): String {
        val str = StringBuffer()
        str.append(if (peopleActive) "Active" else "Inactive")
        str.append(String.SPACE)
        str.append(
            when (mPeopleType) {
                PeopleType.CUSTOMER -> "Customers"
                PeopleType.PARTY -> "Parties"
                PeopleType.KARIGAR -> "Karigar"
                PeopleType.OTHER -> "Other"
                else -> ""
            }
        )
        return str.toString()
    }


    fun showExportPeople() {
        progressDialog = ProgressDialog(context!!)
        progressDialog.show()

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)

            PeopleDbRepo.searchPeopleExportList(database = database, people_kind = "C").forEach {
                println("#People:$it")
                try {
                    val fineList = PeopleDbRepo.getFineWtExport(database = database, customerId = it.id)

                    val balFine = JSONArray()
                    fineList.forEach { fine ->
                        val fineObject = JSONObject()
                        fineObject.put("fineWt", fine.fine_wt)
                        fineObject.put("metalType", fine.metal_type)
                        balFine.put(fineObject)
                    }

                    val cusDetails = JSONObject()
                    cusDetails.put("id", it.id)
                    cusDetails.put("customId", "")
                    cusDetails.put("name", it.name)
                    cusDetails.put("peopleKind", it.people_kind)
                    cusDetails.put("companyName", it.company_name)
                    cusDetails.put("mobileNumber", it.mobile_number)
                    cusDetails.put("phoneNumber", it.alternative_phone_number)
                    cusDetails.put("gstinNumber", it.gstin_number)
                    cusDetails.put("email", it.email)
                    cusDetails.put("idType", "")
                    cusDetails.put("idNumber", "")
                    cusDetails.put("idImage", "null")
                    cusDetails.put("buildingArea", it.building_area)
                    cusDetails.put("city", it.city)
                    cusDetails.put("state", it.state)
                    cusDetails.put("pincode", it.pin_code)
                    cusDetails.put("createdDate", "")
                    cusDetails.put("creditBal", it.bal_amount)
                    cusDetails.put("debitBal", 0.0)
                    cusDetails.put("accountLock", it.account_lock)
                    cusDetails.put("active", true)
                    cusDetails.put("connectId", "")
                    cusDetails.put("sync", 0)
                    cusDetails.put("comment", "")
                    cusDetails.put("customerImage", "null")
                    cusDetails.put("dueDate", 0L)
                    cusDetails.put("creditLimit", 0.0)
                    cusDetails.put("balFineWeight", balFine)
                    cusNameList.put(cusDetails)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                val direct = "${Environment.getExternalStorageDirectory().path}/${DEFAULT_STORAGE_LOCATION}"
                val sdf = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.ENGLISH)
                jsonFilename = "Customers" + sdf.format(Calendar.getInstance().time) + ".json"

                val outputFile = File(direct, jsonFilename)
                try {

                    outputFile.createNewFile()
                    val out = FileOutputStream(outputFile)
                    out.write(cusNameList.toString().toByteArray())
                    out.close()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                val fileUriPath = requireContext().getExternalFilesDir("${Environment.DIRECTORY_DOWNLOADS}/${DEFAULT_STORAGE_LOCATION}")
                val sdf = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.ENGLISH)
                jsonFilename = "Customers" + sdf.format(Calendar.getInstance().time) + ".json"
                val outputFile = File(fileUriPath, jsonFilename)
                try {
                    outputFile.createNewFile()
                    val out = FileOutputStream(outputFile)
                    out.write(cusNameList.toString().toByteArray())
                    out.close()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            delay(2000)
            toolbarContent.setToolbar(getToolbarTitle(), true, false)
            progressDialog.dismiss()
        }
    }

    private fun showExportPeopleParty() {
        progressDialog = ProgressDialog(context!!)
        progressDialog.show()

        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)

            PeopleDbRepo.searchPeopleExportList(database = database, people_kind = "P").forEach {
                println("#People:$it")
                try {
                    val fineList = PeopleDbRepo.getFineWtExport(database = database, customerId = it.id)

                    val balFine = JSONArray()
                    fineList.forEach { fine ->
                        val fineObject = JSONObject()
                        fineObject.put("fineWt", fine.fine_wt)
                        fineObject.put("metalType", fine.metal_type)
                        balFine.put(fineObject)
                    }

                    val cusDetails = JSONObject()
                    cusDetails.put("id", it.id)
                    cusDetails.put("customId", "")
                    cusDetails.put("name", it.name)
                    cusDetails.put("peopleKind", it.people_kind)
                    cusDetails.put("companyName", it.company_name)
                    cusDetails.put("mobileNumber", it.mobile_number)
                    cusDetails.put("phoneNumber", it.alternative_phone_number)
                    cusDetails.put("gstinNumber", it.gstin_number)
                    cusDetails.put("email", it.email)
                    cusDetails.put("idType", "")
                    cusDetails.put("idNumber", "")
                    cusDetails.put("idImage", "null")
                    cusDetails.put("buildingArea", it.building_area)
                    cusDetails.put("city", it.city)
                    cusDetails.put("state", it.state)
                    cusDetails.put("pincode", it.pin_code)
                    cusDetails.put("createdDate", "")
                    cusDetails.put("creditBal", it.bal_amount)
                    cusDetails.put("debitBal", 0.0)
                    cusDetails.put("accountLock", it.account_lock)
                    cusDetails.put("active", true)
                    cusDetails.put("connectId", "")
                    cusDetails.put("sync", 0)
                    cusDetails.put("comment", "")
                    cusDetails.put("customerImage", "null")
                    cusDetails.put("dueDate", 0L)
                    cusDetails.put("creditLimit", 0.0)
                    cusDetails.put("balFineWeight", balFine)
                    cusNameList.put(cusDetails)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                val direct = "${Environment.getExternalStorageDirectory().path}/${DEFAULT_STORAGE_LOCATION}"
                val sdf = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.ENGLISH)
                jsonFilename = "Parties" + sdf.format(Calendar.getInstance().time) + ".json"

                val outputFile = File(direct, jsonFilename)
                try {

                    outputFile.createNewFile()
                    val out = FileOutputStream(outputFile)
                    out.write(cusNameList.toString().toByteArray())
                    out.close()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                val fileUriPath = requireContext().getExternalFilesDir("${Environment.DIRECTORY_DOWNLOADS}/${DEFAULT_STORAGE_LOCATION}")
                val sdf = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.ENGLISH)
                jsonFilename = "Parties" + sdf.format(Calendar.getInstance().time) + ".json"
                val outputFile = File(fileUriPath, jsonFilename)
                try {
                    outputFile.createNewFile()
                    val out = FileOutputStream(outputFile)
                    out.write(cusNameList.toString().toByteArray())
                    out.close()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            delay(2000)
            toolbarContent.setToolbar(getToolbarTitle(), true, false)
            progressDialog.dismiss()
        }
    }

    fun isActiveCustomers(): Boolean = peopleActive

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarContent = ViewModelProviders.of(activity!!).get(ToolbarContent::class.java)
        toolbarContent.addToBackStack(CLASS_SIMPLE_NAME)
//        toolbarContent.setToolbar(CLASS_SIMPLE_NAME, true, false)
        toolbarContent.setToolbar(getToolbarTitle(), true, false)

    }

    override fun onDestroyView() {
        search_customer_search_with.onDetach()
        Utils.hideKeyboard(this)
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    interface OnFragmentInteractionListener {
        fun onCustomerTapped(people: People)
        fun onBackPressed()
        fun showCustomer(people: People, target: Fragment?)
        fun onPeopleChooses(people: People)
    }

    companion object {
        const val CLASS_SIMPLE_NAME: String = "PeopleList"
        const val MODE_LIST: Int = 0
        const val MODE_SELECT: Int = 1
        const val MODE_SELECT_FROM_REPORTS: Int = 2
        const val ARG_PEOPLE_TYPE: String = "people type"
        const val ARG_CREDIT_TYPE: String = "people type"

        @JvmStatic
        fun newInstance(mode: Int, peopleKind: String) = PeopleList().apply {
            arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
                putString(ARG_PEOPLE_TYPE, peopleKind)
            }
        }

        @JvmStatic
        fun newInstance(creditDebit: String, mode: Int, peopleKind: String) = PeopleList().apply {
            arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
                putString(ARG_PEOPLE_TYPE, peopleKind)
                putString(ARG_CREDIT_TYPE, creditDebit)
            }
        }
    }
}
