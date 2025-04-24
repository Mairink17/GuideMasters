package br.com.fiap.db1.guidemasters.notificacoes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import br.com.fiap.db1.guidemasters.MainActivity

class DispensaNotificacoes : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val mainIntent = Intent(context, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(mainIntent)

        val idNotificacao = intent?.getIntExtra("NOTIFICATION_ID", 0) ?: 0
        val gerenciadorNotificacao = NotificationManagerCompat.from(context!!)
        gerenciadorNotificacao.cancel(idNotificacao)
    }
}