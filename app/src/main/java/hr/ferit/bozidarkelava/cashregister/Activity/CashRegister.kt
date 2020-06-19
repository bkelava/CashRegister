package hr.ferit.bozidarkelava.cashregister.Activity

import android.app.Application
import android.content.Context

class CashRegister: Application() {

    companion object {
        lateinit var ApplicationContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = this
    }
}