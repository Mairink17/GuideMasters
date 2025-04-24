package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.funcionalidades.calcularIdade
import br.com.fiap.db1.guidemasters.funcionalidades.stringParaDate
import br.com.fiap.db1.guidemasters.model.Perfil
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun RetornarListaFireBaseLikesAtivos(idUsuario: String, numeroFuncao: Int, callback: (List<Perfil>) -> Unit) {
    val formato = "dd/MM/yyyy"
    val bancoDados = FirebaseFirestore.getInstance()

    CoroutineScope(Dispatchers.IO).launch {
        val funcao: ((List<String>) -> Unit) -> Unit = when (numeroFuncao) {
            1 -> ::retornarListaLikesAtivos
            else -> ::retornarListaLikesAtivos
        }

        funcao.invoke { perfisCurtidos ->
            val listaPerfils = mutableListOf<Perfil>()

            perfisCurtidos.forEach { idUsuarioCurtido ->
                val referenciaUsuario =
                    bancoDados.collection(Constantes.COLLECTION_PERFIL).document(idUsuarioCurtido)

                referenciaUsuario.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nome = document.getString(Constantes.CAMPO_NOME) ?: ""
                        val dataNascimento = document.getString(Constantes.CAMPO_DATA_NASCIMENTO) ?: ""
                        val email = document.getString(Constantes.CAMPO_EMAIL) ?: ""
                        val experiencia = document.getString(Constantes.CAMPO_EXPERIENCIA) ?: ""
                        val opcoesMentoria = document.get(Constantes.CAMPO_OPCOES_MENTORIA) as List<*>?
                        val urlLinkedin = document.getString(Constantes.CAMPO_LINKEDIN) ?: ""
                        val urlFoto = document.getString(Constantes.CAMPO_CAMINHO_FOTO) ?: ""
                        val formacaoAcademia =
                            document.getString(Constantes.CAMPO_FORMACAO_ACADEMICA) ?: ""
                        val grauDeInstrucao = document.getString(Constantes.CAMPO_GRAU_INSTRUCAO) ?: ""

                        val idade: Int = calcularIdade(stringParaDate(dataNascimento, formato))

                        val perfil = Perfil(
                            nome = nome,
                            idade = idade,
                            email = email,
                            experiencia = experiencia,
                            opcoesSelecionadas = opcoesMentoria as List<String>?,
                            urlPerfil = urlFoto,
                            linkedin = urlLinkedin,
                            idUsuario = idUsuarioCurtido,
                            grauInstrucao = grauDeInstrucao,
                            formacaoAcademica = formacaoAcademia
                        )

                        listaPerfils.add(perfil)
                    }

                    if (listaPerfils.size == perfisCurtidos.size) {
                        callback(listaPerfils)
                    }
                }.addOnFailureListener { exception ->
                    callback(emptyList())
                }
            }
        }
    }
}
