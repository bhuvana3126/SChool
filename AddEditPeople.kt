
package com.wholesale.jewels.fauxiq.baheekhata.ui.customer

import android.app.Activity.RESULT_OK
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.wholesale.jewels.fauxiq.baheekhata.databinding.FragmentAddEditPeopleBinding
import com.wholesale.jewels.fauxiq.baheekhata.dto.FineWeight
import com.wholesale.jewels.fauxiq.baheekhata.dto.ToolbarContent
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.People
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.PeoplesFineWeight
import com.wholesale.jewels.fauxiq.baheekhata.enums.Metal
import com.wholesale.jewels.fauxiq.baheekhata.enums.MetalType
import com.wholesale.jewels.fauxiq.baheekhata.enums.PeopleType
import com.wholesale.jewels.fauxiq.baheekhata.enums.getMetalTypes
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.ImageIntentAlertInteractionListener
import com.wholesale.jewels.fauxiq.baheekhata.repo.PeopleDbRepo
import com.wholesale.jewels.fauxiq.baheekhata.room.AppDatabase
import com.wholesale.jewels.fauxiq.baheekhata.ui.customer.people_details_fragment.PeopleDetailFragment
import com.wholesale.jewels.fauxiq.baheekhata.utils.*
import kotlinx.android.synthetic.main.fragment_add_edit_people.*
import kotlinx.android.synthetic.main.item_add_cutomer_fine_wt.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File
import java.io.IOException


private const val ARG_CUSTOMER = "customer"

class AddEditCustomer : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var metalTypes: ArrayList<MetalType>
    private lateinit var mPeople: People
    private var listener: OnFragmentInteractionListener? = null

    val database: AppDatabase by inject(AppDatabase.CURRENT)

    private val onCancelClickListener: View.OnClickListener = View.OnClickListener {

        Utils.hideKeyboard(this)
        listener?.onBackPressed()
    }

    private val onDoneClickListener: View.OnClickListener = View.OnClickListener {

        Utils.hideKeyboard(this)

        if (validateEntries()) {
            getEntries()

            GlobalScope.launch(Dispatchers.IO) {
                PeopleDbRepo.addEditCustomer(database = database, people = mPeople, type = mPeople.people_kind)
            }

            when (targetFragment) {
                is PeopleDetailFragment -> (targetFragment as PeopleDetailFragment).updateCustomer(mPeople)
            }

            listener?.onBackPressed()
        }
    }

    private fun getEntries(): People {

        mPeople.name = getCustomerName()
        mPeople.company_name = getCompanyName()
        mPeople.mobile_number = getMobileNumber()
        mPeople.alternative_phone_number = getAlterMobileNumber()
        mPeople.gstin_number = gatin.text.toString()
        mPeople.email = email.text.toString()
        mPeople.building_area = building_area.text.toString()
        mPeople.city = city.text.toString()
        mPeople.state = state.text.toString()
        mPeople.pin_code = getPinCode()
        if (imageChanged) {
            mPeople.image = (image_customer.drawable as BitmapDrawable).bitmap
        }

        val balAmount = getBalanceAmount()
        mPeople.bal_amount = if (rb_people_amount_debit.isChecked) -balAmount else balAmount

        val balInterestAmount = getBalanceInterestAmount()
        mPeople.bal_interest_amount =
            if (rb_people_interest_amount_debit.isChecked) -balInterestAmount else balInterestAmount

        return mPeople
    }

    private fun getCustomerName(): String = customer_name.text.toString()
    private fun getCompanyName(): String = company_name.text.toString()

    private fun getMobileNumber(): Long =
        if (phone_number.text.toString().isNotEmpty()) phone_number.text.toString().toLong() else 0L

    private fun getAlterMobileNumber(): Long =
        if (alter_phone_number.text.toString().isNotEmpty()) alter_phone_number.text.toString().toLong() else 0L

    private fun getPinCode(): Long =
        if (pin_code.text.toString().isNotEmpty()) pin_code.text.toString().toLong() else 0L

    private fun getBalanceAmount(): Double =
        if (amount.text.toString().isNotEmpty()) amount.text.toString().toDouble() else DOUBLE_DEFAULT_VALUE

    private fun getBalanceInterestAmount(): Double =
        if (intrest_amount.text.toString().isNotEmpty()) intrest_amount.text.toString().toDouble() else DOUBLE_DEFAULT_VALUE

    private val onAddressVisibleListener: View.OnClickListener = View.OnClickListener {

        TransitionManager.beginDelayedTransition(binding.root as ConstraintLayout)

        building_area_layout.visibility = if (it.isSelected) View.VISIBLE else View.GONE
        city_layout.visibility = if (it.isSelected) View.VISIBLE else View.GONE
        state_layout.visibility = if (it.isSelected) View.VISIBLE else View.GONE
        pin_code_layout.visibility = if (it.isSelected) View.VISIBLE else View.GONE

        it.isSelected = it.isSelected.not()
        tv_new_address_details.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (it.isSelected) com.wholesale.jewels.fauxiq.baheekhata.R.drawable.ic_arrow_down else com.wholesale.jewels.fauxiq.baheekhata.R.drawable.ic_arrow_up,
            0
        )
    }


    private val onCustomerImageClickListener: View.OnClickListener = View.OnClickListener {

        Utils.imageIntentAlert(context!!, object : ImageIntentAlertInteractionListener {
            override fun onCaptureImage() {
//                mPeople.image = null

                AppPermissions.instance().checkPermission(
                    activity = activity!!,
                    permission = AppPermissions.PERMISSION_CAMERA,
                    permissionGrandListener = object : AppPermissions.PermissionGrandListener {
                        override fun onGrand() {

                            val imageFile = Utils.createImageFile(context = context!!)
                            currentPhotoPath = imageFile.absolutePath
                            println("Image file path: " + imageFile.absolutePath)

                            listener?.captureImageIntent(
                                imageFile = imageFile,
                                requestCode = REQUEST_IMAGE_CAPTURE
                            )
                        }

                        override fun onDenied() {
                            Toast.makeText(context, com.wholesale.jewels.fauxiq.baheekhata.R.string.permission_denied, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }

            override fun onPickImage() {
//                mPeople.image = null

                AppPermissions.instance().checkPermission(
                    activity = activity!!,
                    permission = AppPermissions.PERMISSION_EXTERNAL_STORAGE,
                    permissionGrandListener = object : AppPermissions.PermissionGrandListener {
                        override fun onGrand() {
                            listener?.pickImageIntent()
                        }

                        override fun onDenied() {
                            Toast.makeText(context, com.wholesale.jewels.fauxiq.baheekhata.R.string.permission_denied, Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }

            override fun onDeleteImage() {
                mPeople.image = null
                image_customer.setImageResource(com.wholesale.jewels.fauxiq.baheekhata.R.drawable.placeholder)
            }
        }).show()
    }

    private val isCustomerAlsoCheckListener = object : OnCheckedChangeListener {
        override fun onCheckedChange(id: Int, isChecked: Boolean) {

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
//            mPeople.purchase_unstock = isChecked
            if (isChecked){
                mPeople.people_kind = PeopleType.PCTOGETHER
            }

        }
    }

    private val onAddFineWeight: View.OnClickListener = View.OnClickListener {

        val fineWeightText = fine_weight.text.toString()
        if (fineWeightText.isNotEmpty()) {

            addFineWeight(fineWeightText)
        } else {
            toast(context = context!!, message = "Enter a fine weight")
        }
    }

    private fun addFineWeight(fineWeightText: String) {

        val fineWeight = FineWeight(
            if (rd_add_fine_weight_debit.isChecked) -fineWeightText.toDouble() else fineWeightText.toDouble(),
            selectedMetalType.id
        )

        val isFineWt = fine_weight_rbtn.isChecked

        if (isFineWt)
            if (mPeople.isFineWeightExist(fineWeight).not()) {

                mPeople.addFineWeight(metalType = fineWeight.metalType, fineWt = fineWeight.fineWt, isFineWt = isFineWt)
            } else {
                toast(context = context!!, message = "Already exist")
                return
            }

        if (isFineWt.not())
            if (mPeople.isInterestFineWeightExist(fineWeight).not()
            ) {
                mPeople.addFineWeight(metalType = fineWeight.metalType, fineWt = fineWeight.fineWt, isFineWt = isFineWt)
            } else {
                toast(context = context!!, message = "Already exist")
                return
            }

        val fineWeightItem =
            LayoutInflater.from(context).inflate(com.wholesale.jewels.fauxiq.baheekhata.R.layout.item_add_cutomer_fine_wt, null)

        fineWeightItem.metal.text = getMetalTypes(fineWeight.metalType).toString()
        fineWeightItem.weight.text =fineWeight.fineWt.toString(3)

        fineWeightItem.metal.setTypeface(
            fineWeightItem.metal.typeface,
            if (isFineWt) Typeface.BOLD else Typeface.ITALIC
        )
        fineWeightItem.weight.setTypeface(
            fineWeightItem.weight.typeface,
            if (isFineWt) Typeface.BOLD else Typeface.ITALIC
        )

        fineWeightItem.remove.setOnClickListener {

            mPeople.removeFineWeight(metalType = fineWeight.metalType, fineWt = fineWeight.fineWt, isFineWt = isFineWt)
            add_fine_wt_layout.removeView(fineWeightItem)
        }

        add_fine_wt_layout.addView(fineWeightItem)
        fineWeightItem.requestFocus()
        fine_weight.text = null
    }

    private fun addFineWeight(metalType: String, fineWt: Double, isFineWeight: Boolean) {

        if (fineWt == Double.ZERO) return

        val fineWeightItem =
            LayoutInflater.from(context).inflate(com.wholesale.jewels.fauxiq.baheekhata.R.layout.item_add_cutomer_fine_wt, null)

        fineWeightItem.metal.text = getMetalTypes(metalType).toString()
        fineWeightItem.weight.text = StringFormat.format(fineWt, StringFormat.DECIMAL_FORMAT_000)

        fineWeightItem.metal.setTypeface(
            fineWeightItem.metal.typeface,
            if (isFineWeight) Typeface.BOLD else Typeface.ITALIC
        )
        fineWeightItem.weight.setTypeface(
            fineWeightItem.weight.typeface,
            if (isFineWeight) Typeface.BOLD else Typeface.ITALIC
        )

        fineWeightItem.remove.setOnClickListener {

            mPeople.removeFineWeight(metalType = metalType, fineWt = fineWt, isFineWt = isFineWeight)
            add_fine_wt_layout.removeView(fineWeightItem)
        }

        add_fine_wt_layout.addView(fineWeightItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mPeople = it.getParcelable(ARG_CUSTOMER)!!
        }
    }

    private lateinit var binding: FragmentAddEditPeopleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                com.wholesale.jewels.fauxiq.baheekhata.R.layout.fragment_add_edit_people,
                container,
                false
            )

        binding.people = mPeople
        binding.onCancelClickListener = onCancelClickListener
        binding.onDoneClickListener = onDoneClickListener
        binding.onAddressVisibleListener = onAddressVisibleListener
        binding.onAddFineWeight = onAddFineWeight
        binding.onCustomerImageClickListener = onCustomerImageClickListener
        binding.isCustomerAlsoCheckListener = isCustomerAlsoCheckListener


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onAddressVisibleListener.onClick(tv_new_address_details)
        setMetalTypeSpinner()
        createMetalList()

        mPeople.let {
            if (mPeople.people_kind.equals(PeopleType.PARTY)) {
                is_customer_also.visibility = View.VISIBLE
            } else {
                is_customer_also.visibility = View.GONE

            }
        }
    }

    private lateinit var toolbarContent: ToolbarContent

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarContent = ViewModelProviders.of(activity!!).get(ToolbarContent::class.java)
        toolbarContent.addToBackStack(CLASS_SIMPLE_NAME)
//        toolbarContent.currentTitle.value = CLASS_SIMPLE_NAME
        toolbarContent.showOptionsMenu.value = false
        toolbarContent.setToolbar(CLASS_SIMPLE_NAME, false, false)

    }

    private fun createMetalList() {

        GlobalScope.launch(Dispatchers.Main) {

            PeopleDbRepo.getFineWt(database = database, customerId = mPeople.id)
                .filter {
                    it.fine_wt.isNotZero() || it.interest_fine_wt.isNotZero()
                }.also {
                    mPeople.fineWeights = it as ArrayList<PeoplesFineWeight>
                }.forEach {
                    addFineWeight(
                        metalType = it.metal_type,
                        fineWt = it.fine_wt,
                        isFineWeight = true
                    )
                    addFineWeight(
                        metalType = it.metal_type,
                        fineWt = it.interest_fine_wt,
                        isFineWeight = false
                    )
                }
        }
    }

    private fun setMetalTypeSpinner() {

        // Spinner click listener
        metal_type.onItemSelectedListener = this

        metalTypes = Metal.instance.metalTypes
        selectedMetalType = metalTypes[0]

        val dataAdapter =
            ArrayAdapter<MetalType>(
                context!!,
                android.R.layout.simple_spinner_item,
                metalTypes
            )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        metal_type.adapter = dataAdapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private lateinit var selectedMetalType:MetalType

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedMetalType = metalTypes[position]
    }

    private fun validateEntries(): Boolean {

        var validation = true

        val (validName, messageName) = Validations.validName(customer_name.text)
        if (validName.not()) {
            validation = false
            toast(context = context!!, message = messageName)
        }

        val (validMobile, messageMobile) = Validations.validMobileNumber(phone_number.text)
        if (validMobile.not()) {
            validation = false
            toast(context = context!!, message = messageMobile)
        }

        val (validAlterMobile, messageAlterMobile) = Validations.validMobileNumberOptional(alter_phone_number.text)
        if (validAlterMobile.not()) {
            validation = false
            toast(context = context!!, message = messageAlterMobile)
        }

        val (validPincode, messagePincode) = Validations.validPinCode(pin_code.text)
        if (validPincode.not()) {
            validation = false
            toast(context = context!!, message = messagePincode)
        }

        val (validGSTIN, messageGSTIN) = Validations.validGSTIN(gatin.text)
        if (validGSTIN.not()) {
            validation = false
            toast(context = context!!, message = messageGSTIN)
        }

        val (validEmail, messageEmail) = Validations.validEmail(email.text)
        if (validEmail.not()) {
            validation = false
            toast(context = context!!, message = messageEmail)
        }

        return validation
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("AddEditCustomer.onActivityResult.requestCode:$requestCode resultCode:$resultCode data:$data")

        if (resultCode == RESULT_OK) {

            when (requestCode) {
                REQUEST_IMAGE_PICK -> {

                    if (data != null) {
                        val uri = data.data

                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                activity!!.contentResolver,
                                uri
                            )

                            Handler().post {
                                listener?.cropImage(bitmap, this)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {

                    Handler().post {
                        listener?.cropImage(currentPhotoPath, this)
                    }
                }
            }
        }
    }

    private var currentPhotoPath: String = ""

    private var imageChanged: Boolean = false

    fun onCropResult(bitmap: Bitmap) {

        imageChanged = true
        image_customer.setImageBitmap(bitmap)
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

    interface OnFragmentInteractionListener {
        fun onBackPressed()
        fun captureImageIntent(imageFile: File, requestCode: Int)
        fun pickImageIntent()
        fun cropImage(filePath: String, target: androidx.fragment.app.Fragment)
        fun cropImage(bitmap: Bitmap?, target: androidx.fragment.app.Fragment)
    }

    companion object {
        const val CLASS_SIMPLE_NAME: String = "AddEditPeople"
        const val REQUEST_IMAGE_CAPTURE = 201
        const val REQUEST_IMAGE_PICK = 202

        @JvmStatic
        fun newInstance(people: People) =
            AddEditCustomer().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CUSTOMER, people)
                }
            }
    }
}
