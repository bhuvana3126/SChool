package com.wholesale.jewels.fauxiq.baheekhata.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.wholesale.jewels.fauxiq.baheekhata.R
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.ImageIntentAlertInteractionListener
import com.wholesale.jewels.fauxiq.baheekhata.interfaces.OnDateSetListener
import kotlinx.android.synthetic.main.image_intent_alert.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author binil on 04/11/18 for Baheekhata app.
 */


object Utils {

    @JvmStatic
    val DATE_FORMAT_dd_MMM_yyyy_HH_mm = "dd-MM-yyyy hh:mm aa"

    @JvmStatic
    val DATE_FORMAT_dd_MMM_yyyy = "dd MMM yyyy"

    @JvmStatic
    val DATE_FORMAT_dd_MM_yyyy = "dd-MM-yyyy"

    @JvmStatic
    val DATE_FORMAT_dd_MM_yyyy_HIPHEN = "dd/MM/yyyy"


    val DAY_START: Int = 0
    val DAY_END: Int = 1

    fun getPx(context: Context?, dp: Int): Int {

        if (context == null) {
            throw NullPointerException("Context cannot be null")
        }

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
            .toInt()
    }

    fun textCapSentences(text: String): String? {

        if (text.isEmpty()) return null

        val c = text[0].toString()
        return text.replace(c, c.toUpperCase())
    }

    fun roundTo2F(d: Double): Double {

        var d2 = d
        d2 += 0.005
        val i = (d2 * 100).toInt()

        return (i / 100.0)
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(
            target.toString().replace(
                " ",
                ""
            )
        ).matches()
    }

    fun color(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun string(mContext: Context, string: Int): String {
        return mContext.resources.getString(string)
    }

    fun drawables(context: Context, drawable: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawable)
    }

    fun hideKeyboard(activity: Activity) {

        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus

        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboard(fragment: androidx.fragment.app.Fragment) {

        if (fragment.context == null) {
            throw java.lang.NullPointerException("NonNull context is required. Usually happens when you invoke this method after calling 'onBackPressed' in a Fragment")
        }

        val imm = fragment.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val token = fragment.view?.rootView?.windowToken
        imm.hideSoftInputFromWindow(token, 0)




    }
    @JvmStatic
    fun format(timeInMillis: Long, format: String): String {

        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val instance = Calendar.getInstance()
        instance.timeInMillis = timeInMillis
        return sdf.format(instance.time)
    }

    fun getCurrentDate_dd_MMM_yyyy(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT_dd_MMM_yyyy, Locale.ENGLISH)
        return sdf.format(Calendar.getInstance().time)
    }

    fun getCurrentDate(): Long {
        return Calendar.getInstance().time.time
    }

    fun datePicker(context: Context, listener: OnDateSetListener): DatePickerDialog {

        val calendar = Calendar.getInstance()

        return DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                listener.onDateSet(view, calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun datePicker(context: Context, date: Long?, listener: OnDateSetListener): DatePickerDialog {

        val calendar = Calendar.getInstance()
        date?.let {
            calendar.timeInMillis = date
        }

        return DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                listener.onDateSet(view, calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun filterOneDay(): Pair<Long, Long> {

        val calendar = Calendar.getInstance()

        val toDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 0)
        val fromDate = calendar.timeInMillis

        return Pair(fromDate, toDate)
    }

    fun filterOneWeek(): Pair<Long, Long> {

        val calendar = Calendar.getInstance()

        val toDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, -1)
        val fromDate = calendar.timeInMillis

        return Pair(fromDate, toDate)
    }

    fun filterOneMonth(): Pair<Long, Long> {

        val calendar = Calendar.getInstance()

        val toDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, -1)
        val fromDate = calendar.timeInMillis

        return Pair(fromDate, toDate)
    }

    fun filterThreeMonth(): Pair<Long, Long> {

        val calendar = Calendar.getInstance()

        val toDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, -3)
        val fromDate = calendar.timeInMillis

        return Pair(fromDate, toDate)
    }

    fun setTime(date: Long, time: Int): Long {

        val calendar = Calendar.getInstance()

        calendar.timeInMillis = date

        when (time) {
            DAY_START -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
            }
            DAY_END -> {
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
            }
        }

        return calendar.timeInMillis
    }

    fun imageIntentAlert(context: Context, listener: ImageIntentAlertInteractionListener): Dialog {

        val layout = LayoutInflater.from(context).inflate(R.layout.image_intent_alert, null)

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.button_bg_stroke_only)
        dialog.setTitle("")
        dialog.setCancelable(true)

        dialog.take_images.setOnClickListener {
            listener.onCaptureImage()
            dialog.dismiss()
        }

        dialog.pick_images.setOnClickListener {
            listener.onPickImage()
            dialog.dismiss()
        }

        dialog.delete_images.setOnClickListener {
            listener.onDeleteImage()
            dialog.dismiss()
        }

        return dialog
    }

    fun sendEmail(context: Context, addresses: Array<String>, subject: String) {

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            ContextCompat.startActivity(context, intent, null)
        }
    }

    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun dimen(context: Context, dimen: Int): Float =
        context.resources.getDimension(dimen)

    fun loadJSONFromAsset(context: Context, file: String): String? {
        val json: String?
        try {
            val `is` = context.assets.open(file)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    /*fun loadJSONFromInternal(context: Context, file: String): String? {
        val json: String?
        try {
            val `is` = File(
                "${Environment.getExternalStorageDirectory()}${File.separator}$path"
            )
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }*/

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        return image
    }

    fun generateRandomOtp(): String = (Random().nextInt(10000 - 1000) + 1000).toString()

    fun getExpDate(referral_id: String): String {

        val calendar: Calendar = Calendar.getInstance()

        calendar.add(Calendar.YEAR, if (referral_id.isNotEmpty()) 1 else 0)
        calendar.add(Calendar.MONTH, if (referral_id.isEmpty()) 1 else 0)

        val sdf = SimpleDateFormat(DATE_FORMAT_dd_MM_yyyy_HIPHEN, Locale.ENGLISH)
        return sdf.format(calendar.time)
    }

    fun getRegDate(): String {

        val calendar: Calendar = Calendar.getInstance()

        val sdf = SimpleDateFormat(DATE_FORMAT_dd_MM_yyyy_HIPHEN, Locale.ENGLISH)
        return sdf.format(calendar.time)
    }

    fun getExpDateInMillis(referral_id: String): Long {

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, if (referral_id.isNotEmpty()) 1 else 0)
        calendar.add(Calendar.MONTH, if (referral_id.isEmpty()) 1 else 0)

        return calendar.time.time
    }

    fun getRegDateInMillis(): Long {

        val calendar: Calendar = Calendar.getInstance()
        return calendar.time.time
    }

    fun generateRandomPassword(): String {
        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
        val str = StringBuilder()
        val random = Random()
        for (i in 0..5) {
            str.append(chars[random.nextInt(chars.size)])
        }
        return str.toString()
    }

    fun splitTextIntoTwo(text: String, minLength: Int = 0): Pair<String, String> {
        println("SplitTextIntoTwo: text = $text")

        if (text.length <= minLength) {
            println("SplitTextIntoTwo: [$text,null]")
            return Pair(text, "")
        }

        var centre = text.length / 2
        while (text.toCharArray()[centre] != ' ' && text.toCharArray()[centre] != ',') {
            centre--
        }

        println("SplitTextIntoTwo: [${text.substring(0, centre + 1)},${text.substring(centre + 1)}]")
        return Pair(text.substring(0, centre + 1), text.substring(centre + 1))
    }

    //Invoice sharing

    fun sharePdfFile(activity: Activity,filePath:String){
        val file = File(filePath)
        try {
            val uri = FileProvider.getUriForFile(activity,"com.wholesale.jewels.fauxiq.baheekhata.fileprovider",file)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "application/pdf"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            activity.startActivity(Intent.createChooser(shareIntent, "Share Invoice"))
        }
        catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    fun sharePdfFileAndroidR(activity: Activity,filePath:File){
        try {
            val uri = FileProvider.getUriForFile(activity,"com.mortgage.fauxiq.pawnbroker.fileprovider",filePath)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "application/pdf"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            activity.startActivity(Intent.createChooser(shareIntent, "Share Invoice"))
        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }
    @SuppressLint("RestrictedApi")
    fun showCustomerDialog(imageBitmap: Bitmap?, context: Context) {
        val dialog = Dialog(context)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.layout)
        val cancel = dialog.findViewById<ImageView>(R.id.cross_dismiss)
        val image = dialog.findViewById<ImageView>(R.id.customer_Image)
        imageBitmap?.let {
            image.setImageBitmap(it)
        }?:run{
            image.setImageDrawable(getDrawable(context,R.drawable.placeholder))
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog .show()

    }

   /* fun getDouble(str: String): Double {
        try {
            return java.lang.Double.parseDouble(str)
        } catch (e: Exception) {
            return 0.0
        }
    }
    fun getWeightString(value: Double): String {
        return DecimalFormat("0.000").format(value)
    }
    fun getRsString(value: Double): String {
        return DecimalFormat("##,##,##,##0.00").format(value)
    }*/

    fun getTomorrowDateByStr (dueDate: String): Long {
        val mills_in_day: Int = 1000 * 60 * 60 * 24
        val sdf = SimpleDateFormat(DATE_FORMAT_dd_MM_yyyy, Locale.ENGLISH)
        val dateSelectedFrom: Date = sdf.parse(dueDate)
        val nextDate : String = sdf .format(dateSelectedFrom.getTime() + mills_in_day)
        val dateNextDate: Date = sdf.parse(nextDate)
        return dateNextDate.time
    }

    fun getTomorrowDate (dueDate: Long) : Long{
        val mills_in_day: Int = 1000 * 60 * 60 * 24
        val sdf = SimpleDateFormat(DATE_FORMAT_dd_MM_yyyy, Locale.ENGLISH)
        val dateSelectedFrom: Date = sdf.parse(Utils.format(dueDate, Utils.DATE_FORMAT_dd_MM_yyyy))
        val nextDate : String = sdf .format(dateSelectedFrom.getTime() + mills_in_day)
        val dateNextDate: Date = sdf.parse(nextDate)
        return dateNextDate.time
    }

}
