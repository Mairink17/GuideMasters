package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

fun darFeedBack(
    idDestinoFeedback: String,
    nota: Int,
    descricao: String,
    idOrigemFeedback: String,
    nomeFeedback: String
) {

    val db = FirebaseFirestore.getInstance()

    val feedbacksRef = db.collection(Constantes.COLLECTION_PERFIL).document(idDestinoFeedback)
        .collection(Constantes.COLLECTION_USUARIO_FEEDBACK)

    feedbacksRef.document(idOrigemFeedback).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {

                if (document.getString(Constantes.CAMPO_FEEDBACK_USUARIO) == idOrigemFeedback) {
                    atualizarFeedback(
                        idDestinoFeedback,
                        nota,
                        descricao,
                        idOrigemFeedback,
                        feedbacksRef.document(idOrigemFeedback),
                        nomeFeedback = nomeFeedback
                    )
                } else {
                    println("O idOrigem do feedback existente não corresponde ao idOrigemFeedback fornecido")
                }
            } else {
                adicionarNovoFeedback(
                    idDestinoFeedback,
                    nota,
                    descricao,
                    idOrigemFeedback,
                    feedbacksRef,
                    nomeFeedback = nomeFeedback
                )
            }
        }
        .addOnFailureListener { e ->
            println("Erro ao verificar feedback existente: $e")
        }
}

private fun adicionarNovoFeedback(
    idDestinoFeedback: String,
    nota: Int,
    descricao: String,
    idOrigemFeedback: String,
    feedbacksRef: CollectionReference,
    nomeFeedback: String
) {
    val novoFeedback = hashMapOf(
        Constantes.CAMPO_FEEDBACK_DATA to Calendar.getInstance().time,
        Constantes.CAMPO_FEEDBACK_NOTA to nota,
        Constantes.CAMPO_FEEDBACK_DESCRICAO to descricao,
        Constantes.CAMPO_FEEDBACK_USUARIO to nomeFeedback
    )
    feedbacksRef.document(idOrigemFeedback).set(novoFeedback)
        .addOnSuccessListener { documentReference ->
            println("Feedback adicionado com ID: $idOrigemFeedback")
        }
        .addOnFailureListener { e ->
            println("Erro ao adicionar feedback: $e")
        }
}

private fun atualizarFeedback(
    idUsuario: String,
    nota: Int,
    descricao: String,
    idOrigemFeedback: String,
    feedbackDocRef: DocumentReference,
    nomeFeedback: String
) {
    val feedbackAtualizado = hashMapOf(
        Constantes.CAMPO_FEEDBACK_DATA to Calendar.getInstance().time,
        Constantes.CAMPO_FEEDBACK_NOTA to nota,
        Constantes.CAMPO_FEEDBACK_DESCRICAO to descricao,
        Constantes.CAMPO_FEEDBACK_USUARIO to nomeFeedback
    )
    feedbackDocRef.set(feedbackAtualizado)
        .addOnSuccessListener {
            println("Feedback atualizado com sucesso")
        }
        .addOnFailureListener { e ->
            println("Erro ao atualizar feedback: $e")
        }
}

fun calcularMediaFeedback(idUsuario: String, callback: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    val feedbacksRef = db.collection(Constantes.COLLECTION_PERFIL)
        .document(idUsuario)
        .collection(Constantes.COLLECTION_USUARIO_FEEDBACK)

    feedbacksRef.get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                var totalNotas = 0
                var totalFeedbacks = 0

                for (document in documents) {
                    val nota = document.getLong(Constantes.CAMPO_FEEDBACK_NOTA)
                    if (nota != null) {
                        totalNotas += nota.toInt()
                        totalFeedbacks++
                    }
                }

                val media = if (totalFeedbacks > 0) totalNotas.toDouble() / totalFeedbacks else 0.0
                val feedbacksText = if (totalFeedbacks == 0) "N/A" else "$media (${totalFeedbacks} aval.)"
                callback(feedbacksText)
            } else {
                val feedbacksText = "N/A"
                callback(feedbacksText)
            }
        }
        .addOnFailureListener { e ->
            val feedbacksText = "N/A"
            callback(feedbacksText)
        }
}

