package br.com.fiap.db1.guidemasters.repository

import android.annotation.SuppressLint
import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("SuspiciousIndentation")
fun criarRetornarGrauDeInstrucao(callback: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val listaMentoriaRef = db.collection(Constantes.COLLECTION_GRAU_DE_INSTRUCAO)
        .document(Constantes.DOCUMENT_GRAU_DE_INSTRUCAO)
    val grauDeInstrucaoPadrao = listOf(
        "Ensino Fundamental Incompleto",
        "Ensino Fundamental Completo",
        "Ensino Médio Incompleto",
        "Ensino Médio Completo",
        "Ensino Superior Incompleto",
        "Ensino Superior Completo",
        "Pós-Graduação Incompleta",
        "Pós-Graduação Completa",
        "Mestrado Incompleto",
        "Mestrado Completo",
        "Doutorado Incompleto",
        "Doutorado Completo"
    )

            listaMentoriaRef.get()
                .addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            val lista =
                                document.get(Constantes.CAMPO_GRAU_DE_INSTRUCAO_OPCAO) as? List<String>
                            if (lista != null) {
                                callback(lista)
                                return@OnCompleteListener
                            }
                        }
                    }

                    listaMentoriaRef.set(mapOf(Constantes.CAMPO_GRAU_DE_INSTRUCAO_OPCAO to grauDeInstrucaoPadrao))
                        .addOnCompleteListener { callback(grauDeInstrucaoPadrao) }
                })

}
