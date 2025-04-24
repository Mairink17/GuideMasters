package br.com.fiap.db1.guidemasters.funcionalidades

import java.util.Calendar
import java.util.Date

fun calcularIdade(dataNascimento: Date): Int {
    val hoje = Calendar.getInstance()
    val nascimento = Calendar.getInstance()
    nascimento.time = dataNascimento
    var idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR)
    if (hoje.get(Calendar.DAY_OF_YEAR) < nascimento.get(Calendar.DAY_OF_YEAR)) {
        idade--
    }
    return idade
}