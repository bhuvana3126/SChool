package com.wholesale.jewels.fauxiq.baheekhata.utils

const val ITEM_FROM_STOCK = "S"
const val ITEM_FROM_STOCK_RETURN = "SR"
const val ITEM_FROM_PURCHASE = "P"

const val PURCHASE_NATURE_BULLION = "B"
const val PURCHASE_NATURE_ITEM = "I"

const val ITEM_STOCK_MAINTAIN_COMBINE_ONLY_WEIGHT = "CW"
const val ITEM_STOCK_MAINTAIN_COMBINE_BOTH_WEIGHT_QUANTITY = "CWQ"
const val ITEM_STOCK_MAINTAIN_COMBINE_ONLY_QUANTITY = "CQ"
const val ITEM_STOCK_MAINTAIN_SINGLE_ONLY_WEIGHT = "SW"
const val ITEM_STOCK_MAINTAIN_SINGLE_BOTH_WEIGHT_QUANTITY = "SWQ"
const val ITEM_STOCK_MAINTAIN_SINGLE_ONLY_QUANTITY = "SQ"

const val ITEM_PURCHASE_TYPE_TOUCH_BASED = "TB"
const val ITEM_PURCHASE_TYPE_WASTAGE_BASED = "WB"
const val ITEM_PURCHASE_TYPE_UNIT_WT_TOUCH_BASED = "UTB"
const val ITEM_PURCHASE_TYPE_UNIT_WT_WASTAGE_BASED = "UWB"
const val ITEM_PURCHASE_TYPE_DIRECT_RATE_BASED = "DRB"
const val ITEM_PURCHASE_TYPE_QTY_BASED = "QB"
const val ITEM_PURCHASE_TYPE_BULLION = "B"

const val ITEM_SALES_TYPE_TOUCH_BASED = "TB"
const val ITEM_SALES_TYPE_WASTAGE_BASED = "WB"
const val ITEM_SALES_TYPE_UNIT_WT_TOUCH_BASED = "UTB"
const val ITEM_SALES_TYPE_UNIT_WT_WASTAGE_BASED = "UWB"
const val ITEM_SALES_TYPE_DIRECT_RATE_BASED = "DRB"
const val ITEM_SALES_TYPE_QTY_BASED = "QB"
const val ITEM_SALES_TYPE_BULLION = "B"

const val ITEM_MAKING_CHARGE_AMOUNT_PER_GM = "A"
const val ITEM_MAKING_CHARGE_PERCENTAGE = "P"

const val ITEM_WASTAGE_VALUE_WEIGHT_PER_GM = "W"
const val ITEM_WASTAGE_VALUE_PERCENTAGE = "P"

const val NEW_ITEM_ID = "NEW"
const val NEW_ITEM_NAME = "ADD +"
const val NEW_ITEM_CATEGORY = "ADD"

const val DOUBLE_DEFAULT_VALUE = 0.0
const val INT_DEFAULT_VALUE = 0

const val UNKNOWN: String = "unknown"
const val DEFAULT_STORAGE_LOCATION = "WhBaheekhataAlpha/"
//const val DEFAULT_STORAGE_LOCATION = "BaheekhataWholesaleApp/"

enum class SaleStatus(val id: String, name: String) {
    STATUS_SOLD(id = "S", name = "Sold"),
    STATUS_UNSOLD(id = "U", name = "Unsold");

    override fun toString() = name
}

enum class AccountType(val id: String, name: String) {
    BUSINESS_ACCOUNT(id = "BA", name = "Business Account"),
    UDHAR_ACCOUNT(id = "UA", name = "UDHAR Account"),
    SETTLEMENT_ACCOUNT(id = "SA", name = "Settlement Account"),
    CHITTI_ACCOUNT(id = "CA", name = "Chitti Account");

    override fun toString() = name
}

enum class Scheme(val id: String, name: String) {
    SCHEME_GST(id = "G", name = "GST"),
    SCHEME_COMPOSITION(id = "C", name = "Composition");

    override fun toString() = name
}

enum class BankPeopleType(val id: String, name: String) {
    CUSTOMER(id = "C", name = "Customer"),
    PARTY(id = "P",name = "Party"),
    KARIGAR(id = "K", name = "Karigar"),
    COMPANY(id = "CO", name = "Company"),
    OTHER(id = "O", name = "Other");
    override fun toString() = name
}


enum class GSTStateCode(val id: String, name: String) {
    JAMMU_AND_KASHMIR(id = "1", name = "JAMMU AND KASHMIR"),
    HIMACHAL_PRADESH(id = "2",name = "HIMACHAL PRADESH"),
    PUNJAB(id = "3", name = "PUNJAB"),
    CHANDIGARH(id = "4", name = "CHANDIGARH"),
    UTTARAKHAND(id = "5", name = "UTTARAKHAND"),
    HARYANA(id = "6", name = "HARYANA"),
    DELHI(id = "7", name = "DELHI"),
    RAJASTHAN(id = "8", name = "RAJASTHAN"),
    UTTAR_PRADESH(id = "9", name = "UTTAR PRADESH"),
    BIHAR(id = "10", name = "BIHAR"),
    SIKKIM(id = "11", name = "SIKKIM"),
    ARUNACHAL_PRADESH(id = "12", name = "ARUNACHAL PRADESH"),
    NAGALAND(id = "13", name = "NAGALAND"),
    MANIPUR(id = "14", name = "MANIPUR"),
    MIZORAM(id = "15", name = "MIZORAM"),
    TRIPURA(id = "16", name = "TRIPURA"),
    MEGHLAYA(id = "17", name = "MEGHLAYA"),
    ASSAM(id = "18", name = "ASSAM"),
    WEST_BENGAL(id = "19", name = "WEST BENGAL"),
    JHARKHAND(id = "20", name = "JHARKHAND"),
    ODISHA(id = "21", name = "ODISHA"),
    CHATTISGARH(id = "22", name = "CHATTISGARH"),
    MADHYA_PRADESH(id = "23", name = "MADHYA PRADESH"),
    GUJARAT(id = "24", name = "GUJARAT"),
    DAMAN_AND_DIU(id = "25", name = "DAMAN AND DIU"),
    DADRA_AND_NAGAR_HAVELI(id = "26", name = "DADRA AND NAGAR HAVELI"),
    MAHARASHTRA(id = "27", name = "MAHARASHTRA"),
    ANDHRA_PRADESH_BEFORE_DIVISION(id = "28", name = "ANDHRA PRADESH(BEFORE DIVISION)"),
    KARNATAKA(id = "29", name = "KARNATAKA"),
    GOA(id = "30", name = "GOA"),
    LAKSHWADEEP(id = "31", name = "LAKSHWADEEP"),
    KERALA(id = "32", name = "KERALA"),
    TAMIL_NADU(id = "33", name = "TAMIL NADU"),
    PUDUCHERRY(id = "34", name = "PUDUCHERRY"),
    ANDAMAN_AND_NICOBAR_ISLANDS(id = "35", name = "ANDAMAN AND NICOBAR ISLANDS"),
    TELANGANA(id = "36", name = "TELANGANA"),
    ANDHRA_PRADESH_NEW(id = "37", name = "ANDHRA PRADESH (NEW)");


    override fun toString() = name
}



