package com.wholesale.jewels.fauxiq.baheekhata.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.databinding.*
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.sales.ItemSale
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.NewStockView
import com.wholesale.jewels.fauxiq.baheekhata.repo.ItemSaleDbRepo
import com.wholesale.jewels.fauxiq.baheekhata.room.AppDatabase
import com.wholesale.jewels.fauxiq.baheekhata.ui.components.EditTextWithClearButton
import com.wholesale.jewels.fauxiq.baheekhata.utils.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SaleDetailItemEdit : androidx.fragment.app.Fragment(), NewStockView {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mItemSale: ItemSale
    private var mMode: Int = 0

    val database: AppDatabase by inject(AppDatabase.CURRENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mItemSale = it.getParcelable(ARG_SALE)!!
            mMode = it.getInt(ARG_MODE)
            mShowRateFields = it.getBoolean(ARG_SHOW_RATE_FIELDS)

            if (mShowRateFields) {
                GlobalScope.launch {
                    mItemSale.addRateSummary(
                        ItemSaleDbRepo.getRateSummary(database = database, itemSaleId = mItemSale.id)
                            .also { println(it) })

                    if (::binding.isInitialized) {
                        binding.executePendingBindings()
                    }

                }
            }

        }
    }

    private lateinit var binding: FragmentSaleDetailItemEditBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sale_detail_item_edit, container, false)
        val myView: View = binding.root

        binding.presenter = this
        binding.item = mItemSale.item
        binding.itemStock = mItemSale.getItemStock()
        binding.itemSale = mItemSale
        binding.showRateFields = mShowRateFields
        binding.onTouchPercentageExpandListener = onTouchPercentageExpandListener
        binding.onWeightExpandListener = onWeightExpandListener
        binding.onChargesExpandListener = onChargesExpandListener
        binding.onMoreDetailsExpandListener = onMoreDetailsExpandListener
        binding.onShowGstLayoutListener = onShowGstLayoutListener
        binding.onRateValueChangeListener = onRateValueChangeListener
        binding.onTaxableAmountChangeListener = onTaxableAmountChangeListener
        binding.onAmountValueChangeListener = onAmountValueChangeListener
        binding.onChargesValueChangeListener = onChargesValueChangeListener
        binding.onNetAmountValueChangeListener = onNetAmountValueChangeListener
        binding.onDiscountValueChangeListener = onDiscountValueChangeListener

        binding.executePendingBindings()
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()


    }

    private fun initUi() {
        binding.edNewStockChargesSaleDetails.setOnClearButtonClickListener(object :
            EditTextWithClearButton.OnClearButtonClickListener {
            override fun onClick() {

                if (isChargesLayoutExpanded()) {

                    chargeLayoutBinding?.edNewStockOtherCharge?.text = null
                    chargeLayoutBinding?.edNewStockMakingCharge?.text = null
                    chargeLayoutBinding?.edNewStockHallmarkCharge?.text = null
                    chargeLayoutBinding?.edNewStockStoneCharge?.text = null
                    chargeLayoutBinding?.edNewStockWaxCharge?.text = null
                    chargeLayoutBinding?.edNewStockOtherCharge?.requestFocus()
                }
            }
        })

        binding.swGstLayoutSaleDetail.visibility = if (mShowRateFields) View.VISIBLE else View.GONE
        showGstLayout(mItemSale.rateSummary != null && mItemSale.rateSummary!!.gst_percentage != 0.0)
    }

    private fun getRate(): Double = binding.edNewStockRateSaleDetail.getDouble()
    private fun getAmount(): Double = binding.edNewStockAmountSaleDetail.getDouble()
    private fun getNetAmount(): Double = binding.edNewStockNetAmountSaleDetail.getDouble()
    private fun getDiscount(): Double = binding.edNewStockDiscountSaleDetail.getDouble()
    private fun getTaxableAmount(): Double = binding.edNewStockTaxableAmountSaleDetail.getDouble()


    private val onTouchPercentageExpandListener: View.OnClickListener = View.OnClickListener {
        showTouchPercentage(!it.isSelected)
        binding.btNewStockSearchTouchPercentageSaleWeight.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (!it.isSelected) R.drawable.ic_arrow_up_accent else R.drawable.ic_arrow_down_accent,
            0
        )
        it.isSelected = !it.isSelected
    }

    private val onWeightExpandListener: View.OnClickListener = View.OnClickListener {

        showWeightLayout(!it.isSelected)
        binding.btNewStockSearchWeightSaleDetails.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (!it.isSelected) R.drawable.ic_arrow_up_accent else R.drawable.ic_arrow_down_accent,
            0
        )
        it.isSelected = !it.isSelected
    }

    private val onChargesExpandListener: View.OnClickListener = View.OnClickListener {

        showChargesLayout(!it.isSelected)
        binding.btNewStockSearchChargesSaleDetail.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (!it.isSelected) R.drawable.ic_arrow_up_accent else R.drawable.ic_arrow_down_accent,
            0
        )
        it.isSelected = !it.isSelected
    }

    private val onMoreDetailsExpandListener: View.OnClickListener = View.OnClickListener {

        showMoreDetailsLayout(!it.isSelected)
        binding.tvNewStockMoreDetailsSaleDetails.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (!it.isSelected) R.drawable.ic_arrow_up_accent else R.drawable.ic_arrow_down_accent,
            0
        )
        it.isSelected = !it.isSelected
    }

    private val onShowGstLayoutListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        showGstLayout(isChecked)
    }

    private val onRateValueChangeListener = object : OnTextChangeListener {
        override fun onTextChange(text: CharSequence) {
            updateAmount()
        }
    }

    private val onAmountValueChangeListener = object : OnTextChangeListener {
        override fun onTextChange(text: CharSequence) {

            binding.edNewStockNetAmountSaleDetail.setText(
                StringFormat.format(
                    Formulas.calcItemNetAmount(
                        amount = if (text.isNotEmpty()) text.toString().toDouble() else DOUBLE_DEFAULT_VALUE,
                        charges = mItemSale.total_charge
                    ),
                    StringFormat.DECIMAL_FORMAT_00
                )
            )
        }
    }

    private val onChargesValueChangeListener = object : OnTextChangeListener {
        override fun onTextChange(text: CharSequence) {

            binding.edNewStockNetAmountSaleDetail.setText(
                Formulas.calcItemNetAmount(
                    amount = getAmount(),
                    charges = if (text.isNotEmpty()) text.toString().replace(
                        getString(R.string.rupee),
                        ""
                    ).toDouble() else DOUBLE_DEFAULT_VALUE
                ).toString(2)
            )
        }
    }

    private val onNetAmountValueChangeListener = object : OnTextChangeListener {
        override fun onTextChange(text: CharSequence) {

            binding.edNewStockTaxableAmountSaleDetail.setText(
                StringFormat.format(
                    Formulas.calcTaxableAmount(
                        netAmount = getNetAmount(),
                        discount = getDiscount()
                    ),
                    StringFormat.DECIMAL_FORMAT_00
                )
            )
        }
    }

    private val onDiscountValueChangeListener = object : OnTextChangeListener {
        override fun onTextChange(text: CharSequence) {

            binding.edNewStockTaxableAmountSaleDetail.setText(
                StringFormat.format(
                    Formulas.calcTaxableAmount(
                        netAmount = getNetAmount(),
                        discount = getDiscount()
                    ),
                    StringFormat.DECIMAL_FORMAT_00
                )
            )
        }
    }

    private fun updateAmount() {

        if (binding.edNewStockAmountSaleDetail == null) return

        if (mItemSale.getItemStock() == null) {
            toast(context = context!!, message = "Please select the item")
            return
        }

        binding.edNewStockAmountSaleDetail.setText(
            StringFormat.format(
                d = calcAmount(),
                type = StringFormat.DECIMAL_FORMAT_00
            )
        )
    }

    private fun calcAmount(): Double =
        when (mItemSale.item?.saleType) {

            ITEM_PURCHASE_TYPE_TOUCH_BASED, ITEM_PURCHASE_TYPE_UNIT_WT_TOUCH_BASED, ITEM_PURCHASE_TYPE_BULLION -> {

                val wastageWeight = getWastageWeight()

                if (wastageWeight != DOUBLE_DEFAULT_VALUE) {
                    Formulas.calcItemAmountWastage(
                        totalWt = mItemSale.total_wt,
                        rate = getRate(),
                        melt = mItemSale.getItemStock()!!.melt
                    )
                } else {
                    Formulas.calcItemAmountByTouch(netWt = mItemSale.net_wt, rate = getRate(), touch = getTouch())
                }
            }

            ITEM_PURCHASE_TYPE_WASTAGE_BASED, ITEM_PURCHASE_TYPE_UNIT_WT_WASTAGE_BASED -> {
                Formulas.calcItemAmountWastage(
                    totalWt = mItemSale.total_wt,
                    rate = getRate(),
                    melt = mItemSale.getItemStock()!!.melt
                )
            }

            ITEM_PURCHASE_TYPE_DIRECT_RATE_BASED -> {
                Formulas.calcItemAmountDirectRate(
                    totalWt = calcTotalWt(),
                    rate = getRate()
                )

            }
            else -> {
                DOUBLE_DEFAULT_VALUE
            }
        }

    private fun getTouch(): Double =
        if (binding.edNewStockTouchPercentageSaleDetails != null && binding.edNewStockTouchPercentageSaleDetails.text!!.isNotEmpty())
            binding.edNewStockTouchPercentageSaleDetails.text.toString().toDouble() else DOUBLE_DEFAULT_VALUE

    private fun getWastageWeight(): Double {

        if (weightLayoutBinding?.edNewStockWastageWt == null || weightLayoutBinding?.edNewStockWastageWt?.text!!.isEmpty()) {
            return DOUBLE_DEFAULT_VALUE
        }
        return weightLayoutBinding?.edNewStockWastageWt?.text.toString().toDouble()
    }

    private val onTaxableAmountChangeListener = object : OnTextChangeListener {
        override fun onTextChange(text: CharSequence) {
            calcGst()
        }
    }

    private var mShowRateFields: Boolean = false

    //    Touch percentage

    private fun showTouchPercentage(b: Boolean) {

        TransitionManager.beginDelayedTransition(binding.newStockMainLayoutSaledetails)
        if (b) {

            if (mItemSale.item != null) {

                val array = mItemSale.item!!.touchPercentageRange.split(STRING_COMMA)

                for (element in array) {
                    val tv = TextView(context)
                    tv.text = element
                    tv.setPadding(
                        Utils.getPx(context, 16),
                        Utils.getPx(context, 8),
                        Utils.getPx(context, 16),
                        Utils.getPx(context, 8)
                    )
                    tv.setOnClickListener { v ->
                        binding.btNewStockSearchTouchPercentageSaleWeight
                        binding.edNewStockTouchPercentageSaleDetails.setText((v as TextView).text.toString())
                        showTouchPercentage(false)
                        binding.btNewStockSearchTouchPercentageSaleWeight.performClick()
                    }
                    binding.layoutNewStockTouchSaleDetail.removeAllViews()
                    binding.layoutNewStockTouchSaleDetail.addView(tv)
                }
            }
        } else binding.layoutNewStockTouchSaleDetail.removeAllViews()
    }

    override fun onTouchPercentageChanged(touchPercentage: CharSequence) {
        binding.edNewStockFineWtSaleDetails.setText(StringFormat.format(calcFineWt(), StringFormat.DECIMAL_FORMAT_000))
        mItemSale.touch =
            if (touchPercentage.isNotEmpty()) touchPercentage.toString().toDouble() else DOUBLE_DEFAULT_VALUE
        updateAmount()
    }

    override fun onQuantityChanged(quantity: CharSequence) {

        calcGrossWt()
        mItemSale.qty = if (quantity.isNotEmpty()) quantity.toString().toInt() else INT_DEFAULT_VALUE


        if (mItemSale.item != null &&
            (mItemSale.item!!.saleType == ITEM_PURCHASE_TYPE_UNIT_WT_TOUCH_BASED ||
                    mItemSale.item!!.saleType == ITEM_PURCHASE_TYPE_UNIT_WT_WASTAGE_BASED)
        ) {

            if (mItemSale.unit_wt != DOUBLE_DEFAULT_VALUE) {

                val grossWeight = Formulas.getGrossWeight(qty = mItemSale.qty, unitWt = mItemSale.unit_wt)
                mItemSale.gross_wt = grossWeight

                val kgFromGm = mItemSale.gross_wt

                binding.edNewStockWt.setText(if (kgFromGm != 0.0) kgFromGm.toString() else null)
            } else if (mItemSale.gross_wt != DOUBLE_DEFAULT_VALUE && mItemSale.qty != 0) {

                binding.edNewStockUnitWeightSaleWeight.setText(
                    StringFormat.format(
                        Formulas.getUnitWeight(qty = mItemSale.qty, grossWt = mItemSale.gross_wt),
                        StringFormat.DECIMAL_FORMAT_000
                    )
                )
            }
        }
    }

    override fun onUnitWeightChanged(unitWeight: CharSequence) {

        mItemSale.unit_wt =
            if (unitWeight.isNotEmpty()) unitWeight.toString().toDouble() else DOUBLE_DEFAULT_VALUE

        if (mItemSale.item != null &&
            (mItemSale.item!!.saleType == ITEM_PURCHASE_TYPE_UNIT_WT_TOUCH_BASED ||
                    mItemSale.item!!.saleType == ITEM_PURCHASE_TYPE_UNIT_WT_WASTAGE_BASED)
        ) {

            if (mItemSale.qty != INT_DEFAULT_VALUE) {

                val grossWeight = Formulas.getGrossWeight(qty = mItemSale.qty, unitWt = mItemSale.unit_wt)
                mItemSale.gross_wt = grossWeight

                val kgFromGm = mItemSale.gross_wt
                binding.edNewStockWt.setText(if (kgFromGm != 0.0) kgFromGm.toString() else null)
            }

            binding.edNewStockWt.isFocusable = mItemSale.unit_wt == DOUBLE_DEFAULT_VALUE
            binding.edNewStockWt.isFocusableInTouchMode = mItemSale.unit_wt == DOUBLE_DEFAULT_VALUE
        }
    }

//    GST layout

    private var gstLayoutBinding: LayoutNewStockGstLayoutBinding? = null
    private lateinit var gstLayoutView: View

    private fun showGstLayout(b: Boolean) {

        TransitionManager.beginDelayedTransition(binding.newStockMainLayoutSaledetails)

        if (b) {
            gstLayoutBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_new_stock_gst_layout,
                binding.layoutNewStockGstSaleDetail,
                false
            )
            gstLayoutView = gstLayoutBinding!!.root

            if (mItemSale.rateSummary != null) {
                gstLayoutBinding!!.rateSummary = mItemSale.rateSummary!!
            }

            binding.layoutNewStockGstSaleDetail.removeAllViews()
            binding.layoutNewStockGstSaleDetail.addView(gstLayoutView)

            gstLayoutBinding!!.onGstPercentageChangeListener = object : OnTextChangeListener {
                override fun onTextChange(text: CharSequence) {
                    calcGst()
                }
            }

            gstLayoutBinding!!.onTaxGroupChangeListener = RadioGroup.OnCheckedChangeListener { _, _ ->
                calcGst()
            }

            gstLayoutBinding!!.executePendingBindings()

            calcGst()
        } else {
            binding.layoutNewStockGstSaleDetail.removeAllViews()
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun calcGst() {

        if (gstLayoutBinding?.edNewStockGstPercentage == null) {

            binding.edNewStockTotalAmountSaleDetail.setText(
                StringFormat.format(
                    Formulas.calTotalAmountAfterTax(
                        taxableAmount = getTaxableAmount(),
                        gstPercentage = if (mItemSale.rateSummary != null) mItemSale.rateSummary!!.gst_percentage else DOUBLE_DEFAULT_VALUE
                    ), StringFormat.DECIMAL_FORMAT_00
                )
            )
            return
        }

        val gstPercentage =
            if (gstLayoutBinding?.edNewStockGstPercentage?.text!!.isNotEmpty()) gstLayoutBinding?.edNewStockGstPercentage?.text.toString().toDouble() else 0.0

        var cgstValue = 0.0
        var sgstValue = 0.0
        var igstValue = 0.0
        var utgstValue = 0.0

        if (gstLayoutBinding?.sgstCgst?.isChecked == true) {

            cgstValue = gstPercentage / 2
            sgstValue = gstPercentage / 2
        }

        if (gstLayoutBinding?.igst?.isChecked == true) {

            igstValue = gstPercentage
        }

        if (gstLayoutBinding?.utgstCgst?.isChecked == true) {

            cgstValue = gstPercentage / 2
            utgstValue = gstPercentage / 2
        }

        gstLayoutBinding?.tvNewStockCgst?.text = getString(
            R.string.cgst_percentage,
            StringFormat.format(cgstValue, StringFormat.DECIMAL_FORMAT_00)
        )
        gstLayoutBinding?.tvNewStockSgst?.text = getString(
            R.string.sgst_percentage,
            StringFormat.format(sgstValue, StringFormat.DECIMAL_FORMAT_00)
        )
        gstLayoutBinding?.tvNewStockIgst?.text = getString(
            R.string.igst_percentage,
            StringFormat.format(igstValue, StringFormat.DECIMAL_FORMAT_00)
        )
        gstLayoutBinding?.tvNewStockUtgst?.text = getString(
            R.string.utgst_percentage,
            StringFormat.format(utgstValue, StringFormat.DECIMAL_FORMAT_00)
        )

        val taxableAmount = getTaxableAmount()

        gstLayoutBinding?.tvNewStockCgstValue?.text = getString(
            R.string.rupee_x_x,
            Formulas.calcPercentage(amount = taxableAmount, percentage = cgstValue).toString()
        )
        gstLayoutBinding?.tvNewStockSgstValue?.text = getString(
            R.string.rupee_x_x,
            Formulas.calcPercentage(amount = taxableAmount, percentage = sgstValue).toString()
        )
        gstLayoutBinding?.tvNewStockIgstValue?.text = getString(
            R.string.rupee_x_x,
            Formulas.calcPercentage(amount = taxableAmount, percentage = igstValue).toString()
        )
        gstLayoutBinding?.tvNewStockUtgstValue?.text = getString(
            R.string.rupee_x_x,
            Formulas.calcPercentage(amount = taxableAmount, percentage = utgstValue).toString()
        )
        gstLayoutBinding?.tvNewStockTotalValue?.text = getString(
            R.string.rupee_x_x,
            Formulas.calcPercentage(amount = taxableAmount, percentage = gstPercentage).toString()
        )

        binding.edNewStockTotalAmountSaleDetail.setText(
            StringFormat.format(
                Formulas.calTotalAmountAfterTax(
                    taxableAmount = taxableAmount,
                    gstPercentage = gstPercentage
                ), StringFormat.DECIMAL_FORMAT_00
            )
        )
    }

//    Weight layout

    private var weightLayoutBinding: LayoutNewSaleWeightLayoutBinding? = null
    private lateinit var weightLayoutView: View

    private fun showWeightLayout(b: Boolean) {

        TransitionManager.beginDelayedTransition(binding.newStockMainLayoutSaledetails)
        if (b) {

            weightLayoutBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_new_sale_weight_layout,
                binding.layoutNewStockWeightsSaleDetail,
                false
            )
            weightLayoutView = weightLayoutBinding!!.root

            binding.layoutNewStockWeightsSaleDetail.removeAllViews()
            binding.layoutNewStockWeightsSaleDetail.addView(weightLayoutView)

            if (mItemSale.net_wt.isZero()) {
                with(mItemSale) { net_wt = gross_wt }
            }

            if (mItemSale.total_wt.isZero()) {
                with(mItemSale) { total_wt = gross_wt }
            }

            weightLayoutBinding?.presenter = this
            weightLayoutBinding?.itemSale = mItemSale
            weightLayoutBinding?.executePendingBindings()

        } else {
            binding.layoutNewStockWeightsSaleDetail.removeAllViews()
        }
    }

    override fun onGrossWeightChanged(id: Int, grossWeight: CharSequence) {

        val grossWt = calcGrossWt()

        if (grossWt > mItemSale.getItemStock()?.gross_wt ?: Double.ZERO) {

            toast(context = context!!, message = "You can not sell more than the quantity")
            val field = view?.findViewById<EditText>(id)

            field?.let {
                field.setText(field.text.subSequence(0, field.text.length - 1))
                field.setSelection(field.text.length)
            }
            return
        }

        if (isWeightLayoutExpanded()) weightLayoutBinding?.edNewStockNetWt?.setText(
            StringFormat.format(
                calcNetWt(),
                StringFormat.DECIMAL_FORMAT_000
            )
        )
        if (isWeightLayoutExpanded()) weightLayoutBinding?.edNewStockTotalWt?.setText(
            StringFormat.format(
                calcTotalWt(),
                StringFormat.DECIMAL_FORMAT_000
            )
        )
        binding.edNewStockFineWtSaleDetails.setText(StringFormat.format(calcFineWt(), StringFormat.DECIMAL_FORMAT_000))

        if (mItemSale.item != null &&
            (mItemSale.item!!.saleType == ITEM_PURCHASE_TYPE_UNIT_WT_TOUCH_BASED ||
                    mItemSale.item!!.saleType == ITEM_PURCHASE_TYPE_UNIT_WT_WASTAGE_BASED) &&
            mItemSale.unit_wt == DOUBLE_DEFAULT_VALUE &&
            mItemSale.gross_wt != DOUBLE_DEFAULT_VALUE &&
            mItemSale.qty != INT_DEFAULT_VALUE
        ) {

            if (mItemSale.gross_wt != DOUBLE_DEFAULT_VALUE) {
                binding.edNewStockUnitWeightSaleWeight.setText(
                    StringFormat.format(
                        Formulas.getUnitWeight(qty = mItemSale.qty, grossWt = mItemSale.gross_wt),
                        StringFormat.DECIMAL_FORMAT_000
                    )
                )
            }
        }

        updateAmount()
    }

    override fun onLessWeightChanged(lessWeight: CharSequence) {
        weightLayoutBinding?.edNewStockNetWt?.setText(StringFormat.format(calcNetWt(), StringFormat.DECIMAL_FORMAT_000))
        weightLayoutBinding?.edNewStockTotalWt?.setText(
            StringFormat.format(
                calcTotalWt(),
                StringFormat.DECIMAL_FORMAT_000
            )
        )
        binding.edNewStockFineWtSaleDetails.setText(StringFormat.format(calcFineWt(), StringFormat.DECIMAL_FORMAT_000))
        mItemSale.less_wt =
            if (lessWeight.isNotEmpty()) lessWeight.toString().toDouble() else DOUBLE_DEFAULT_VALUE
    }

    override fun onStoneWeightChanged(stoneWeight: CharSequence) {
        weightLayoutBinding?.edNewStockNetWt?.setText(StringFormat.format(calcNetWt(), StringFormat.DECIMAL_FORMAT_000))
        weightLayoutBinding?.edNewStockTotalWt?.setText(
            StringFormat.format(
                calcTotalWt(),
                StringFormat.DECIMAL_FORMAT_000
            )
        )
        binding.edNewStockFineWtSaleDetails.setText(StringFormat.format(calcFineWt(), StringFormat.DECIMAL_FORMAT_000))
        mItemSale.stone_wt =
            if (stoneWeight.isNotEmpty()) stoneWeight.toString().toDouble() else DOUBLE_DEFAULT_VALUE
    }

    override fun onWaxWeightChanged(waxWeight: CharSequence) {
        weightLayoutBinding?.edNewStockNetWt?.setText(StringFormat.format(calcNetWt(), StringFormat.DECIMAL_FORMAT_000))
        weightLayoutBinding?.edNewStockTotalWt?.setText(
            StringFormat.format(
                calcTotalWt(),
                StringFormat.DECIMAL_FORMAT_000
            )
        )
        binding.edNewStockFineWtSaleDetails.setText(StringFormat.format(calcFineWt(), StringFormat.DECIMAL_FORMAT_000))
        mItemSale.wax_wt = if (waxWeight.isNotEmpty()) waxWeight.toString().toDouble() else DOUBLE_DEFAULT_VALUE
    }

    override fun onWastageWeightChanged(wastageWeight: CharSequence) {
        weightLayoutBinding?.edNewStockTotalWt?.setText(
            StringFormat.format(
                calcTotalWt(),
                StringFormat.DECIMAL_FORMAT_000
            )
        )
        binding.edNewStockFineWtSaleDetails.setText(StringFormat.format(calcFineWt(), StringFormat.DECIMAL_FORMAT_000))
        mItemSale.wastage_wt =
            if (wastageWeight.isNotEmpty()) wastageWeight.toString().toDouble() else DOUBLE_DEFAULT_VALUE
    }

    override fun onNetWeightChanged(netWeight: CharSequence) {
        weightLayoutBinding?.edNewStockNetWt?.error =
            if (Validations.validNetWeight(netWeight).not()) getString(R.string.invalid_net_weight)
            else null
    }

    override fun onTotalWeightChanged(totalWeight: CharSequence) {
        weightLayoutBinding?.edNewStockTotalWt?.error =
            if (Validations.validTotalWeight(totalWeight).not()) getString(R.string.invalid_total_weight)
            else null
    }

    private fun calcGrossWt(): Double {

        val grossWeight = Formulas.getGrossWeight(
            binding.edNewStockWt.text.toString()
        )
        mItemSale.gross_wt = grossWeight

        return grossWeight
    }

    private fun calcNetWt(): Double {

        if (binding.edNewStockWt == null) return DOUBLE_DEFAULT_VALUE

        val grossWeight = Formulas.getGrossWeight(
            binding.edNewStockWt.text.toString()
        )
        mItemSale.gross_wt = grossWeight

        val netWeight = Formulas.calcNetWeight(
            grossWeight,
            if (isWeightLayoutExpanded()) weightLayoutBinding?.edNewStockLessWt?.getDouble()
                ?: 0.0 else DOUBLE_DEFAULT_VALUE,
            if (isWeightLayoutExpanded()) weightLayoutBinding?.edNewStockStoreWt?.getDouble()
                ?: 0.0 else DOUBLE_DEFAULT_VALUE,
            if (isWeightLayoutExpanded()) weightLayoutBinding?.edNewStockWaxWt?.getDouble()
                ?: 0.0 else DOUBLE_DEFAULT_VALUE
        )
        mItemSale.net_wt = netWeight
        return netWeight
    }

    private fun calcTotalWt(): Double {

        val totalWeight = Formulas.calcTotalWeight(
            calcNetWt(),
            if (weightLayoutBinding?.edNewStockWastageWt != null) weightLayoutBinding?.edNewStockWastageWt?.text.toString() else "0.0"
        )

        mItemSale.total_wt = totalWeight
        return totalWeight
    }

    private fun calcFineWt(): Double {

        var touch = getTouch()

        if (touch == DOUBLE_DEFAULT_VALUE) touch =
            if (mItemSale.getItemStock() != null) mItemSale.getItemStock()!!.melt else DOUBLE_DEFAULT_VALUE

        val fineWeight =

            if (mItemSale.item != null) when (mItemSale.item!!.saleType) {

                ITEM_PURCHASE_TYPE_WASTAGE_BASED,
                ITEM_PURCHASE_TYPE_UNIT_WT_WASTAGE_BASED ->

                    Formulas.calcFineWeightWithMelt(
                        totalWt = calcTotalWt(),
                        melt = if (mItemSale.getItemStock() != null) mItemSale.getItemStock()!!.melt else DOUBLE_DEFAULT_VALUE
                    )

                ITEM_PURCHASE_TYPE_TOUCH_BASED,
                ITEM_PURCHASE_TYPE_UNIT_WT_TOUCH_BASED,
                ITEM_PURCHASE_TYPE_DIRECT_RATE_BASED,
                ITEM_PURCHASE_TYPE_BULLION ->

                    Formulas.calcFineWeightWithTouch(
                        netWt = calcNetWt(),
                        touch = touch
                    )

                else -> DOUBLE_DEFAULT_VALUE
            } else DOUBLE_DEFAULT_VALUE

        mItemSale.fine_wt = fineWeight
        return fineWeight
    }

    private fun isWeightLayoutExpanded(): Boolean = binding.btNewStockSearchWeightSaleDetails.isSelected

//    Charges layout

    private var chargeLayoutBinding: LayoutNewSalesChargesLayoutBinding? = null
    private lateinit var chargeLayoutView: View

    private fun showChargesLayout(b: Boolean) {

        TransitionManager.beginDelayedTransition(binding.newStockMainLayoutSaledetails)
        if (b) {

            chargeLayoutBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.layout_new_sales_charges_layout,
                binding.layoutNewStockChargesSaleDetail,
                false
            )
            chargeLayoutView = chargeLayoutBinding!!.root

            binding.layoutNewStockChargesSaleDetail.removeAllViews()
            binding.layoutNewStockChargesSaleDetail.addView(chargeLayoutView)

            chargeLayoutBinding?.presenter = this
            chargeLayoutBinding?.itemSale = mItemSale
            chargeLayoutBinding?.executePendingBindings()

        } else binding.layoutNewStockChargesSaleDetail.removeAllViews()
    }

    override fun onOtherChargeChanged(otherCharge: CharSequence) {
        binding.edNewStockChargesSaleDetails.setText(calcTotalCharge().toString(2))
        mItemSale.other_charge = otherCharge.getDouble()
    }

    override fun onMakingChargeChanged(makingCharge: CharSequence) {
        binding.edNewStockChargesSaleDetails.setText(calcTotalCharge().toString(2))
        mItemSale.making_charge = makingCharge.getDouble()
    }

    override fun onHallmarkChargeChanged(hallmarkCharge: CharSequence) {
        binding.edNewStockChargesSaleDetails.setText(calcTotalCharge().toString(2))
        mItemSale.hallmark_charge = hallmarkCharge.getDouble()
    }

    override fun onStoneChargeChanged(stoneCharge: CharSequence) {
        binding.edNewStockChargesSaleDetails.setText(calcTotalCharge().toString(2))
        mItemSale.stone_charge = stoneCharge.getDouble()
    }

    override fun onWaxChargeChanged(waxCharge: CharSequence) {
        binding.edNewStockChargesSaleDetails.setText(calcTotalCharge().toString(2))
        mItemSale.wax_charge = waxCharge.getDouble()
    }

    private fun calcTotalCharge(): Double {
        val totalCharge = Formulas.totalCharge(
            chargeLayoutBinding?.edNewStockOtherCharge?.getDouble() ?: 0.0,
            chargeLayoutBinding?.edNewStockMakingCharge?.getDouble() ?: 0.0,
            chargeLayoutBinding?.edNewStockHallmarkCharge?.getDouble() ?: 0.0,
            chargeLayoutBinding?.edNewStockStoneCharge?.getDouble() ?: 0.0,
            chargeLayoutBinding?.edNewStockWaxCharge?.getDouble() ?: 0.0
        )
        mItemSale.total_charge = totalCharge
        return totalCharge
    }

    private fun isChargesLayoutExpanded(): Boolean = binding.btNewStockSearchChargesSaleDetail.isSelected

//    More details

    private var moreDetailsLayoutBinding: LayoutNewSalesMoreDetailsLayoutBinding? = null
    private lateinit var moreDetailsLayoutView: View

    private fun showMoreDetailsLayout(b: Boolean) {

        TransitionManager.beginDelayedTransition(binding.newStockMainLayoutSaledetails)
        if (b) {

            moreDetailsLayoutBinding = DataBindingUtil.inflate(
                layoutInflater,

                R.layout.layout_new_sales_more_details_layout,
                binding.layoutNewStockMoreDetailsSaleDetail,
                false
            )
            moreDetailsLayoutView = moreDetailsLayoutBinding!!.root

            binding.layoutNewStockMoreDetailsSaleDetail.removeAllViews()
            binding.layoutNewStockMoreDetailsSaleDetail.addView(moreDetailsLayoutView)

            moreDetailsLayoutBinding?.itemSale = mItemSale
            moreDetailsLayoutBinding?.executePendingBindings()

        } else binding.layoutNewStockMoreDetailsSaleDetail.removeAllViews()
    }

    private fun isMoreDetailsLayoutExpanded(): Boolean = binding.tvNewStockMoreDetailsSaleDetails.isSelected


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
        listener = null
    }

    interface OnFragmentInteractionListener {

    }

    companion object {
        const val CLASS_SIMPLE_NAME: String = "Item stock"
        const val ARG_SALE = "item sale"
        const val ARG_MODE = "mode"
        const val ARG_SHOW_RATE_FIELDS = "show rate fields"


        const val MODE_EDIT_SAME_STOCK: Int = 0

        @JvmStatic
        fun newInstance(
            itemSale: ItemSale,
            mode: Int,
            showRateFields: Boolean
        ) =
            SaleDetailItemEdit().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SALE, itemSale)
                    putInt(ARG_MODE, mode)
                    putBoolean(ARG_SHOW_RATE_FIELDS, showRateFields)
                }
            }
    }
}
