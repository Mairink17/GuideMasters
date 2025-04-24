package br.com.fiap.db1.guidemasters.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.repository.ChatRepository
import br.com.fiap.db1.guidemasters.repository.getDadosUsuarioLogado
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import br.com.fiap.db1.guidemasters.R



@SuppressLint("RememberReturnType")
@Composable
fun TelaChat(navController: NavHostController, perfil: Perfil) {


    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    val idUsuarioLogado = autenticacao.currentUser?.uid


    val chatRepository = remember { ChatRepository() }
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var textState by remember { mutableStateOf("") }
    var isFirstMessage by remember { mutableStateOf(true) }

    val scrollState = rememberLazyListState()

    LaunchedEffect(key1 = idUsuarioLogado, key2 = perfil.idUsuario) {
        if (idUsuarioLogado != null) {
            chatRepository.getConversation(userId = idUsuarioLogado, contactId = perfil.idUsuario) { loadedMessages ->
                val messagesList = loadedMessages.map { messageMap ->
                    val text = messageMap[Constantes.CAMPO_CHAT_TEXTO] ?: ""
                    val isSentByUser = messageMap[Constantes.CAMPO_CHAT_USUARIO_ENVIO_MENSAGEM] == idUsuarioLogado
                    Message(text, isSentByUser)
                }
                messages = messagesList
            }

        }
    }

    if (messages.isNotEmpty()) {
        LaunchedEffect(true) {
            if (isFirstMessage) {
                scrollState.scrollToItem(messages.size - 1)
                isFirstMessage = false
            }
        }
    }

    Column {
        TopAppBarContent(perfil = perfil, navController)

        Box(
            modifier = Modifier.weight(1f)
        ) {
            ChatContent(messages = messages, scrollState = scrollState)
        }
        BottomAppBarContent(
            textState = textState,
            onTextChange = { newText -> textState = newText },
            onSendClicked = {
                if (textState.isNotEmpty()) {
                    if (idUsuarioLogado != null) {
                        chatRepository.enviaUsuarioConectado(
                            userId = idUsuarioLogado,
                            contactId = perfil.idUsuario,
                            message = textState
                        )
                        chatRepository.enviaDestinatario(
                            userId = idUsuarioLogado,
                            contactId = perfil.idUsuario,
                            message = textState
                        )
                    }
                    messages = messages + Message(textState, true)
                    textState = ""
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContent(perfil: Perfil, navController: NavHostController) {
    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    val idUsuarioLogado = autenticacao.currentUser?.uid

    var perfilUsuarioLogado by remember { mutableStateOf<Perfil?>(null) }

    getDadosUsuarioLogado {
        perfilUsuarioLogado = it
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(perfil.urlPerfil),
                    contentDescription = "Foto do perfil",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    perfil.nome,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Constantes.URL_TELA_CONTATOS) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },

        actions = {

            IconButton(onClick = { navController.navigate("${Constantes.URL_TELA_CONTATOS_PERFIL}/${perfil.idUsuario}") }) {
                Icon(Icons.Default.Person, contentDescription = stringResource(id = R.string.tela_chat_visualizar_perfil))
            }
            IconButton(onClick = { navController.navigate("${Constantes.URL_TELA_FEEDBACK}/${idUsuarioLogado}/${perfil.idUsuario}/${perfilUsuarioLogado?.nome}") }) {
                Icon(Icons.Default.StarRate, contentDescription = stringResource(id = R.string.tela_chat_enviar_feedback))
            }
        }


    )
}

@Composable
fun ChatContent(messages: List<Message>, scrollState: LazyListState) {
    LazyColumn(
        reverseLayout = true,
        state = scrollState,
        modifier = Modifier.padding(16.dp)
    ) {
        items(messages.reversed()) { message ->
            MessageBubble(message)
        }
    }
}

@Composable
fun BottomAppBarContent(textState: String, onTextChange: (String) -> Unit, onSendClicked: () -> Unit) {
    val placeholderMensagem = stringResource(id = R.string.placeholder_mensagem_chat)

    BottomAppBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textState,
                onValueChange = { onTextChange(it) },
                modifier = Modifier.weight(1f),
                placeholder = { Text(placeholderMensagem) }
            )
            IconButton(
                onClick = {
                    onSendClicked()
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(id = R.string.botao_enviar)
                )
            }
        }
    }
}


@Composable
fun MessageBubble(message: Message) {
    val backgroundColor = if (message.isSentByUser) {
        Color.LightGray
    } else {
        DarkBlue
    }

    val contentColor = if (message.isSentByUser) {
        Color.Black
    } else {
        Color.White
    }

    val alignment = if (message.isSentByUser) {
        Alignment.TopEnd
    } else {
        Alignment.TopStart
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor
        )
    }
}


data class Message(val text: String, val isSentByUser: Boolean)
