package com.wholesale.jewels.fauxiq.baheekhata.utils

import android.text.Editable

object Validations {

    private const val MIN_NAME_TEXT_LENGTH: Int = 2
    private const val MOBILE_NUMBER_LENGTH: Int = 10
    private const val PIN_CODE_LENGTH: Int = 6
    private const val GSTIN_LENGTH: Int = 15
    private const val MAX_NAME_TEXT_LENGTH: Int = 50
    private const val MAX_NAME_TEXT_LENGTH_PEOPLE: Int = 25
    private const val MAX_PLAIN_TEXT_LENGTH: Int = 200

    fun validName(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty text")
        }

        if (text.length < MIN_NAME_TEXT_LENGTH) {
            return Pair(false, "Required a name with min length of $MIN_NAME_TEXT_LENGTH")

        }

        if (MAX_NAME_TEXT_LENGTH < text.length) {
            return Pair(false, "Required a name with max length of $MAX_NAME_TEXT_LENGTH")
        }

        if (!text.matches(Regex("[a-zA-Z0-9 ]*"))) {
            return Pair(false, "Special characters not allowed")
        }
        return Pair(true, text.toString())
    }

    fun validName(text: String?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty text")
        }

        if (text.length < MIN_NAME_TEXT_LENGTH) {
            return Pair(false, "Required a name with min length of $MIN_NAME_TEXT_LENGTH")

        }
        return Pair(true, text)
    }

    fun validSearchText(text: Editable): Boolean = text.isNotEmpty()

    fun validMelt(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty melt")
        }

        val split = text.toString().split('.')

        if (text.getDouble() <= Double.ZERO) {
            return Pair(false, "Melt should be greater than 0")
        }

        if (split.isNotEmpty() && 3 < split[0].length) {
            return Pair(false, "Invalid percentage")
        }

        if (1 < split.size && 2 < split[1].length) {
            return Pair(false, "Invalid percentage")
        }

        return Pair(true, text.toString())
    }

    fun validMelt(text: String?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty melt")
        }

        val split = text.toString().split('.')

        if (split.isNotEmpty() && 3 < split[0].length) {
            return Pair(false, "Invalid melt percentage")
        }

        if (1 < split.size && 2 < split[1].length) {
            return Pair(false, "Invalid melt percentage")
        }

        return Pair(true, text.toString())
    }

    fun validPercentage(text: String?): Pair<Boolean, String> {

        if (text != null && text.isNotEmpty()) {
            val split = text.toString().split('.')

            if (split.isNotEmpty() && 3 < split[0].length) {
                return Pair(false, "Invalid melt percentage")
            }

            if (1 < split.size && 2 < split[1].length) {
                return Pair(false, "Invalid melt percentage")
            }
        }

        return Pair(true, text.toString())
    }

    fun validLowStock(text: Editable?): Pair<Boolean, String> {

        if (text != null || text.toString().isNotEmpty()) {

            val split = text.toString().split('.')

            if (split.isNotEmpty() && 4 < split[0].length) {
                return Pair(false, "Invalid low stock")
            }

            if (1 < split.size && 3 < split[1].length) {
                return Pair(false, "Invalid low stock")
            }
        }
        return Pair(true, text.toString())
    }

    fun validItemDescription(text: Editable?): Pair<Boolean, String> {

        if (text != null || text.toString().isNotEmpty()) {
            if (MAX_PLAIN_TEXT_LENGTH < text!!.length) {
                return Pair(false, "Required a text with max length of $MAX_PLAIN_TEXT_LENGTH")
            }
        }
        return Pair(true, text.toString())
    }

    fun validTouchPercentage(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty melt")
        }

        val split = text.toString().split('.')

        if (split.isNotEmpty() && 3 < split[0].length) {
            return Pair(false, "Invalid touch percentage")
        }

        if (1 < split.size && 2 < split[1].length) {
            return Pair(false, "Invalid touch percentage")
        }

        return Pair(true, text.toString())
    }

    fun validMobileNumber(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty text")
        }

        if (text.length != MOBILE_NUMBER_LENGTH) {
            return Pair(false, "Required a $MOBILE_NUMBER_LENGTH digit mobile number")
        }

        if (!text.matches(Regex("^[0-9]{10}"))) {
            return Pair(false, "Invalid mobile number")
        }

        return Pair(true, text.toString())
    }

    fun validMobileNumberOptional(text: Editable?): Pair<Boolean, String> {

        if (text != null && text.toString().isNotEmpty()) {

            if (text.length != MOBILE_NUMBER_LENGTH) {
                return Pair(false, "Required a $MOBILE_NUMBER_LENGTH digit mobile number")
            }

            if (!text.matches(Regex("^[0-9]{10}"))) {
                return Pair(false, "Invalid mobile number")
            }
        }
        return Pair(true, text.toString())
    }

    fun validPinCode(text: Editable?): Pair<Boolean, String> {

        if (text != null && text.toString().isNotEmpty()) {

            if (text.length != PIN_CODE_LENGTH) {
                return Pair(false, "Required a $PIN_CODE_LENGTH digit pincode")
            }

            if (!text.matches(Regex("^[0-9 ]*"))) {
                return Pair(false, "Invalid pincode")
            }
        }
        return Pair(true, text.toString())
    }

    fun validGSTIN(text: Editable?): Pair<Boolean, String> {

        if (text != null && text.toString().isNotEmpty()) {

            if (text.length != GSTIN_LENGTH) {
                return Pair(false, "Required a $GSTIN_LENGTH digit gstin")
            }

            if (!text.matches(Regex("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"))) {
                return Pair(false, "Invalid gstin")
            }
        }
        return Pair(true, text.toString())
    }

    fun validEmail(text: Editable?): Pair<Boolean, String> {

        if (text != null && text.toString().isNotEmpty()) {

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(text.toString().replace(" ", "")).matches().not()) {
                return Pair(false, "Required a valid email")
            }
        }
        return Pair(true, text.toString())
    }

    fun validAccountNumber(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required Account Number")
        }

        return Pair(true, text.toString())
    }
    fun validAmount(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required Amount")
        }

        return Pair(true, text.toString())
    }

    fun validIfscCode(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required IFSC Code")
        }

        return Pair(true, text.toString())
    }

    fun validPassword(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required Password")
        }

        return Pair(true, text.toString())
    }

    fun validBeeds(text: Editable?): Pair<Boolean, String> {

        if (text == null || text.isEmpty()) {
            return Pair(false, "Required a non empty text")
        }

        return Pair(true, text.toString())
    }

    fun validCustomerStartBillAmount(text: String?): Pair<Boolean, String> {
        if (text == null || text.isEmpty()) {
            return Pair(false, "Enter start Amount")
        }
        return Pair(true, text)
    }
    fun validCustomerEndBillAmount(text: String?): Pair<Boolean, String> {
        if (text == null || text.isEmpty()) {
            return Pair(false, "Enter end Amount")
        }
        return Pair(true, text)
    }

    fun validNetWeight(netWeight: CharSequence): Boolean =
        netWeight.isEmpty() || (netWeight.isNotEmpty() && netWeight.toString().toDouble() >= 0.0)

    fun validTotalWeight(totalWeight: CharSequence): Boolean =
        totalWeight.isEmpty() || (totalWeight.isNotEmpty() && totalWeight.toString().toDouble() >= 0.0)

    fun validNetWeight(netWeight: String): Boolean =
        netWeight.isEmpty() || (netWeight.isNotEmpty() && netWeight.toString().toDouble() >= 0.0)

    fun validTotalWeight(totalWeight: String): Boolean =
        totalWeight.isEmpty() || (totalWeight.isNotEmpty() && totalWeight.toString().toDouble() >= 0.0)
}
