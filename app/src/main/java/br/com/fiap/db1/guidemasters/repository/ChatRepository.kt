package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun enviaUsuarioConectado(userId: String, contactId: String, message: String) {
        val conversationRef = firestore.collection(Constantes.COLLECTION_PERFIL).document(userId)
            .collection(Constantes.COLLECTION_USUARIO_CHAT).document(contactId)

        conversationRef.get().addOnSuccessListener { documentSnapshot ->
            val messages = documentSnapshot.get(Constantes.CAMPO_CHAT_MENSAGENS) as? MutableList<HashMap<String, String>> ?: mutableListOf()
            messages.add(hashMapOf(
                Constantes.CAMPO_CHAT_USUARIO_ENVIO_MENSAGEM to userId,
                Constantes.CAMPO_CHAT_TEXTO to message
            ))
            conversationRef.set(hashMapOf(Constantes.CAMPO_CHAT_ID_CONTATO to contactId, Constantes.CAMPO_CHAT_MENSAGENS to messages))
        }
    }

    fun enviaDestinatario(userId: String, contactId: String, message: String) {
        val conversationRef = firestore.collection(Constantes.COLLECTION_PERFIL).document(contactId)
            .collection(Constantes.COLLECTION_USUARIO_CHAT).document(userId)

        conversationRef.get().addOnSuccessListener { documentSnapshot ->
            val messages = documentSnapshot.get(Constantes.CAMPO_CHAT_MENSAGENS) as? MutableList<HashMap<String, String>> ?: mutableListOf()
            messages.add(hashMapOf(
                Constantes.CAMPO_CHAT_USUARIO_ENVIO_MENSAGEM to userId,
                Constantes.CAMPO_CHAT_TEXTO to message
            ))
            conversationRef.set(hashMapOf(Constantes.CAMPO_CHAT_ID_CONTATO to userId, Constantes.CAMPO_CHAT_MENSAGENS to messages))
        }
    }

    fun getConversation(userId: String, contactId: String, callback: (List<HashMap<String, String>>) -> Unit) {
        val conversationRef = firestore.collection(Constantes.COLLECTION_PERFIL).document(userId)
            .collection(Constantes.COLLECTION_USUARIO_CHAT).document(contactId)

        conversationRef.addSnapshotListener { documentSnapshot, _ ->
            val messages = documentSnapshot?.get(Constantes.CAMPO_CHAT_MENSAGENS) as? List<HashMap<String, String>> ?: emptyList()
            callback(messages)
        }
    }

}
