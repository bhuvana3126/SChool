package com.wholesale.jewels.fauxiq.baheekhata.utils

import com.wholesale.jewels.fauxiq.baheekhata.enums.PaymentFrequency
import kotlin.math.roundToInt
import kotlin.math.roundToLong

object Formulas {

    fun getGrossWeight(strKg: String?, strGm: String?, strMg: String?): Double {
        /*String strKg =etWeightKg.getText().toString();
        String strGm =etWeightGm.getText().toString();
        String strMg =etWeightMg.getText().toString();*/

        val weight: Double
        if (strKg == null && strGm == null && strMg == null) {
            return 0.0
        }

        var kg = 0.0
        var gm = 0.0
        var mg = 0.0

        try {
            kg = java.lang.Double.parseDouble(strKg!!)
        } catch (e: NumberFormatException) {
            //e.printStackTrace();
        }

        try {
            gm = java.lang.Double.parseDouble(strGm!!)
        } catch (e: NumberFormatException) {
            //e.printStackTrace();
        }

        try {
            mg = java.lang.Double.parseDouble(strMg!!)
        } catch (e: NumberFormatException) {
            //e.printStackTrace();
        }

        if (kg == 0.0 && gm == 0.0 && mg == 0.0) {
            return 0.0
        }

        weight = kg * 1000 + gm + mg / 1000
        return weight.roundTo(3)
    }

    fun getGrossWeight(qty: Int, unitWt: Double): Double = (qty * unitWt).roundTo(3)
    fun getUnitWeight(qty: Int, grossWt: Double): Double = (grossWt / qty).roundTo(3)
    fun getQuantity(unitWt: Double, grossWt: Double): Int = (grossWt / unitWt).toInt()

    fun calcNetWeight(grossWeight: Double, lessWeight: Double, stoneWeight: Double, waxWeight: Double): Double {
        return (grossWeight - (lessWeight + stoneWeight + waxWeight)).roundTo(3)
    }

    fun calcTotalWeight(netWt: Double, wastageWeight: String): Double {

        /*
        netWeight -> calcNetWeight()
        wastageWeight ->ed_new_stock_wastage_wt.text.toString()*/


        if (wastageWeight.isEmpty()) {
            return netWt
        }

        val wastageWt = when (wastageWeight.isNotEmpty()) {
            true -> wastageWeight.toDouble()
            false -> 0.0
        }

        return (netWt + wastageWt).roundTo(3)
    }

    fun calcFineWeightWithTouch(netWt: Double, touch: Double): Double = (netWt * touch / 100).roundTo(3)

    fun calcFineWeightWithMelt(totalWt: Double, melt: Double): Double = (totalWt * melt / 100).roundTo(3)
    @JvmStatic
    fun calcGrossWeightFromFineWtAndMelt(fineWt: Double, melt: Double): Double = (fineWt * 100 / melt).roundTo(3)

    fun totalCharge(
        other_charge: Double,
        making_charge: Double,
        hallmark_charge: Double,
        stone_charge: Double,
        wax_charge: Double
    ): Double {

        return (other_charge + making_charge + hallmark_charge + stone_charge + wax_charge).roundTo(2)
    }

    //    This method will gives kg from gm(double) i.e 123456.789 -> 123
    @JvmStatic
    fun getKgFromGm(wt: Double): Int {
        return (wt / 1000).toInt()
    }

    //    This method will gives gm from gm(double) i.e 123456.789 -> 456
    @JvmStatic
    fun getGmFromGm(wt: Double): Int {
        return (wt % 1000).toInt()
    }

    //    This method will gives mg from gm(double) i.e 123456.789 -> 789
    @JvmStatic
    fun getMgFromGm(wt: Double): Int {
        return ((wt * 1000) % (1000)).toInt()
    }

    fun calTotalAmountAfterTax(taxableAmount: Double, gstPercentage: Double): Double =
        (taxableAmount + taxableAmount * gstPercentage / 100).roundTo(2)

    fun calcPercentage(amount: Double, percentage: Double): Double =
        (amount * percentage / 100).roundTo(2)

    fun calcItemAmountByTouch(netWt: Double, rate: Double, touch: Double): Double =
        (netWt * rate * touch / 100).roundTo(2)

    fun calcItemAmountWastage(totalWt: Double, rate: Double, melt: Double): Double =
        (totalWt * rate * melt / 100).roundTo(2)

    fun calcItemAmountDirectRate(totalWt: Double, rate: Double): Double =
        (totalWt * rate).roundTo(2)

    fun calcItemNetAmount(amount: Double, charges: Double): Double =
        (amount + charges).roundTo(2)

    fun calcTaxableAmount(netAmount: kotlin.Double, discount: kotlin.Double): kotlin.Double =
        (netAmount - discount).roundTo(2)

    @JvmStatic
    fun calcConvertToFine(amount: Double, rate: Double): Double =
//        (if (rate != 0.0) amount / rate else 0.0).roundTo(3)
        (if (rate != 0.0) amount / rate else 0.0)

    @JvmStatic
    fun calcConvertToFineTest(amount: Double, rate: Double): Long =
        (if (rate != 0.0) amount / rate else 0.0).roundToTest()

    //By anil 25-09-19
    @JvmStatic
    fun calcConvertToRate(fine: Double, rate: Double): Double = (fine * rate)

    @JvmStatic
    fun calcConvertToRateTest(fine: Double, rate: Double): Long = (fine * rate).roundToTest()

    //fun calcConvertToRate(fine: Double, rate: Double): Double = (fine * rate).roundTo(2)

    fun calcInterestForUdhaarAmount(
        amount: Double,
        interestRate: Double,
        payment_frequency: PaymentFrequency,
        lastUpdateDate: Long
    ): Double {

        var numberOfDays =
            ((DateUtils.currentDate.endDateInMillis - lastUpdateDate.endDateInMillis).toDouble() / 86400000.0).toInt()
        numberOfDays += if ((DateUtils.currentDate.endDateInMillis - lastUpdateDate.endDateInMillis).toDouble() % 86400000.0 > 0) 1 else 0

        return amount * interestRate *
                when (payment_frequency) {
                    PaymentFrequency.MONTHLY -> 12
                    else -> 1
                } / 100 / 365 * numberOfDays
    }


    fun getGrossWeight(wt: String?): Double {

        val weight: Double
        if (wt == null) {
            return 0.0
        }

        var gm = 0.0


        try {
            gm = java.lang.Double.parseDouble(wt)
        } catch (e: NumberFormatException) {
            //e.printStackTrace();
        }

        if ( gm == 0.0 ) {
            return 0.0
        }

        weight = gm
        return weight.roundTo(3)
    }

    // Auto GST 29JUN2020
    fun calGstAmountAutoGst(gstIncludeAmount: Double, gstPercentage: Double): Double =
        (((gstIncludeAmount * gstPercentage) / (100 + gstPercentage)).roundToInt()).toDouble()

    fun calGstHalfAmountAutoGst(amount: Double, percentage: Double): Double =
        (((amount * percentage) / (100 + percentage)).roundToInt()).toDouble() / 2

    fun calTaxableAmountAutoGst(gstIncludeAmount: Double, gstPercentage: Double): Double =
        (gstIncludeAmount - (((gstIncludeAmount * gstPercentage) / (100 + gstPercentage)).roundToInt()).toDouble()).roundTo(2)

    fun calGrWeightAutoGst(rate: Double, taxableAmount: Double): Double =
        (taxableAmount / rate).roundTo(3)

    fun calFinalRateAutoGst(weight : Double, taxableAmount: Double): Double =
        (taxableAmount / weight).roundTo(2)

    fun calIsTaxableGetBack(weight : Double, rate: Double): Double =
        ((rate * weight).roundToInt()).toDouble().roundTo(2)

    // Auto GST 29JUN2020 Without Round off
    fun calNROGstAmountAutoGst(gstIncludeAmount: Double, gstPercentage: Double): Double =
        ((gstIncludeAmount * gstPercentage) / (100 + gstPercentage))

    fun calNROGstHalfAmountAutoGst(amount: Double, percentage: Double): Double =
        ((amount * percentage) / (100 + percentage)) / 2

    fun calNROTaxableAmountAutoGst(gstIncludeAmount: Double, gstPercentage: Double): Double =
        (gstIncludeAmount - ((gstIncludeAmount * gstPercentage) / (100 + gstPercentage))).roundTo(2)

    fun calNROGrWeightAutoGst(rate: Double, taxableAmount: Double): Double =
        (taxableAmount / rate).roundTo(3)

    fun calNROFinalRateAutoGst(weight : Double, taxableAmount: Double): Double =
        (taxableAmount / weight).roundTo(2)

    fun calNROIsTaxableGetBack(weight : Double, rate: Double): Double =
        (rate * weight).roundTo(2)

    /*fun calGstAmountAutoGst(gstIncludeAmount: Double, gstPercentage: Double): Double =
        ((gstIncludeAmount * gstPercentage) / (100 + gstPercentage)).roundTo(2)

    fun calGstHalfAmountAutoGst(amount: Double, percentage: Double): Double =
        (((amount * percentage) / (100 + percentage)) / 2 ).roundTo(2)

    fun calTaxableAmountAutoGst(gstIncludeAmount: Double, gstPercentage: Double): Double =
        (gstIncludeAmount - ((gstIncludeAmount * gstPercentage) / (100 + gstPercentage))).roundTo(2)

    fun calGrWeightAutoGst(rate: Double, taxableAmount: Double): Double =
        (taxableAmount / rate).roundTo(3)

    fun calFinalRateAutoGst(weight : Double, taxableAmount: Double): Double =
        (taxableAmount / weight).roundTo(2)

    fun calIsTaxableGetBack(weight : Double, rate: Double): Double =
        ((rate * weight).roundToInt()).toDouble().roundTo(2)*/
}