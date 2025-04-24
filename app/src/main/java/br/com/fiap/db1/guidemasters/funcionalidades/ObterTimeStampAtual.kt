package br.com.fiap.db1.guidemasters.funcionalidades

import java.util.Calendar

fun obterTimestampDiaAtual(): Long {

    val calendario = Calendar.getInstance()
    calendario.set(Calendar.HOUR_OF_DAY, 0)
    calendario.set(Calendar.MINUTE, 0)
    calendario.set(Calendar.SECOND, 0)
    calendario.set(Calendar.MILLISECOND, 0)

    return calendario.timeInMillis
}