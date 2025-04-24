package br.com.fiap.db1.guidemasters.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.repository.RetornarListaFireBaseLikesAtivos
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun TelaListaContatosLikes(navController: NavHostController) {
    var perfis by remember { mutableStateOf<List<Perfil>>(emptyList()) }

    val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }
    val bancoDados = FirebaseFirestore.getInstance()

    val idUsuarioLogado = autenticacao.currentUser?.uid

    val docRef = idUsuarioLogado?.let {
        bancoDados.collection(Constantes.COLLECTION_PERFIL).document(it)
    }
    docRef?.get()?.addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
            val tipoMentoria = documentSnapshot.get(Constantes.CAMPO_PERFIL) as String?
            if (tipoMentoria != null) {
                autenticacao.uid?.let {
                    RetornarListaFireBaseLikesAtivos(it, numeroFuncao = 2) { listaPerfis ->
                        perfis = listaPerfis
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.mensagem_tela_contatos),
                fontSize = 20.sp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(perfis) { perfil ->
                    PerfilItem(perfil = perfil) { navController.navigate("${Constantes.URL_TELA_CHAT}/${perfil.idUsuario}") }
                }
            }
        }

        Button(
            onClick = { navController.navigate(Constantes.URL_TELA_PRINCIPAL) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(DarkBlue)
        ) {
            Text(stringResource(id = R.string.botao_voltar))
        }
    }
}

@Composable
fun PerfilItem(
    perfil: Perfil,
    onChatClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onChatClicked() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            rememberImagePainter(
                perfil.urlPerfil,
                builder = {
                    placeholder(R.drawable.padrao)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        Text(
            text = perfil.nome,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )

    }
}
