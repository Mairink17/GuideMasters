package br.com.fiap.db1.guidemasters.funcionalidades

fun validaUrlLinkedIn(url: String): Boolean {
    val pattern = Regex("^https?://(?:www\\.)?linkedin\\.com/in/[a-zA-Z0-9-]+/?$")
    return pattern.matches(url)
}