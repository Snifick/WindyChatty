package com.flowtrust.windychatty.ui.pages.auth.contry_picker

import com.google.i18n.phonenumbers.AsYouTypeFormatter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

fun getPhoneMaskUsingLibphonenumber(countryCode: String): String {
    val phoneUtil = PhoneNumberUtil.getInstance()

    try {
        // Получаем пример номера для указанной страны
        val exampleNumber: Phonenumber.PhoneNumber? = phoneUtil.getExampleNumberForType(countryCode, PhoneNumberUtil.PhoneNumberType.MOBILE)

        // Если номер доступен, форматируем его в виде строки
        if (exampleNumber != null) {
            val formattedNumber = phoneUtil.format(exampleNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            return formattedNumber.replace("\\d".toRegex(), "0") // Заменяем цифры на X для маски
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}