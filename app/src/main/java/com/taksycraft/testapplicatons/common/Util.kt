package com.taksycraft.testapplicatons.common

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

object Util {
    fun isValidMobileNumber(countryCode: String="91", phNumber: String="9876543210") : Boolean{
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val isoCode = phoneNumberUtil.getRegionCodeForCountryCode(countryCode.toInt())
        var phoneNumber: Phonenumber.PhoneNumber? = null
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode)
        } catch (e: NumberParseException) {
            System.err.println(e)
        }

        val isValid = phoneNumberUtil.isValidNumber(phoneNumber)
        return if (isValid) {
            val internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            println(internationalFormat)
            true
        } else {
            println(phNumber)
            false
        }

    }
}