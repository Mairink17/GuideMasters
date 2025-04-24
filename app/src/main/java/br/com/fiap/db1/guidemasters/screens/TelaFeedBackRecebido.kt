package br.com.fiap.db1.guidemasters.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun TelaFeedbacksRecebidos(navController: NavController, idUsuario: String) {

    val feedbacksPorUsuario = remember { mutableStateMapOf<String, MutableList<Map<String, Any>>>() }
    val dadosCarregados = remember { mutableStateOf(false) }
    val mensagemErroFeedback = stringResource(id = R.string.print_erro_feedback)
    val feedbackUsuario = stringResource(id = R.string.feedback_usuario)
    val feedbackData = stringResource(id = R.string.feedback_data)
    val feedbackNota = stringResource(id = R.string.feedback_nota)
    val feedbackDescricao = stringResource(id = R.string.feedback_descricao)




    fun carregarFeedbacks() {
        val db = FirebaseFirestore.getInstance()
        val feedbacksRef = db.collection(Constantes.COLLECTION_PERFIL)
            .document(idUsuario)
            .collection(Constantes.COLLECTION_USUARIO_FEEDBACK)

        feedbacksRef.get()
            .addOnSuccessListener { documents ->
                feedbacksPorUsuario.clear()
                for (document in documents) {
                    val data = document.getTimestamp(Constantes.CAMPO_FEEDBACK_DATA)?.toDate()
                    val nota = document.getLong(Constantes.CAMPO_FEEDBACK_NOTA)?.toInt()
                    val descricao = document.getString(Constantes.CAMPO_FEEDBACK_DESCRICAO)
                    val usuario = document.getString(Constantes.CAMPO_FEEDBACK_USUARIO)
                    if (data != null && nota != null && descricao != null && usuario != null) {
                        if (!feedbacksPorUsuario.containsKey(usuario)) {
                            feedbacksPorUsuario[usuario] = mutableListOf()
                        }
                        feedbacksPorUsuario[usuario]?.add(
                            mapOf(
                                Constantes.CAMPO_FEEDBACK_DATA to data,
                                Constantes.CAMPO_FEEDBACK_NOTA to nota,
                                Constantes.CAMPO_FEEDBACK_DESCRICAO to descricao,
                                Constantes.CAMPO_FEEDBACK_USUARIO to usuario
                            )
                        )
                    }
                }
                dadosCarregados.value = true
            }
            .addOnFailureListener { e ->
                println("$mensagemErroFeedback $e")
            }
    }

    LaunchedEffect(Unit) {
        carregarFeedbacks()
    }

    if (dadosCarregados.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = stringResource(id = R.string.feedback_recebido),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Black, thickness = 3.dp)
            Spacer(modifier = Modifier.height(5.dp))

            feedbacksPorUsuario.forEach { (usuario, feedbacks) ->
                LazyColumn {
                    items(feedbacks) { feedback ->
                        Text(text = "$feedbackUsuario ${feedback[Constantes.CAMPO_FEEDBACK_USUARIO]}")
                        Text("$feedbackData ${ feedback[Constantes.CAMPO_FEEDBACK_DATA]}" )
                        Text(text = "$feedbackNota ${feedback[Constantes.CAMPO_FEEDBACK_NOTA]} ")
                        Text(text = "$feedbackDescricao ${feedback[Constantes.CAMPO_FEEDBACK_DESCRICAO]}")
                        Spacer(modifier = Modifier.height(3.dp))
                        Divider(color = Color.Black, thickness = 2.dp)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }


            }
        }
    } else {

        Text(stringResource(id = R.string.mensagem_carregando_feedbacks))
    }
}
