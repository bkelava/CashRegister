package hr.ferit.bozidarkelava.cashregister.miscellaneous

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hr.ferit.bozidarkelava.cashregister.activity.CashRegister
import hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments.ViewStock

fun checkPermission(permission: String): Boolean {
    val result: Int = ContextCompat.checkSelfPermission(CashRegister.ApplicationContext, permission)
    if (result == PackageManager.PERMISSION_GRANTED) {
        return true
    }
    else {
        return false
    }
}

fun requestPermission(activity: Activity, permission: String, code: Int) {
    ActivityCompat.requestPermissions(activity, arrayOf(permission), 0)
}

fun hasPermission(): Boolean {
    val status1 = ContextCompat.checkSelfPermission(CashRegister.ApplicationContext, Manifest.permission.CAMERA)
    val status2 = ContextCompat.checkSelfPermission(CashRegister.ApplicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val status3 = ContextCompat.checkSelfPermission(CashRegister.ApplicationContext, Manifest.permission.INTERNET)

    return status1 == PackageManager.PERMISSION_GRANTED && status2 == PackageManager.PERMISSION_GRANTED && status3 == PackageManager.PERMISSION_GRANTED
}