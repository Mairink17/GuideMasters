package br.com.fiap.db1.guidemasters.funcionalidades

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun converterTimestampData(timestamp: Any?): String {
    if (timestamp is Timestamp) {
        val date = timestamp.toDate()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return dateFormat.format(date)
    } else {
        return ""
    }
}
