package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun retornarListaLikesAtivos( callback: (List<String>) -> Unit) {

    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    val banco = FirebaseFirestore.getInstance()
    val userId = autenticacao.currentUser?.uid

    val db = FirebaseFirestore.getInstance()
    val likesRef = userId?.let { db.collection(Constantes.COLLECTION_PERFIL).document(it) }

    if (likesRef != null) {
        likesRef.get().addOnSuccessListener { document ->
            val listaLikes = document.get(Constantes.CAMPO_MATCHES_ATIVOS) as? List<String>
            if (listaLikes != null) {
                callback(listaLikes)
            } else {
                callback(emptyList())
            }
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}
