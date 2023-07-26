package com.wholesale.jewels.fauxiq.baheekhata.utils

object NumberToWordsRupees {
    fun getRupeesInWords( number:String):String {

        var number_str = number
        var twodigitword = ""
        var word = ""
        var HTLC = arrayOf<String>("", "Hundred", "Thousand", "Lakh", "Crore") //H-hundread , T-Thousand, ..
        var split = intArrayOf(0, 2, 3, 5, 7, 9)
        var temp = arrayOfNulls<String>(split.size)
        var addzero = true
        var len1 = number_str.length

        for (l in 1 until split.size)
            if (number_str.length == split[l]) addzero = false
        if (addzero == true) {
            number_str = "0" + number_str
        }
        var len = number_str.length
        var j = 0
        //spliting & putting numbers in temp array.
        while (split[j] < len)
        {
            var beg = len - split[j + 1]
            var end = beg + split[j + 1] - split[j]
            temp[j] = number_str.substring(beg, end)
            j = j + 1
        }
        for (k in 0 until j)
        {
            twodigitword = temp[k]?.let { ConvertOnesTwos(it) }.toString()
            if (k >= 1)
            {
                if (twodigitword.trim { it <= ' ' }.isNotEmpty()) word = twodigitword + " " + HTLC[k] + " " + word
            }
            else
                word = twodigitword
        }
        return (word)
    }

    fun ConvertOnesTwos(t:String):String {
        val ones = arrayOf<String>("", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen")
        val tens = arrayOf<String>("", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety")
        var word = ""
        val num = Integer.parseInt(t)
        if (num % 10 == 0)
            word = tens[num / 10] + " " + word
        else if (num < 20)
            word = ones[num] + " " + word
        else
        {
            word = tens[(num - (num % 10)) / 10] + word
            word = word + " " + ones[num % 10]
        }
        return word
    }
}