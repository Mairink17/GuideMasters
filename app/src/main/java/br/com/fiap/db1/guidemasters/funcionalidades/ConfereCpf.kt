package br.com.fiap.db1.guidemasters.funcionalidades

fun validarCPF(cpf: String): Boolean {

    val cpfLimpo = cpf.replace("[^0-9]".toRegex(), "")

    if (cpfLimpo.length != 11)
        return false

    if (cpfLimpo == "00000000000" ||
        cpfLimpo == "11111111111" ||
        cpfLimpo == "22222222222" ||
        cpfLimpo == "33333333333" ||
        cpfLimpo == "44444444444" ||
        cpfLimpo == "55555555555" ||
        cpfLimpo == "66666666666" ||
        cpfLimpo == "77777777777" ||
        cpfLimpo == "88888888888" ||
        cpfLimpo == "99999999999")
        return false

    var soma = 0
    for (i in 0 until 9) {
        soma += (10 - i) * cpfLimpo[i].toString().toInt()
    }
    var resto = soma % 11
    var digitoVerificador1 = if (resto < 2) 0 else 11 - resto

    soma = 0
    for (i in 0 until 10) {
        soma += (11 - i) * cpfLimpo[i].toString().toInt()
    }
    resto = soma % 11
    var digitoVerificador2 = if (resto < 2) 0 else 11 - resto

    return cpfLimpo[9].toString().toInt() == digitoVerificador1 && cpfLimpo[10].toString().toInt() == digitoVerificador2
}