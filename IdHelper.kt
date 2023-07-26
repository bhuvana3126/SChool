package com.wholesale.jewels.fauxiq.baheekhata.utils

import com.wholesale.jewels.fauxiq.baheekhata.enums.PeopleType

class IdHelper {

    companion object {

        const val MAX_FOUR_DIGIT: Int = 9999

        private const val ITEM_SALE_INITIAL = "IS"
        private const val ITEM_PURCHASE_INITIAL = "IP"
        private const val ITEM_KARIGAR_INITIAL = "IK"
        private const val SALE_PAYMENT_INITIAL = "SP"
        private const val PURCHASE_PAYMENT_INITIAL = "PP"
        private const val SALE_SUMMARY_INITIAL = "SS"
        private const val PURCHASE_SUMMARY_INITIAL = "PS"
        private const val KARIGAR_SUMMARY_INITIAL = "KS"
        private const val RETURN_KARIGAR_SUMMARY_INITIAL = "RKS"
        private const val KARIGAR_PAYMENT_INITIAL = "KP"
        private const val RETURN_KARIGAR_PAYMENT_INITIAL = "RKP"
        private const val ITEM_KARIGAR_BEEDS_INITIAL = "IB"
        private const val ITEM_RETURN_KARIGAR_BEEDS_INITIAL = "RB"
        private const val ITEM_RETURN_KARIGAR_INITIAL = "RK"
        private const val ITEM_KARIGAR_BEEDS_NAME_INITIAL = "BN"
        private const val NOTIFICATION_INITIAL = 'N'


        private const val STOCK_INITIAL = 'S'
        private const val ITEM_INITIAL = 'I'
        private const val CATEGORY_INITIAL = 'C'
        private const val MONEY_IO_INITIAL = 'M'
        private const val FINE_IO_INITIAL = 'F'
        private const val SUBCATEGORY_INITIAL = "SC"
        private const val CUSTOMER_INITIAL = 'C'
        private const val COMPANY_INITIAL = "CO"
        private const val PARTY_INITIAL = 'P'
        private const val EXPENSE_INITIAL = 'E'
        private const val APP_SETTINGS = 'A'
        private const val BANK_ACCOUNT = 'B'
        private const val BANK_TRANSACTION = 'T'
        private const val DEFAULT_METAL_RATE = 'D'
        private const val OLD_ENTRY = 'X'
        private const val CHECKOUT_BALANCE = "CB"
        private const val UDHAAR_AMOUNT_INITIAL = "UA"
        private const val UDHAAR_FINE_INITIAL = "UF"
        private const val CLEARANCE_BALANCE_UPDATION = "FS"

        private const val CREATE_CUSTOMER_BILL_STATUS = 'B'

        //return
        private const val RETURN_ITEM_SALE_INITIAL = "RIS"
        private const val RETURN_SALE_SUMMARY_INITIAL = "RSS"
        private const val RETURN_SALE_PAYMENT_INITIAL = "RSP"

        private const val RETURN_ITEM_PURCHASE_INITIAL = "RIP"
        private const val RETURN_PURCHASE_PAYMENT_INITIAL = "RPP"
        private const val RETURN_PURCHASE_SUMMARY_INITIAL = "RPS"


        fun createCategoryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(CATEGORY_INITIAL, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$CATEGORY_INITIAL${id.appendZeros(4)}" else
                    "$CATEGORY_INITIAL$id"
            } ?: "${CATEGORY_INITIAL}0001"

        fun createMoneyIOId(lastId: String?) =

            lastId?.let {
                val id = it.replace(MONEY_IO_INITIAL, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$MONEY_IO_INITIAL${id.appendZeros(4)}" else
                    "$MONEY_IO_INITIAL$id"
            } ?: "${MONEY_IO_INITIAL}0001"

        fun createFineIOId(lastId: String?) =

            lastId?.let {
                val id = it.replace(FINE_IO_INITIAL, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$FINE_IO_INITIAL${id.appendZeros(4)}" else
                    "$FINE_IO_INITIAL$id"
            } ?: "${FINE_IO_INITIAL}0001"

        fun createSubCategoryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(SUBCATEGORY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$SUBCATEGORY_INITIAL${id.appendZeros(4)}" else
                    "$SUBCATEGORY_INITIAL$id"
            } ?: "${SUBCATEGORY_INITIAL}0001"

        fun createItemId(lastId: String?) =

            lastId?.let {
                val id = it.replace(ITEM_INITIAL, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_INITIAL$id"
            } ?: "${ITEM_INITIAL}0001"

        fun createStockId(lastId: String?) =

            lastId?.let {
                val id = it.replace(STOCK_INITIAL, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$STOCK_INITIAL${id.appendZeros(4)}" else
                    "$STOCK_INITIAL$id"
            } ?: "${STOCK_INITIAL}0001"

        fun createSaleSummaryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(SALE_SUMMARY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$SALE_SUMMARY_INITIAL${id.appendZeros(4)}" else
                    "$SALE_SUMMARY_INITIAL$id"
            } ?: "${SALE_SUMMARY_INITIAL}0001"

        fun createReturnSaleSummaryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(RETURN_SALE_SUMMARY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_SALE_SUMMARY_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_SALE_SUMMARY_INITIAL$id"
            } ?: "${RETURN_SALE_SUMMARY_INITIAL}0001"

        fun createPurchaseSummaryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(PURCHASE_SUMMARY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$PURCHASE_SUMMARY_INITIAL${id.appendZeros(4)}" else
                    "$PURCHASE_SUMMARY_INITIAL$id"
            } ?: "${PURCHASE_SUMMARY_INITIAL}0001"

        fun createKarigarSummaryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(KARIGAR_SUMMARY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$KARIGAR_SUMMARY_INITIAL${id.appendZeros(4)}" else
                    "$KARIGAR_SUMMARY_INITIAL$id"
            } ?: "${KARIGAR_SUMMARY_INITIAL}0001"

        fun createReturnPurchaseSummaryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(RETURN_PURCHASE_SUMMARY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_PURCHASE_SUMMARY_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_PURCHASE_SUMMARY_INITIAL$id"
            } ?: "${RETURN_PURCHASE_SUMMARY_INITIAL}0001"

        fun createItemSaleId(lastId: String?) =

            lastId?.let {
                val id = it.replace(ITEM_SALE_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_SALE_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_SALE_INITIAL$id"
            } ?: "${ITEM_SALE_INITIAL}0001"

        fun createSalePaymentId(lastId: String?) =

            lastId?.let {
                val id = it.replace(SALE_PAYMENT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$SALE_PAYMENT_INITIAL${id.appendZeros(4)}" else
                    "$SALE_PAYMENT_INITIAL$id"
            } ?: "${SALE_PAYMENT_INITIAL}0001"

        fun createReturnSalePaymentId(lastId: String?) =

            lastId?.let {
                val id = it.replace(RETURN_SALE_PAYMENT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_SALE_PAYMENT_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_SALE_PAYMENT_INITIAL$id"
            } ?: "${RETURN_SALE_PAYMENT_INITIAL}0001"

        fun createPurchasePaymentId(lastId: String?) =

            lastId?.let {
                val id = it.replace(PURCHASE_PAYMENT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$PURCHASE_PAYMENT_INITIAL${id.appendZeros(4)}" else
                    "$PURCHASE_PAYMENT_INITIAL$id"
            } ?: "${PURCHASE_PAYMENT_INITIAL}0001"

        fun createReturnPurchasePaymentId(lastId: String?) =

            lastId?.let {
                val id = it.replace(RETURN_PURCHASE_PAYMENT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_PURCHASE_PAYMENT_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_PURCHASE_PAYMENT_INITIAL$id"
            } ?: "${RETURN_PURCHASE_PAYMENT_INITIAL}0001"

        fun createItemSaleId(lastId: String?, index:Int):String =

            lastId?.let {
                val id = it.replace(ITEM_SALE_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_SALE_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_SALE_INITIAL$id"
            } ?: "${ITEM_SALE_INITIAL}000${index+1}"

        fun createReturnItemSaleId(lastId: String?, index:Int):String =

            lastId?.let {
                val id = it.replace(RETURN_ITEM_SALE_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_ITEM_SALE_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_ITEM_SALE_INITIAL$id"
            } ?: "${RETURN_ITEM_SALE_INITIAL}000${index+1}"

        fun createPurchaseId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(ITEM_PURCHASE_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_PURCHASE_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_PURCHASE_INITIAL$id"
            } ?: "${ITEM_PURCHASE_INITIAL}000${index+1}"

        fun createKarigarId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(ITEM_KARIGAR_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_KARIGAR_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_KARIGAR_INITIAL$id"
            } ?: "${ITEM_KARIGAR_INITIAL}000${index+1}"

        fun createKarigarBeedsId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(ITEM_KARIGAR_BEEDS_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_KARIGAR_BEEDS_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_KARIGAR_BEEDS_INITIAL$id"
            } ?: "${ITEM_KARIGAR_BEEDS_INITIAL}000${index+1}"


        fun createKarigarBeedId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(ITEM_KARIGAR_BEEDS_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_KARIGAR_BEEDS_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_KARIGAR_BEEDS_INITIAL$id"
            } ?: "${ITEM_KARIGAR_BEEDS_INITIAL}000${index + 1}"


      fun createReturnPurchaseId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(RETURN_ITEM_PURCHASE_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_ITEM_PURCHASE_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_ITEM_PURCHASE_INITIAL$id"
            } ?: "${RETURN_ITEM_PURCHASE_INITIAL}000${index+1}"


        fun createExpenseId(lastId: String?) =

            lastId?.let {
                val id = it.replace(EXPENSE_INITIAL, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$EXPENSE_INITIAL${id.appendZeros(4)}" else
                    "$EXPENSE_INITIAL$id"
            } ?: "${EXPENSE_INITIAL}0001"

        fun createAppSetttingId(lastId: String?) =

            lastId?.let {
                val id = it.replace(APP_SETTINGS, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$APP_SETTINGS${id.appendZeros(4)}" else
                    "$APP_SETTINGS$id"
            } ?: "${APP_SETTINGS}0001"

        fun createPeopleId(lastId: String?, peopleType: PeopleType): String =

            lastId?.let {
                val id = it.replace(peopleType.id, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "${peopleType.id}${id.appendZeros(4)}" else
                    "${peopleType.id}$id"
            } ?: "${peopleType.id}0001"

        fun createCompanyId(lastId: String?): String =

            lastId?.let {
                val id = it.replace(COMPANY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$COMPANY_INITIAL${id.appendZeros(4)}" else
                    "$COMPANY_INITIAL$id"
            } ?: "${COMPANY_INITIAL}0001"

//Bank Account

        fun createBankAccountId(lastId: String?) =

            lastId?.let {
                val id = it.replace(BANK_ACCOUNT, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$BANK_ACCOUNT${id.appendZeros(4)}" else
                    "$BANK_ACCOUNT$id"
            } ?: "${BANK_ACCOUNT}0001"

        fun createTransactionId(lastId: String?) =

            lastId?.let {
                val id = it.replace(BANK_TRANSACTION, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$BANK_TRANSACTION${id.appendZeros(4)}" else
                    "$BANK_TRANSACTION$id"
            } ?: "${BANK_TRANSACTION}0001"

        fun createDefaultMetalRateId(lastId: String?) =

            lastId?.let {
                val id = it.replace(DEFAULT_METAL_RATE, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$DEFAULT_METAL_RATE${id.appendZeros(4)}" else
                    "$DEFAULT_METAL_RATE$id"
            } ?: "${DEFAULT_METAL_RATE}0001"

        fun createOldEntryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(OLD_ENTRY, Char.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$OLD_ENTRY${id.appendZeros(4)}" else
                    "$OLD_ENTRY$id"
            } ?: "${OLD_ENTRY}0001"


        fun createCheckoutId(lastId: String?) =

            lastId?.let {
                val id = it.replace(CHECKOUT_BALANCE, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$CHECKOUT_BALANCE${id.appendZeros(4)}" else
                    "$CHECKOUT_BALANCE$id"
            } ?: "${CHECKOUT_BALANCE}0001"

        fun createUdhaarAmountId(lastId: String?) =

            lastId?.let {
                val id = it.replace(UDHAAR_AMOUNT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$UDHAAR_AMOUNT_INITIAL${id.appendZeros(4)}" else
                    "$UDHAAR_AMOUNT_INITIAL$id"
            } ?: "${UDHAAR_AMOUNT_INITIAL}0001"

        fun createUdhaarFineId(lastId: String?) =

            lastId?.let {
                val id = it.replace(UDHAAR_FINE_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$UDHAAR_FINE_INITIAL${id.appendZeros(4)}" else
                    "$UDHAAR_FINE_INITIAL$id"
            } ?: "${UDHAAR_FINE_INITIAL}0001"

        fun createCustomerBillStatus(lastId: String?) = lastId?.let {
            val id = it.replace(CREATE_CUSTOMER_BILL_STATUS, Char.ZERO).toInt().plus(1)
            if (id <= MAX_FOUR_DIGIT)
                "$CREATE_CUSTOMER_BILL_STATUS${id.appendZeros(4)}" else
                "$CREATE_CUSTOMER_BILL_STATUS$id"
        } ?: "${CREATE_CUSTOMER_BILL_STATUS}0001"

        fun createClearanceBalanceUpdationId(lastId: String?) =

            lastId?.let {
                val id = it.replace(CLEARANCE_BALANCE_UPDATION, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$CLEARANCE_BALANCE_UPDATION${id.appendZeros(4)}" else
                    "$CLEARANCE_BALANCE_UPDATION$id"
            } ?: "${CLEARANCE_BALANCE_UPDATION}0001"


        fun generateBillNo(lastId: String?,prefix:String): String =
            lastId?.let{
                var id = 0
                if (it.contains(prefix)){
                    id = it.replace(prefix,String.ZERO).toInt().plus(1)
                }else{
                    id = it.replace(SALE_SUMMARY_INITIAL,String.ZERO).toInt().plus(1)
                }
                if (id <= MAX_FOUR_DIGIT)
                    "$prefix${id.appendZeros(4)}" else
                    "$prefix$id"
            } ?: "${prefix}0001"

        //karigar payment
        fun createKarigarPaymentId(lastId: String?) =

            lastId?.let {
                val id = it.replace(KARIGAR_PAYMENT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$KARIGAR_PAYMENT_INITIAL${id.appendZeros(4)}" else
                    "$KARIGAR_PAYMENT_INITIAL$id"
            } ?: "${KARIGAR_PAYMENT_INITIAL}0001"

        fun createReturnKarigarId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(ITEM_RETURN_KARIGAR_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_RETURN_KARIGAR_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_RETURN_KARIGAR_INITIAL$id"
            } ?: "${ITEM_RETURN_KARIGAR_INITIAL}000${index + 1}"

        fun createReturnKarigarBeedId(lastId: String?, index:Int = 0):String =

            lastId?.let {
                val id = it.replace(ITEM_RETURN_KARIGAR_BEEDS_INITIAL, String.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$ITEM_RETURN_KARIGAR_BEEDS_INITIAL${id.appendZeros(4)}" else
                    "$ITEM_RETURN_KARIGAR_BEEDS_INITIAL$id"
            } ?: "${ITEM_RETURN_KARIGAR_BEEDS_INITIAL}000${index + 1}"

        fun createReturnKarigarSummaryId(lastId: String?) =

            lastId?.let {
                val id = it.replace(RETURN_KARIGAR_SUMMARY_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_KARIGAR_SUMMARY_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_KARIGAR_SUMMARY_INITIAL$id"
            } ?: "${RETURN_KARIGAR_SUMMARY_INITIAL}0001"

        // return karigar payment
        fun createReturnKarigarPaymentId(lastId: String?) =

            lastId?.let {
                val id = it.replace(RETURN_KARIGAR_PAYMENT_INITIAL, String.ZERO).toInt().plus(1)

                if (id <= MAX_FOUR_DIGIT)
                    "$RETURN_KARIGAR_PAYMENT_INITIAL${id.appendZeros(4)}" else
                    "$RETURN_KARIGAR_PAYMENT_INITIAL$id"
            } ?: "${RETURN_KARIGAR_PAYMENT_INITIAL}0001"


        fun createNotificationId(lastId: String?, index:Int):String =

            lastId?.let {
                val id = it.replace(NOTIFICATION_INITIAL, Char.ZERO).toInt().plus(index+1)

                if (id <= MAX_FOUR_DIGIT)
                    "$NOTIFICATION_INITIAL${id.appendZeros(4)}" else
                    "$NOTIFICATION_INITIAL$id"
            } ?: "${NOTIFICATION_INITIAL}000${index+1}"

        fun isCustomer(id: String): Boolean = id.toCharArray()[0] == CUSTOMER_INITIAL
        fun isParty(id: String) = id.toCharArray()[0] == PARTY_INITIAL && id.toCharArray()[1].isDigit()
        fun isPurchasePayment(id: String) = id.substring(0, 2) == PURCHASE_PAYMENT_INITIAL
        fun isSalePayment(id: String) = id.substring(0, 2) == SALE_PAYMENT_INITIAL

        fun isReturnPurchasePayment(id: String) = id.substring(0, 3) == RETURN_PURCHASE_PAYMENT_INITIAL
        fun isReturnKarigarPayment(id: String) = id.substring(0, 3) == RETURN_KARIGAR_PAYMENT_INITIAL
        fun isReturnSalePayment(id: String) = id.substring(0, 3) == RETURN_SALE_PAYMENT_INITIAL

        fun isKarigarPayment(id: String) = id.substring(0, 2) == KARIGAR_PAYMENT_INITIAL

        fun getCompanyId() = "${COMPANY_INITIAL}0001"
    }
}