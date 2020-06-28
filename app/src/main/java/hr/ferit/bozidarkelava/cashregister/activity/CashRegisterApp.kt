package hr.ferit.bozidarkelava.cashregister.activity

import android.app.Application
import android.content.Context

class CashRegisterApp: Application() {

    companion object {
        lateinit var ApplicationContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationContext = this
    }
}