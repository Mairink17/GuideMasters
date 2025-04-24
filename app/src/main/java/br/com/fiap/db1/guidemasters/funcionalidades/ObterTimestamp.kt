package br.com.fiap.db1.guidemasters.funcionalidades

import java.util.Calendar

fun obterTimestamp(data: String, horario: String): Long {

    val partesData = data.split("/")
    val dia = partesData[0].toInt()
    val mes = partesData[1].toInt()
    val ano = partesData[2].toInt()

    val partesHorario = horario.split(":")
    val hora = partesHorario[0].toInt()
    val minuto = partesHorario[1].toInt()

    val calendar = Calendar.getInstance()
    calendar.set(ano, mes - 1, dia, hora, minuto, 0)
    return calendar.timeInMillis
}