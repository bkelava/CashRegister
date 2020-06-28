package hr.ferit.bozidarkelava.cashregister.managers

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp
import hr.ferit.bozidarkelava.cashregister.miscellaneous.CHANNEL
import hr.ferit.bozidarkelava.cashregister.miscellaneous.getChannelId
import java.io.File

class MyNotificationManager() {

    fun displayNotification(notificationTitle: String, notificationText: String, path: String) {
        val file = File(path)

        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
                    CashRegisterApp.ApplicationContext,
                    "hr.ferit.android.fileprovider",
                    file!!
                ) else Uri.fromFile(file), "image/*"
            ).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pendingIntent = PendingIntent.getActivity(
            CashRegisterApp.ApplicationContext,
            0,
            intent,
            0
        )

        val notification = NotificationCompat.Builder(CashRegisterApp.ApplicationContext,
            getChannelId(
                CHANNEL
            )
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(CashRegisterApp.ApplicationContext).notify(1001, notification)
    }
}