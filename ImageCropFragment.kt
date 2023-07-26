package com.wholesale.jewels.fauxiq.baheekhata.ui.components

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.databinding.FragmentImageCropBinding
import com.wholesale.jewels.fauxiq.baheekhata.dto.ToolbarContent
import com.wholesale.jewels.fauxiq.baheekhata.room.converters.ImageBitmapConverters
import com.wholesale.jewels.fauxiq.baheekhata.ui.company.AddEditCompany
import com.wholesale.jewels.fauxiq.baheekhata.ui.customer.AddEditCustomer
import com.wholesale.jewels.fauxiq.baheekhata.ui.karigar.karigarAdd.ItemKarigarFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.karigar.karigarAdd.ItemKarigarEditFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.karigar.karigarAdd.KarigarItemFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.purchase.new_purchase.ItemPurchaseFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.returnpurchase.ReturnItemPurchaseFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.returnsale.ReturnItemSaleFragment
import com.wholesale.jewels.fauxiq.baheekhata.ui.sales.new_sale.ItemSalesFragment
import com.wholesale.jewels.fauxiq.baheekhata.utils.Utils
import kotlinx.android.synthetic.main.fragment_image_crop.*
import java.io.ByteArrayOutputStream

class ImageCropFragment : androidx.fragment.app.Fragment() {

    private var imageFilePath: String? = null
    private var imageBitmap: Bitmap? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageFilePath = it.getString(ARG_IMAGE_FILE_PATH)
            imageBitmap = ImageBitmapConverters.toBitmap(it.getByteArray(ARG_IMAGE_BITMAP))
        }
    }

    private lateinit var binding: FragmentImageCropBinding

    private val onCropDoneListener: View.OnClickListener = View.OnClickListener {

        if (!image.isChangingScale) {

            if (targetFragment != null) {
                when (targetFragment) {
                    is AddEditCustomer -> (targetFragment as AddEditCustomer).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is AddEditCompany -> (targetFragment as AddEditCompany).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is ItemSalesFragment -> (targetFragment as ItemSalesFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is ItemPurchaseFragment -> (targetFragment as ItemPurchaseFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is ReturnItemPurchaseFragment -> (targetFragment as ReturnItemPurchaseFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is ReturnItemSaleFragment -> (targetFragment as ReturnItemSaleFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is ItemKarigarFragment -> (targetFragment as ItemKarigarFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is KarigarItemFragment -> (targetFragment as KarigarItemFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                    is ItemKarigarEditFragment -> (targetFragment as ItemKarigarEditFragment).onCropResult(
                        resizeBitmap(
                            context!!,
                            image.croppedImage
                        )
                    )
                }
            }
            listener?.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_image_crop,
                container,
                false
            )
        val myView: View = binding.root

        binding.onCropDoneListener = onCropDoneListener

        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbarContent = ViewModelProviders.of(activity!!).get(ToolbarContent::class.java)
        toolbarContent.currentTitle.value = null
        toolbarContent.showOptionsMenu.value = false
        toolbarContent.showToolbarButton.value = false
    }


    private fun initUi() {

        if (imageFilePath != null)
            image.setImageFilePath(imageFilePath)
        else
            image.setImageBitmap(imageBitmap)

        image.setAspectRatio(1, 1)
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
        fun onBackPressed()
    }

    companion object {
        private const val ARG_IMAGE_FILE_PATH: String = "ImageFilePath"
        private const val ARG_IMAGE_BITMAP: String = "ImageBitmap"
        const val CLASS_SIMPLE_NAME = "ImageCropFragment"

        @JvmStatic
        fun newInstance(filePath: String) =
            ImageCropFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_FILE_PATH, filePath)
                }
            }

        @JvmStatic
        fun newInstance(bitmap: Bitmap?) =
            ImageCropFragment().apply {
                arguments = Bundle().apply {
                    putByteArray(
                        ARG_IMAGE_BITMAP,
                        toByteArray(bitmap)
                    )
                }
            }

        private fun toByteArray(image: Bitmap?): ByteArray? {

            if (image == null) return ByteArray(0)

            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }

        private fun resizeBitmap(context: Context, image: Bitmap): Bitmap {

            val (newWidth, newHeight) = getCompressedSize(
                context,
                image
            )

            return ImageBitmapConverters.getResizedBitmap(
                bm = image,
                newWidth = newWidth,
                newHeight = newHeight
            )
        }

        private fun getCompressedSize(context: Context, image: Bitmap): Pair<Int, Int> {

            val width = image.width
            val height = image.height

            return if (width <= height)
                Pair((Utils.getPx(context = context,  dp = 240).toDouble() / height * width).toInt(), Utils.getPx(context = context,  dp = 240)) else
                Pair(Utils.getPx(context = context,  dp = 240), (Utils.getPx(context = context,  dp = 240).toDouble() / width * height).toInt())

        }
    }
}
