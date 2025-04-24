package br.com.fiap.db1.guidemasters.repository

import android.util.Log
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.constantes.Constantes.COLLECTION_PERFIL
import br.com.fiap.db1.guidemasters.model.Perfil
import com.google.firebase.firestore.FirebaseFirestore


fun cadastrarNovoUsuario(
    perfil: Perfil,
    idUsuarioLogado: String?
) {
    val bancoDados by lazy{
        FirebaseFirestore.getInstance()
    }

    idUsuarioLogado?.let { id ->
        val dados = mapOf(
            Constantes.CAMPO_EMAIL to perfil.email,
            Constantes.CAMPO_NOME to perfil.nome,
            Constantes.CAMPO_EXPERIENCIA to perfil.experiencia,
            Constantes.CAMPO_CPF to perfil.cpf,
            Constantes.CAMPO_LINKEDIN to perfil.linkedin,
            Constantes.CAMPO_DATA_NASCIMENTO to perfil.dataNascimento,
            Constantes.CAMPO_PERFIL to perfil.perfil.toString(),
            Constantes.CAMPO_OPCOES_MENTORIA to perfil.opcoesSelecionadas?.toList(),
            Constantes.CAMPO_ID_USUARIO to id,
            Constantes.CAMPO_GRAU_INSTRUCAO to perfil.grauInstrucao,
            Constantes.CAMPO_FORMACAO_ACADEMICA to perfil.formacaoAcademica,
            Constantes.CAMPO_PERFIL_ATIVO to false
        )


        bancoDados
            .collection(COLLECTION_PERFIL)
            .document(id)
            .set(dados)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                val stackTrace = Thread.currentThread().stackTrace
                if (stackTrace.size >= 3) {
                    val caller = stackTrace[2]
                    val methodName = caller.methodName
                    Log.d(methodName, e.toString())
                }

            }
    }
}