package br.com.fiap.db1.guidemasters.notificacoes

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import br.com.fiap.db1.guidemasters.MainActivity
import br.com.fiap.db1.guidemasters.R

fun criaCanalNotificacao(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val channelId = "matchs"
        val channelName = "Notificação Matchs"
        val channelDescription = "Notificação Matchs"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
fun mostraNotificacao(context: Context, title: String, message: String, navController: NavController) {
    val channelId = "matchs"
    criaCanalNotificacao(context)

    val intent = Intent(context, MainActivity::class.java).apply {
        putExtra("destination", "telaContatos")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_school_24)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        notify(1, notificationBuilder.build())
    }
}
