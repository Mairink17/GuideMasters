package br.com.fiap.db1.guidemasters.model

import br.com.fiap.db1.guidemasters.constantes.TipoPerfil

data class Perfil(
    var urlPerfil: String = "",
    var email: String = "",
    var perfil: TipoPerfil = TipoPerfil.MENTORADO,
    var cpf: String = "",
    var idade: Int = 0,
    var experiencia: String = "",
    var linkedin: String = "",
    var nome: String = "",
    var opcoesSelecionadas: List<String>?,
    var dataNascimento: String = "",
    var senha: String = "",
    var idUsuario: String = "",
    var grauInstrucao : String = "",
    var formacaoAcademica : String = ""


)