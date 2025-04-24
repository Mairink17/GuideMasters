package br.com.fiap.db1.guidemasters.notificacoes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.fiap.db1.guidemasters.MainActivity
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.repository.removerNovosMatchsLista
import br.com.fiap.db1.guidemasters.repository.verificarNovoMatch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificacoesEmBackground : Service() {

    private val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    companion object {
        const  val NOTIFICATION_ID = 12345
        const  val CHANNEL_ID = "MyBackgroundServiceChannel"
    }

    private val handler = Handler(Looper.getMainLooper())
    private val tempoIntevaloNotificacao = 60000L // 1 minuto
    private var isNotifica = false


    @SuppressLint("HandlerLeak")
    private val checkCondicaoExecucao = object : Runnable {
        override fun run() {
            autenticacao.currentUser?.uid?.let { userId ->
                verificarNovoMatch(userId) { temMatch ->
                    if (temMatch) {
                        mostraNotificacao()
                        removerNovosMatchsLista(userId)
                    }
                }
            }

            handler.postDelayed(this, tempoIntevaloNotificacao)
        }
    }

    override fun onCreate() {
        super.onCreate()
        criaCanalNotificacao()
        handler.postDelayed(checkCondicaoExecucao, tempoIntevaloNotificacao)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkCondicaoExecucao)
    }

    private fun mostraNotificacao() {

        val mainIntent = Intent(this, MainActivity::class.java)

        val clickIntent = Intent(this, DispensaNotificacoes::class.java)
        val stackBuilder = TaskStackBuilder.create(this)

        stackBuilder.addNextIntent(mainIntent)
        stackBuilder.addNextIntent(clickIntent)

        val clickPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificacao = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Novo Match")
            .setContentText("Clique para visualizar novo Match")
            .setSmallIcon(R.drawable.baseline_school_24)
            .setContentIntent(clickPendingIntent)
            .setAutoCancel(true)
            .build()


        val gerenciadorNotificacao = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                applicationContext as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_ID
            )
            return
        }

        gerenciadorNotificacao.notify(NOTIFICATION_ID, notificacao)
    }


    private fun criaCanalNotificacao() {

        val nome = "Novo Match"
        val descricaoCanal = "Clique para visualizar novo Match"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, nome, importance).apply {
            description = descricaoCanal
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

