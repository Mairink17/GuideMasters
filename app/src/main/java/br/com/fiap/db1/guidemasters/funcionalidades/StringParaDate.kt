package br.com.fiap.db1.guidemasters.funcionalidades

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun stringParaDate(dataString: String, formato: String): Date {

    val dateFormat = SimpleDateFormat(formato, Locale.getDefault())
    return dateFormat.parse(dataString)!!
}