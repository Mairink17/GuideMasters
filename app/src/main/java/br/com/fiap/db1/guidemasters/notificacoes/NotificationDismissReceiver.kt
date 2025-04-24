package br.com.fiap.db1.guidemasters.notificacoes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationDismissedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "DISMISS_NOTIFICATION") {
            val gerenciadorNotificacao = NotificationManagerCompat.from(context!!)
            gerenciadorNotificacao.cancel(NotificacoesEmBackground.NOTIFICATION_ID)
        }
    }
}
