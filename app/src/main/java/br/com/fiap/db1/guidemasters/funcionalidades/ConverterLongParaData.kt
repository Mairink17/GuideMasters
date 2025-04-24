package br.com.fiap.db1.guidemasters.funcionalidades

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun converterLongParaData(timestamp: Long) : String{

    val data = Date(timestamp)
    val mascara = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return mascara.format(data)

}