package hr.ferit.bozidarkelava.cashregister.managers

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegister
import hr.ferit.bozidarkelava.cashregister.miscellaneous.CHANNEL
import hr.ferit.bozidarkelava.cashregister.miscellaneous.getChannelId
import java.io.File

class MyNotificationManager() {

    val NOTIFICATION_TITLE = "QR SAVED!"


    fun displayNotification(notificationText: String, path: String) {
        val file = File(path)

        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                    CashRegister.ApplicationContext,
                    "hr.ferit.android.fileprovider",
                    file!!
                ) else Uri.fromFile(file), "image/*"
            ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pendingIntent = PendingIntent.getActivity(
            CashRegister.ApplicationContext,
            0,
            intent,
            0
        )

        val notification = NotificationCompat.Builder(CashRegister.ApplicationContext,
            getChannelId(
                CHANNEL
            )
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(notificationText)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(CashRegister.ApplicationContext).notify(1001, notification)
    }
}