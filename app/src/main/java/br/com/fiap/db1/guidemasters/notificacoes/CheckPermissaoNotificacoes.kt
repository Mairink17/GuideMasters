package br.com.fiap.db1.guidemasters.notificacoes

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat

fun checkNotificationPermission(activity: Activity) {
    if (!NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Permissão de Notificação")
        builder.setMessage("Você precisa as notificações para receber alertas.")
        builder.setPositiveButton("OK") { dialog, which ->
            val intent = Intent()
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            activity.startActivity(intent)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}
