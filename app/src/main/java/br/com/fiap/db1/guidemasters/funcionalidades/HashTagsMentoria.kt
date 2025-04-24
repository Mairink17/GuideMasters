package br.com.fiap.db1.guidemasters.funcionalidades

fun hashTagsMentoria(lista: List<String>): String {
    return lista.joinToString(separator = " ") { "#$it" }
}
