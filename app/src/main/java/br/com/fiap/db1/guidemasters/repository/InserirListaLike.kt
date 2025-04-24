package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

fun darLike(idPerfilAlvo: String, idUsuario: String) {

    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    val userId = autenticacao.currentUser?.uid
    val banco = FirebaseFirestore.getInstance()

    userId?.let { uid ->
        val perfilRef = banco.collection(Constantes.COLLECTION_LIKE).document(idPerfilAlvo)

        perfilRef.get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                val perfisCurtidos =
                    doc.get(Constantes.CAMPO_LIKE_USUARIO_CURTIDA) as? MutableList<String>
                if (perfisCurtidos != null) {
                    if (!perfisCurtidos.contains(idUsuario)) {
                        perfilRef.update(Constantes.CAMPO_LIKE_USUARIO_CURTIDA, FieldValue.arrayUnion(idUsuario))
                    }
                }
            } else {
                val dados = hashMapOf(
                    Constantes.CAMPO_LIKE_USUARIO_CURTIDA to mutableListOf(idUsuario)
                )
                perfilRef.set(dados)
            }
        }

    }
}

fun verificarLike(idPerfilAlvo: String, idUsuario: String, callback: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val likesRef = db.collection(Constantes.COLLECTION_LIKE).document(idUsuario)

    likesRef.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val document = task.result
            if (document != null && document.exists()) {
                val listaLikes = document.get(Constantes.CAMPO_LIKE_USUARIO_CURTIDA) as? List<String>
                if (listaLikes != null && idPerfilAlvo in listaLikes) {
                    callback(true)
                } else {
                    callback(false)
                }
            } else {
                callback(false)
            }
        } else {
            callback(false)
        }
    }
}
