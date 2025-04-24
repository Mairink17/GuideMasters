package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.funcionalidades.calcularIdade
import br.com.fiap.db1.guidemasters.funcionalidades.stringParaDate
import br.com.fiap.db1.guidemasters.model.Perfil
import com.google.firebase.firestore.FirebaseFirestore

fun RetornarPerfilUnico(userId : String,callback: (Perfil?) -> Unit){
    val formato = "dd/MM/yyyy"
    val db = FirebaseFirestore.getInstance()

    userId.let { uid ->
        val usuarioRef = db.collection(Constantes.COLLECTION_PERFIL).document(uid)

        usuarioRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val nome = documentSnapshot.getString(Constantes.CAMPO_NOME) ?: ""
                val dataNascimento = documentSnapshot.getString(Constantes.CAMPO_DATA_NASCIMENTO) ?: ""
                val email = documentSnapshot.getString(Constantes.CAMPO_EMAIL) ?: ""
                val experiencia = documentSnapshot.getString(Constantes.CAMPO_EXPERIENCIA) ?: ""
                val opcoesMentoria = documentSnapshot.get(Constantes.CAMPO_OPCOES_MENTORIA) as List<String>?
                val urlLinkedin = documentSnapshot.getString(Constantes.CAMPO_LINKEDIN) ?: ""
                val urlFoto = documentSnapshot.getString(Constantes.CAMPO_CAMINHO_FOTO) ?: ""
                val cpf = documentSnapshot.getString(Constantes.CAMPO_CPF) ?: ""
                val idade: Int = calcularIdade(stringParaDate(dataNascimento, formato))
                val formacaoAcademia = documentSnapshot.getString(Constantes.CAMPO_FORMACAO_ACADEMICA) ?: ""
                val grauInstrucao = documentSnapshot.getString(Constantes.CAMPO_GRAU_INSTRUCAO) ?: ""

                val perfil = Perfil(
                    nome = nome,
                    idade = idade,
                    email = email,
                    experiencia = experiencia,
                    opcoesSelecionadas = opcoesMentoria,
                    urlPerfil = urlFoto,
                    linkedin = urlLinkedin,
                    idUsuario = uid,
                    cpf = cpf,
                    dataNascimento = dataNascimento,
                    grauInstrucao = grauInstrucao,
                    formacaoAcademica = formacaoAcademia
                )
                callback(perfil)
            } else {
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }
}