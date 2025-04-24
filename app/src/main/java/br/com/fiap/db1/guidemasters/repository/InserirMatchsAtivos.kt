package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.firestore.FirebaseFirestore

fun inserirMatchsAtivos(idUsuarioLogado: String, idUsuarioMatch: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection(Constantes.COLLECTION_PERFIL).document(idUsuarioLogado)

    usuarioRef.get().addOnSuccessListener { documentSnapshot ->
        val matchMap = mutableMapOf<String, Any>()

        val listaMatchsAtivos = documentSnapshot.get(Constantes.CAMPO_MATCHES_ATIVOS) as? MutableList<String> ?: mutableListOf()
        if (!listaMatchsAtivos.contains(idUsuarioMatch)) {
            listaMatchsAtivos.add(idUsuarioMatch)
            matchMap[Constantes.CAMPO_MATCHES_ATIVOS] = listaMatchsAtivos
        }

        val listaNovosMatchs = documentSnapshot.get(Constantes.CAMPO_NOVOS_MATCHS) as? MutableList<String> ?: mutableListOf()
        if (!listaNovosMatchs.contains(idUsuarioMatch)) {
            listaNovosMatchs.add(idUsuarioMatch)
            matchMap[Constantes.CAMPO_NOVOS_MATCHS] = listaNovosMatchs
        }
        usuarioRef.update(matchMap)
    }
}

fun removerNovoMatch(idUsuarioLogado: String, idUsuarioMatch: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection(Constantes.COLLECTION_PERFIL).document(idUsuarioLogado)

    usuarioRef.get().addOnSuccessListener { documentSnapshot ->
        val listaNovosMatchs = documentSnapshot.get(Constantes.CAMPO_NOVOS_MATCHS) as? MutableList<String>
        if (listaNovosMatchs != null && listaNovosMatchs.contains(idUsuarioMatch)) {
            listaNovosMatchs.remove(idUsuarioMatch)
            val matchMap = mutableMapOf<String, Any>()
            matchMap[Constantes.CAMPO_NOVOS_MATCHS] = listaNovosMatchs
            usuarioRef.update(matchMap)
        }
    }
}

fun removerNovosMatchsLista(idUsuarioLogado: String) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection(Constantes.COLLECTION_PERFIL).document(idUsuarioLogado)

    val matchMap = mutableMapOf<String, Any>()
    matchMap[Constantes.CAMPO_NOVOS_MATCHS] = mutableListOf<String>()
    usuarioRef.update(matchMap)
}


fun verificarNovoMatch(idUsuarioLogado: String, callback: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val usuarioRef = db.collection(Constantes.COLLECTION_PERFIL).document(idUsuarioLogado)

    usuarioRef.get().addOnSuccessListener { documentSnapshot ->
        val listaMatch = documentSnapshot.get(Constantes.CAMPO_NOVOS_MATCHS) as? List<*>
        val hasNewMatch = !listaMatch.isNullOrEmpty()
        callback(hasNewMatch)
    }.addOnFailureListener {
        callback(false)
    }
}
