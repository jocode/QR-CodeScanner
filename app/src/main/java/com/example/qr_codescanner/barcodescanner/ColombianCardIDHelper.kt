package com.example.qr_codescanner.barcodescanner

/**
 * Parses a barcode string to extract relevant ID card values.
 *
 * @param barcodeString The raw barcode string containing encoded ID card data.
 * @return A map containing parsed ID card values, such as idNumber, lastName, firstName, dayOfBirth, monthOfBirth, yearOfBirth, and gender.
 */
internal fun parseBarcodeValues(barcodeString: String): Map<String, String> {
    val idCardValues: MutableMap<String, String> = HashMap()
    val genderAndBloodType = barcodeString.substring(150, 167)
    val firstName = barcodeString.substring(104, 149)
    val lastName = barcodeString.substring(58, 103)
    val gender = genderAndBloodType[1].toString()
    val birthday = genderAndBloodType.substring(2, 10)

    // Store the scanned ID card data in a HashMap
    idCardValues["idNumber"] = eraseFirstZeroNumbers(barcodeString.substring(48, 58))
    idCardValues["lastName"] = splitWordsWithLongSpaces(lastName)
    idCardValues["firstName"] = splitWordsWithLongSpaces(firstName)
    idCardValues["dayOfBirth"] = birthday.substring(6, 8)
    idCardValues["monthOfBirth"] = birthday.substring(4, 6)
    idCardValues["yearOfBirth"] = birthday.substring(0, 4)
    idCardValues["gender"] = gender

    return idCardValues
}

/**
 * Removes leading zeros from an ID number while ensuring a single zero remains if the ID number is zero.
 *
 * @param idNumber The ID number with potential leading zeros.
 * @return The ID number with leading zeros removed, except when the ID number is zero.
 */
internal fun eraseFirstZeroNumbers(idNumber: String): String {
    return idNumber.replaceFirst("^0+(?!$)".toRegex(), "")
}

/**
 * This function takes a string with multiple consecutive spaces and replaces them
 * with a single space. It also trims any leading or trailing whitespace.
 *
 * @param stringWithLongSpaces The input string containing multiple consecutive spaces.
 * @return A cleaned string with only single spaces between words and no leading/trailing spaces.
 */
internal fun splitWordsWithLongSpaces(stringWithLongSpaces: String): String {
    return stringWithLongSpaces.replace("\\s{2,}".toRegex(), " ").trim()
}


/**
 * Maps a gender string to a corresponding code.
 *
 * @param gender The gender string ("M" for male, "F" for female).
 * @return The corresponding code ("0002" for male, "0001" for female), or an empty string for unknown genders.
 */
internal fun setGender(gender: String): String {
    return when (gender) {
        "M" -> "0002"
        "F" -> "0001"
        else -> ""
    }
}