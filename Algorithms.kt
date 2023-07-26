package com.wholesale.jewels.fauxiq.baheekhata.utils

import com.wholesale.jewels.fauxiq.baheekhata.dto.UnpaidAmountTrans
import com.wholesale.jewels.fauxiq.baheekhata.dto.UnpaidFineTrans
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.OpeningBalance
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.people.OpeningFineWeightWithDate
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.udhaar.UdhaarAmountPayment
import com.wholesale.jewels.fauxiq.baheekhata.dto.entities.udhaar.UdhaarFinePayment

@JvmName("possibleAmountTransToPay")
fun autoPay(
    amount: Double,
    interestAmount: Double,
    unpaidAmountTrans: List<UnpaidAmountTrans>
): AutoPayAmountResult {

    var amountBalance = amount
    var interestBalance = interestAmount
    println("19Algorithms-amountBalance:$amountBalance")


    unpaidAmountTrans.forEach { payment ->

        if (amountBalance == 0.0 && interestBalance == 0.0) return@forEach

//        amountBalance = amountBalance.roundTo(2)
        amountBalance = amountBalance
        interestBalance = interestBalance.roundTo(2)

        if (amountBalance.positive() > payment.bal_amount.positive()) {
            amountBalance -= payment.bal_amount
            payment.pay = payment.bal_amount
        } else {
//            payment.pay = amountBalance.roundTo(2)
            payment.pay = amountBalance
            amountBalance = 0.0
        }

        if (interestBalance.positive() > payment.bal_interest_amount.positive()) {
            interestBalance -= payment.bal_interest_amount
            payment.interestPay = payment.bal_interest_amount
        } else {
            payment.interestPay = interestBalance.roundTo(2)
            interestBalance = 0.0
        }
    }

    return AutoPayAmountResult(unpaidAmountTrans, amountBalance, interestBalance)
}

@JvmName("possibleUdhaarAmountTransToPay")
fun autoPay(
    amount: Double,
    interestAmount: Double,
    udhaarAmountPayments: List<UdhaarAmountPayment>
): AutoPayUdhaarAmountResult {

    var amountBalance = amount
    var interestBalance = interestAmount

    udhaarAmountPayments.forEach { payment ->

        if (amountBalance == 0.0 && interestBalance == 0.0) return@forEach
        amountBalance = amountBalance.roundTo(2)
        interestBalance = interestBalance.roundTo(2)
        println("66Autopay: amountBalance:$amountBalance,interestBalance:$interestBalance")

        if (amountBalance.positive() > (payment.amount - payment.amount_paid).positive()) {
            //amountBalance -= payment.amount - payment.amount_paid
            //payment.pay = payment.amount - payment.amount_paid
            amountBalance -= (payment.amount - payment.amount_paid).unaryMinus()
            payment.pay = (payment.amount - payment.amount_paid).unaryMinus()
            println("71Autopay: amountBalance:$amountBalance,pay:${payment.pay}")
        } else {
            payment.pay = amountBalance.roundTo(2)
            amountBalance = 0.0
            println("75Autopay: amountBalance:$amountBalance,pay:${payment.pay}")
        }

        if (interestBalance.positive() > (payment.interest_amount - payment.interest_amount_paid).positive()) {
            interestBalance -= (payment.interest_amount - payment.interest_amount_paid).unaryMinus()
            payment.interestPay =
                (payment.interest_amount - payment.interest_amount_paid).unaryMinus()
            println("81Autopay: interestBalance:$interestBalance,pay:${payment.interestPay}")
        } else {
            payment.interestPay = interestBalance.roundTo(2)
            interestBalance = 0.0
            println("85Autopay: interestBalance:$interestBalance,pay:${payment.interestPay}")

        }

        println("89Autopay: amountBalance:$amountBalance,interestBalance:$interestBalance")
        println("90Autopay: pay:${payment.pay},payment.interestPay:${payment.interestPay}")
        println("91payment$payment")

    }

    return AutoPayUdhaarAmountResult(udhaarAmountPayments, amountBalance, interestBalance)
}

@JvmName("possibleFineTransToPay")
fun autoPay(
    metalType: String,
    amount: Double,
    interestAmount: Double,
    unpaidTrans: List<UnpaidFineTrans>
): AutoPayFineResult {

    var amountBalance = amount
    var interestBalance = interestAmount
    println("Algorithms1-autoPay-amountBalance: $amountBalance")

    unpaidTrans.forEach { payment ->

        if ((amountBalance == 0.0 && interestBalance == 0.0) || payment.metal_type != metalType) return@forEach

        //amountBalance = amountBalance.roundTo(3)
        //interestBalance = interestBalance.roundTo(3)

        amountBalance = amountBalance
        interestBalance = interestBalance

        println("Algorithms2-autoPay-roundTo(3)-amountBalance: $amountBalance")

        if (amountBalance.positive() > payment.bal_fine_wt.positive()) {
            amountBalance -= payment.bal_fine_wt
            payment.pay = payment.bal_fine_wt
        } else {
            //payment.pay = amountBalance.roundTo(3)
            payment.pay = amountBalance
            amountBalance = 0.0
        }

        if (interestBalance.positive() > payment.bal_interest_fine_wt.positive()) {
            interestBalance -= payment.bal_interest_fine_wt
            payment.interestPay = payment.bal_interest_fine_wt
        } else {
            //payment.interestPay = interestBalance.roundTo(3)
            payment.interestPay = interestBalance
            interestBalance = 0.0
        }
        println("Algorithms3-autoPay-roundTo(3)-payment.pay: ${payment.pay}")

    }
    return AutoPayFineResult(unpaidTrans, amountBalance, interestBalance)
}

@JvmName("possibleUdhaarFineTransToPay")
fun autoPay(
    metalType: String,
    amount: Double,
    interestAmount: Double,
    unpaidTrans: List<UdhaarFinePayment>
): AutoPayUdhaarFineResult {

    var amountBalance = amount
    var interestBalance = interestAmount

    unpaidTrans.forEach { payment ->

        if ((amountBalance == 0.0 && interestBalance == 0.0) || payment.metal_type.id != metalType) return@forEach

        amountBalance = amountBalance.roundTo(3)
        interestBalance = interestBalance.roundTo(3)

        if (amountBalance.positive() > (payment.fine_wt - payment.fine_wt_paid).positive()) {
            amountBalance -= (payment.fine_wt - payment.fine_wt_paid).unaryMinus()
            payment.pay = (payment.fine_wt - payment.fine_wt_paid).unaryMinus()
        } else {
            payment.pay = amountBalance.roundTo(3)
            amountBalance = 0.0
        }

        if (interestBalance.positive() > (payment.interest_fine_wt - payment.interest_fine_wt_paid).positive()) {
            interestBalance -= (payment.interest_fine_wt - payment.interest_fine_wt_paid).unaryMinus()
            payment.interestPay =
                (payment.interest_fine_wt - payment.interest_fine_wt_paid).unaryMinus()
        } else {
            payment.interestPay = interestBalance.roundTo(3)
            interestBalance = 0.0
        }

        /*if ((amountBalance == 0.0 && interestBalance == 0.0) || payment.metal_type.id != metalType) return@forEach

        amountBalance = amountBalance.roundTo(3)
        interestBalance = interestBalance.roundTo(3)

        if (amountBalance.positive() > (payment.fine_wt - payment.fine_wt_paid).positive()) {
            amountBalance -= payment.fine_wt - payment.fine_wt_paid
            payment.pay = payment.fine_wt - payment.fine_wt_paid
        } else {
            payment.pay = amountBalance.roundTo(3)
            amountBalance = 0.0
        }

        if (interestBalance.positive() > (payment.interest_fine_wt - payment.interest_fine_wt_paid).positive()) {
            interestBalance -= payment.interest_fine_wt - payment.interest_fine_wt_paid
            payment.interestPay = payment.interest_fine_wt - payment.interest_fine_wt_paid
        } else {
            payment.interestPay = interestBalance.roundTo(3)
            interestBalance = 0.0
        }*/
    }
    return AutoPayUdhaarFineResult(unpaidTrans, amountBalance, interestBalance)
}

@JvmName("possibleOpeningFineToPay")
fun autoPay(
    metalType: String,
    amount: Double,
    interestAmount: Double,
    unpaidOpeningFine: OpeningFineWeightWithDate?
): AutoPayOpeningFineResult {

    var amountBalance = amount
    var interestBalance = interestAmount
    println("173Algorithms-AutoPayOpeningFineResult")

    //Change done by anil - 24-09-19 10.59pm
    unpaidOpeningFine?.let { payment ->
        if (payment.metal_type == metalType) {
            if (amount.positive() > payment.bal_fine_wt.positive()) {

                amountBalance -= payment.bal_fine_wt
                payment.pay = payment.bal_fine_wt
            } else {
                //payment.pay = amount.roundTo(3)
                payment.pay = amount
                amountBalance = 0.0
            }
            if (interestAmount.positive() > payment.bal_interest_fine_wt.positive()) {

                interestBalance -= payment.bal_interest_fine_wt
                payment.interestPay = payment.bal_interest_fine_wt
            } else {
                //payment.interestPay = interestAmount.roundTo(3)
                payment.interestPay = interestAmount
                interestBalance = 0.0
            }
        }
        println("196Algorithms-AutoPayOpeningFineResult$amountBalance")

    }
    //return AutoPayOpeningFineResult(unpaidOpeningFine, amountBalance.roundTo(3), interestBalance.roundTo(3))
    return AutoPayOpeningFineResult(unpaidOpeningFine, amountBalance, interestBalance)
}

@JvmName("possibleOpeningAmountToPay")
fun autoPay(
    amount: Double,
    interestAmount: Double,
    unpaidOpeningAmount: OpeningBalance?
): AutoPayOpeningAmountResult {

    var amountBalance = amount
    var interestBalance = interestAmount

    println("221Algorithms-amountBalance:$amountBalance")

    unpaidOpeningAmount?.let { payment ->

        if (amount.positive() > payment.bal_amount.positive()) {

            amountBalance -= payment.bal_amount
            payment.pay = payment.bal_amount
        } else {
//            payment.pay = amount.roundTo(2)
            payment.pay = amount
            amountBalance = 0.0
        }
        if (interestAmount.positive() > payment.bal_interest_amount.positive()) {

            interestBalance -= payment.bal_interest_amount
            payment.interestPay = payment.bal_interest_amount
        } else {
            payment.interestPay = interestAmount.roundTo(2)
            interestBalance = 0.0
        }
    }
//    return AutoPayOpeningAmountResult(unpaidOpeningAmount, amountBalance.roundTo(2), interestBalance.roundTo(2))
    return AutoPayOpeningAmountResult(
        unpaidOpeningAmount,
        amountBalance,
        interestBalance.roundTo(2)
    )
}

data class AutoPayOpeningAmountResult(
    val unpaidTrans: OpeningBalance?,
    val amountBalance: Double,
    val interestBalance: Double
)

data class AutoPayOpeningFineResult(
    val unpaidTrans: OpeningFineWeightWithDate?,
    val amountBalance: Double,
    val interestBalance: Double
)

data class AutoPayFineResult(
    val unpaidTrans: List<UnpaidFineTrans>,
    val amountBalance: Double,
    val interestBalance: Double
)

data class AutoPayUdhaarFineResult(
    val unpaidTrans: List<UdhaarFinePayment>,
    val amountBalance: Double,
    val interestBalance: Double
)

data class AutoPayAmountResult(
    val unpaidAmountTrans: List<UnpaidAmountTrans>,
    val amountBalance: Double,
    val interestBalance: Double
)

data class AutoPayUdhaarAmountResult(
    val unpaidAmountTrans: List<UdhaarAmountPayment>,
    val amountBalance: Double,
    val interestBalance: Double
)

//return purchase and sale

@JvmName("possibleAmountTransToPayReturn")
fun autoPayReturn(
    amount: Double,
    interestAmount: Double,
    unpaidAmountTrans: List<UnpaidAmountTrans>
): AutoPayAmountResult {

    var amountBalance = amount.unaryMinus()
    var interestBalance = interestAmount.unaryMinus()
    println("329Algorithms-amountBalance:$amountBalance")


    unpaidAmountTrans.forEach { payment ->

        if (amountBalance == 0.0 && interestBalance == 0.0) return@forEach

//        amountBalance = amountBalance.roundTo(2)
        amountBalance = amountBalance
        interestBalance = interestBalance.roundTo(2)
        println("339Algorithms-amountBalance:$amountBalance")

        if (amountBalance.positive() > payment.bal_amount.positive()) {
            amountBalance -= payment.bal_amount
            payment.pay = payment.bal_amount
        } else {
//            payment.pay = amountBalance.roundTo(2)
            payment.pay = amountBalance
            amountBalance = 0.0
        }

        if (interestBalance.positive() > payment.bal_interest_amount.positive()) {
            interestBalance -= payment.bal_interest_amount
            payment.interestPay = payment.bal_interest_amount
        } else {
            payment.interestPay = interestBalance.roundTo(2)
            interestBalance = 0.0
        }
    }

    return AutoPayAmountResult(unpaidAmountTrans, amountBalance, interestBalance)
}

@JvmName("possibleFineTransToPayReturn")
fun autoPayReturnFine(
    metalType: String,
    amount: Double,
    interestAmount: Double,
    unpaidTrans: List<UnpaidFineTrans>
): AutoPayFineResult {

    var amountBalance = amount.unaryMinus()
    var interestBalance = interestAmount.unaryMinus()
    println("Algorithms1-autoPay-amountBalance: $amountBalance")

    unpaidTrans.forEach { payment ->

        if ((amountBalance == 0.0 && interestBalance == 0.0) || payment.metal_type != metalType) return@forEach

        //amountBalance = amountBalance.roundTo(3)
        //interestBalance = interestBalance.roundTo(3)

        amountBalance = amountBalance
        interestBalance = interestBalance

        println("Algorithms2-autoPay-roundTo(3)-amountBalance: $amountBalance")

        if (amountBalance.positive() > payment.bal_fine_wt.positive()) {
            amountBalance -= payment.bal_fine_wt
            payment.pay = payment.bal_fine_wt
        } else {
            //payment.pay = amountBalance.roundTo(3)
            payment.pay = amountBalance
            amountBalance = 0.0
        }

        if (interestBalance.positive() > payment.bal_interest_fine_wt.positive()) {
            interestBalance -= payment.bal_interest_fine_wt
            payment.interestPay = payment.bal_interest_fine_wt
        } else {
            //payment.interestPay = interestBalance.roundTo(3)
            payment.interestPay = interestBalance
            interestBalance = 0.0
        }
        println("Algorithms3-autoPay-roundTo(3)-payment.pay: ${payment.pay}")

    }
    return AutoPayFineResult(unpaidTrans, amountBalance, interestBalance)
}

