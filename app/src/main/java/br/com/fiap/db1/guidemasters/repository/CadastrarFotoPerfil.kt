package br.com.fiap.db1.guidemasters.repository

import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.constantes.Constantes.COLLECTION_PERFIL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


fun cadastrarFotoUsuarioUri(fotoSelecionada: Uri?, idUsuarioLogado: String) {
    val armazenamentoStorage by lazy{
        FirebaseStorage.getInstance()
    }

    val bancoDados by lazy{
        FirebaseFirestore.getInstance()
    }
    if (fotoSelecionada != null) {
        val storageRef = armazenamentoStorage.getReference(Constantes.STORAGE_FOTOS)
        val fotoRef = storageRef.child(idUsuarioLogado)
            .child(Constantes.NOME_FOTO_PADRAO)

        fotoRef.putFile(fotoSelecionada).addOnSuccessListener { taskSnapshot ->
            fotoRef.downloadUrl.addOnSuccessListener { uri ->
                val fotoUrl = uri.toString()
                bancoDados
                    .collection(COLLECTION_PERFIL)
                    .document(idUsuarioLogado).update(Constantes.CAMPO_CAMINHO_FOTO, fotoUrl)
            }
        }
    }
}

fun cadastrarFotoUsuarioBitmap(bitmapImagemSelecionada: Bitmap?, idUsuarioLogado: String) {
    val armazenamentoStorage by lazy{
        FirebaseStorage.getInstance()
    }

    val bancoDados by lazy{
        FirebaseFirestore.getInstance()
    }
    if (bitmapImagemSelecionada != null) {
        val storageRef = armazenamentoStorage.getReference(Constantes.STORAGE_FOTOS)
        val fotoRef = storageRef.child(idUsuarioLogado)
            .child(Constantes.NOME_FOTO_PADRAO)
        val outPutStream = ByteArrayOutputStream()
        bitmapImagemSelecionada.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            outPutStream
        )

        fotoRef.putBytes(outPutStream.toByteArray()).addOnSuccessListener {
            val handler = Handler(Looper.getMainLooper())
            val delayMillis = 1000
            handler.postDelayed(object : Runnable {
                override fun run() {
                    fotoRef.downloadUrl.addOnSuccessListener { uri ->
                        val fotoUrl = uri.toString()
                        if (fotoUrl.isNotEmpty()) {
                            // URL da foto está disponível, atualiza no Firestore
                            bancoDados
                                .collection(COLLECTION_PERFIL)
                                .document(idUsuarioLogado)
                                .update(Constantes.CAMPO_CAMINHO_FOTO, fotoUrl)
                                .addOnSuccessListener {
                                    // Sucesso ao atualizar a URL da foto
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Erro ao atualizar URL da foto: $e")
                                }
                        } else {
                            // URL da foto ainda não está disponível, continua verificando
                            handler.postDelayed(this, delayMillis.toLong())
                        }
                    }
                }
            }, delayMillis.toLong())
        }.addOnFailureListener { e ->
            Log.e("Storage", "Erro ao fazer upload da foto: $e")
        }
    }

}