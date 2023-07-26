package com.wholesale.jewels.fauxiq.baheekhata.utils

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.enums.MetalType
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.AlertDialogInterface
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.AlertDialogInterfaceWithMetalType
import kotlinx.android.synthetic.main.alert_bank_openingbalz.view.*
import kotlinx.android.synthetic.main.alert_delete_message.view.*
import kotlinx.android.synthetic.main.alert_message_with_edittext.view.*
import kotlinx.android.synthetic.main.alert_message_with_edittext.view.value
import kotlinx.android.synthetic.main.alert_message_with_edittext_spinner.view.*
import kotlinx.android.synthetic.main.alert_simple_message.view.cancel
import kotlinx.android.synthetic.main.alert_simple_message.view.done
import kotlinx.android.synthetic.main.alert_simple_message.view.text
import kotlinx.android.synthetic.main.alert_simple_message.view.title
import kotlinx.android.synthetic.main.fragment_karigar_beeds_item.view.*

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun toast(context: Context, message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

object AlertMessages {

    fun alert(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        isCancellable: Boolean,
        listener: AlertDialogInterface,
        span: SpannableString? = null,
        hideCancelButton: Boolean = false
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.alert_simple_message, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setTitle("")
        dialog.setCancelable(isCancellable)

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.title.text = title
        layout.text.text = message
        span?.let {
            layout.text.setText(span, TextView.BufferType.SPANNABLE)
        }
        layout.done.text = positiveButton
        layout.cancel.text = negativeButton

        if (hideCancelButton) {
            layout.cancel.gone()
        }

        layout.cancel.setOnClickListener {
            listener.onCancel(dialog)
        }
        layout.done.setOnClickListener {
            listener.onProceed(dialog)
        }

        return dialog
    }

    fun messageWithEditText(
        context: Context,
        title: String,
        message: String,
        value: String,
        positiveButton: String,
        negativeButton: String,
        isCancellable: Boolean,
        listener: AlertDialogInterface,
        textChangeListener: OnTextViewTextChangeListener? = null
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.alert_message_with_edittext, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setTitle("")
        dialog.setCancelable(isCancellable)

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.title.text = title
        layout.text.text = message
        layout.value.hint = value
        layout.done.text = positiveButton
        layout.cancel.text = negativeButton

        layout.value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textChangeListener?.onTextChange(layout.title, layout.value.text)
            }
        })

        layout.cancel.setOnClickListener {
            listener.onCancel(dialog)
        }
        layout.done.setOnClickListener {
            if (layout.value.text.isNotEmpty()) {
                listener.onProceed(dialog, layout.value.text.toString())
            } else {
                toast(context = context, message = "Please enter the rate.")
            }
        }

        return dialog
    }

    fun messageWithEditTextAndSpinner(
        context: Context,
        title: String,
        message: String,
        value: String,
        positiveButton: String,
        negativeButton: String,
        metalTypes: ArrayList<MetalType>,
        isCancellable: Boolean,
        listener: AlertDialogInterfaceWithMetalType,
        textChangeListener: OnTextViewTextChangeListener? = null
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.alert_message_with_edittext_spinner, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setTitle("")
        dialog.setCancelable(isCancellable)

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.title.text = title
        layout.text.text = message
        layout.value.hint = value
        layout.done.text = positiveButton
        layout.cancel.text = negativeButton

        layout.value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textChangeListener?.onTextChange(layout.title, layout.value.text)
            }
        })

        var metalType = metalTypes[0]

        layout.metal_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                metalType = metalTypes[position]
            }
        }

        val dataAdapter =
            ArrayAdapter<MetalType>(
                context,
                android.R.layout.simple_spinner_item,
                metalTypes
            )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        layout.metal_type.adapter = dataAdapter

        layout.cancel.setOnClickListener {
            listener.onCancel(dialog)
        }
        layout.done.setOnClickListener {

            if (layout.value.text.isNotEmpty()) {
                listener.onProceed(dialog, layout.value.text.toString(), metalType)
            } else {
                toast(context = context, message = "Please enter the rate.")
            }
        }

        return dialog
    }

    fun alertDelete(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        isCancellable: Boolean,
        listener: AlertDialogInterface,
        span: SpannableString? = null,
        hideCancelButton: Boolean = false
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.alert_delete_message, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setTitle("")
        dialog.setCancelable(isCancellable)

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.delete_title.text = title
        layout.delete_text.text = message
        span?.let {
            layout.text.setText(span, TextView.BufferType.SPANNABLE)
        }
        layout.delete_done.text = positiveButton
        layout.delete_cancel.text = negativeButton

        if (hideCancelButton) {
            layout.delete_cancel.gone()
        }

        layout.delete_cancel.setOnClickListener {
            listener.onCancel(dialog)
        }
        layout.delete_done.setOnClickListener {
            listener.onProceed(dialog)
        }

        return dialog
    }


    fun AddExpenseCategory(
        context: Context,
        message: String,
        value: String,
        positiveButton: String,
        negativeButton: String,
        isCancellable: Boolean,
        listener: AlertDialogInterface,
        textChangeListener: OnTextViewTextChangeListener? = null
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.alert_message_with_edittext_category, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setCancelable(isCancellable)

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.text.text = message
        layout.value.hint = value
        layout.done.text = positiveButton
        layout.cancel.text = negativeButton

        layout.value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textChangeListener?.onTextChange(layout.title, layout.value.text)
            }
        })

        layout.cancel.setOnClickListener {
            listener.onCancel(dialog)
        }
        layout.done.setOnClickListener {
            if (layout.value.text.isNotEmpty()) {
                listener.onProceed(dialog, layout.value.text.toString())
            } else {
                toast(context = context, message = "Please add Category Names.")
            }
        }

        return dialog
    }

    fun AddBeedsStones(
        context: Context,
        message1: String,message2: String,message3: String,message4: String,message5: String,message6: String,message7: String,message8: String,
        value1: String,value2: String,value3: String,value4: String,value5: String,value6: String,value7: String, value8: String,
        positiveButton: String,
        negativeButton: String,
        isCancellable: Boolean,
        listener: AlertDialogInterface,
        textChangeListener: OnTextViewTextChangeListener? = null
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.fragment_karigar_beeds_item, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setCancelable(isCancellable)
        dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT)

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.tv_beeds_stones.text = message1
        layout.tv_qty.text = message2
        layout.tv_carat.text = message3
        layout.tv_gr_wt.text = message4
        layout.tv_less.text = message5
        layout.tv_rate_carat.text = message6
        layout.tv_rate_gr.text = message7
        layout.tv_value.text = message8

        layout.ed_beeds_stones.hint = value1
        layout.ed_qty.hint = value2
        layout.ed_carat.hint = value3
        layout.ed_gr_wt.hint = value4
        layout.ed_less.hint = value5
        layout.ed_rate_carat.hint = value6
        layout.ed_rate_gr.hint = value7
        layout.ed_value.hint = value8
        layout.bt_new_stock_done.text = positiveButton
        layout.bt_new_stock_cancel.text = negativeButton

        layout.ed_beeds_stones.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textChangeListener?.onTextChange(layout.title, layout.ed_beeds_stones.text.toString())
            }
        })

        layout.bt_new_stock_cancel.setOnClickListener {
            listener.onCancel(dialog)
        }
        layout.bt_new_stock_done.setOnClickListener {
            if (layout.ed_beeds_stones.text!!.isNotEmpty() ||layout.ed_qty.text!!.isNotEmpty()||layout.ed_carat.text!!.isNotEmpty()||layout.ed_gr_wt.text!!.isNotEmpty()||layout.ed_less.text!!.isNotEmpty()||layout.ed_rate_carat.text!!.isNotEmpty()||layout.ed_rate_gr.text!!.isNotEmpty()||layout.ed_value.text!!.isNotEmpty() ) {
                listener.onProceed(dialog, layout.ed_beeds_stones.text.toString())
                listener.onProceed(dialog, layout.ed_qty.text.toString())
                listener.onProceed(dialog, layout.ed_carat.text.toString())
                listener.onProceed(dialog, layout.ed_gr_wt.text.toString())
                listener.onProceed(dialog, layout.ed_less.text.toString())
                listener.onProceed(dialog, layout.ed_rate_carat.text.toString())
                listener.onProceed(dialog, layout.ed_rate_gr.text.toString())
                listener.onProceed(dialog, layout.ed_value.text.toString())
            } else {
                toast(context = context, message = "Please add Category Names.")
            }
        }

        return dialog
    }

    fun alertBalance(
        context: Context,
        title: String,
        message: Double,
        listener: AlertDialogInterface
    ): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.alert_bank_openingbalz, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.setTitle("")

        dialog.setOnDismissListener { listener.onCancel(dialog) }

        layout.bank_title.text = title
        layout.bank_text.text = message.toString()

        return dialog
    }

}