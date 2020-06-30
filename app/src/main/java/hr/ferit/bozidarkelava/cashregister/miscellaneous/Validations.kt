package hr.ferit.bozidarkelava.cashregister.miscellaneous

import android.text.TextUtils

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Double.Companion.round2Decimals(value: Double) {
    Math.round(value * 100)/100.0
}