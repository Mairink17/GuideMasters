package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



fun adicionarPerfilInteracao(perfilId: String, curtido: Boolean) {

    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    val userId = autenticacao.currentUser?.uid
    val banco = FirebaseFirestore.getInstance()

    userId?.let { uid ->
        val perfilRef = banco.collection(Constantes.COLLECTION_ENCONTRADOS).document(uid)

        perfilRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                val perfisCurtidos = doc.get(Constantes.CAMPO_ENCONTRADO_CURTIDA) as? MutableList<String>
                if (perfisCurtidos != null) {
                    if (!perfisCurtidos.contains(perfilId)) {
                        perfisCurtidos.add(perfilId)
                        perfilRef.update(Constantes.CAMPO_ENCONTRADO_CURTIDA, perfisCurtidos)
                    }
                }
            } else {
                val dados = hashMapOf(
                    Constantes.CAMPO_ENCONTRADO_CURTIDA to mutableListOf(perfilId)
                )
                perfilRef.set(dados)
            }
        }
    }
}

fun getPerfisCurtidos(callback: (List<String>) -> Unit) {

    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    val userId = autenticacao.currentUser?.uid
    val banco = FirebaseFirestore.getInstance()
    val perfisCurtidos = mutableListOf<String>()

    userId?.let { uid ->
        val perfilRef = banco.collection(Constantes.COLLECTION_ENCONTRADOS).document(uid)

        perfilRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                val perfisCurtidosList = doc.get(Constantes.CAMPO_ENCONTRADO_CURTIDA) as? List<String>
                perfisCurtidosList?.let {
                    perfisCurtidos.addAll(it)
                }
            }
            callback(perfisCurtidos)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}
