package hr.ferit.bozidarkelava.cashregister.miscellaneous

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp

fun getChannelId(name: String): String = "${CashRegisterApp.ApplicationContext.packageName}-$name"

const val CHANNEL = "CHANNEL"

@RequiresApi(api = Build.VERSION_CODES.O)
fun createNotificationChannel(name: String, description: String, importance: Int) : NotificationChannel{
    val channel = NotificationChannel(getChannelId(name), name, importance)
    channel.description = description
    channel.setShowBadge(true)
    return channel
}

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannels() {
    val channels = mutableListOf<NotificationChannel>()
    channels.add(
        createNotificationChannel(
            CHANNEL,
            "DESCRIPTION",
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
    )
    val notificationManager = CashRegisterApp.ApplicationContext.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannels(channels)
}

