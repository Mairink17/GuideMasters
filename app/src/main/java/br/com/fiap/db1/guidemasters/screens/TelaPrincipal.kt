package br.com.fiap.db1.guidemasters.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.componentes.DropBoxFiltro
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.funcionalidades.AnimacaoMatch
import br.com.fiap.db1.guidemasters.funcionalidades.hashTagsMentoria
import br.com.fiap.db1.guidemasters.funcionalidades.textoPerfilUsuario
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.repository.RetornarListaFireBase
import br.com.fiap.db1.guidemasters.repository.adicionarPerfilInteracao
import br.com.fiap.db1.guidemasters.repository.calcularMediaFeedback
import br.com.fiap.db1.guidemasters.repository.darLike
import br.com.fiap.db1.guidemasters.repository.inserirMatchsAtivos
import br.com.fiap.db1.guidemasters.repository.verificarLike
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue


@Composable
fun TelaPrincipal(navController: NavHostController) {

    val bancoDados by lazy{
        FirebaseFirestore.getInstance()
    }
    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }


    val context = LocalContext.current


    var perfis by remember { mutableStateOf<List<Perfil>>(emptyList()) }

    var opcoesFiltro by remember { mutableStateOf<List<String>>(emptyList()) }

    val valorArrastarTela : Int = 180

    val idUsuarioLogado = autenticacao.currentUser?.uid

    if(idUsuarioLogado.isNullOrEmpty()){
        navController.navigate(Constantes.URL_LOGIN)
    }

    if(autenticacao.currentUser?.isEmailVerified != true){
        navController.navigate(Constantes.URL_LOGIN)
    }

    val docRef = idUsuarioLogado?.let {
        bancoDados.collection(Constantes.COLLECTION_PERFIL).document(
            it
        )
    }

    var mediaAvaliacao by remember { mutableStateOf("") }

    var filtroSelecionado by remember { mutableStateOf("") }

    docRef?.get()?.addOnSuccessListener { documentSnapshot ->
        if (documentSnapshot.exists()) {
            opcoesFiltro = (documentSnapshot.get(Constantes.CAMPO_OPCOES_MENTORIA) as List<String>?)!!

            val tipoMentoria = documentSnapshot.get(Constantes.CAMPO_PERFIL) as String?
            if (tipoMentoria != null) {
                RetornarListaFireBase(tipoMentoria , numeroFuncao = 1 , filtroSelecionado , opcoesFiltro ) { listaPerfis ->
                    perfis = listaPerfis
                }
            }

        }
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isDragging by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var isMatched by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                //.padding(5.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(stringResource(id = R.string.app_name), color = DarkBlue, fontSize = 12.sp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(Color.White)
                    .offset(x = offsetX.dp)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, panGesture, _, _ ->
                            val scaleFactor = 0.3f

                            offsetX += panGesture.x * scaleFactor
                            offsetX = offsetX.coerceIn(-200f, 200f)

                            if (panGesture.x != 0f) {
                                isDragging = true
                            } else if (isDragging) {
                                isDragging = false
                                if (offsetX.absoluteValue > valorArrastarTela) {

                                    currentIndex++
                                    if (currentIndex >= perfis.size) {
                                        currentIndex = 0
                                    }
                                }
                                offsetX = 0f
                            }
                        }
                    }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                if (offsetX.absoluteValue > valorArrastarTela) {

                    if (currentIndex < perfis.size) {
                        if(offsetX > valorArrastarTela){
                            autenticacao.uid?.let { darLike(perfis[currentIndex].idUsuario, it) }
                            autenticacao.uid?.let {
                                verificarLike(idPerfilAlvo = perfis[currentIndex].idUsuario, it){ like->
                                    isMatched = like
                                    if(isMatched){
                                        inserirMatchsAtivos( idUsuarioMatch = perfis[currentIndex].idUsuario,
                                            idUsuarioLogado = autenticacao.uid!!
                                        )
                                        inserirMatchsAtivos( idUsuarioMatch = autenticacao.uid!!
                                            , idUsuarioLogado = perfis[currentIndex].idUsuario
                                        )
                                    }
                                }
                            }

                        }
                        adicionarPerfilInteracao(perfis[currentIndex].idUsuario , true)
                        //currentIndex++
                    }
                    offsetX = 0f
                }

                var isFlipped by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.9f)
                        .background(if (isMatched) Color.White.copy(alpha = 0.3f) else Color.White),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),                )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { isFlipped = !isFlipped },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isFlipped) {
                            if (currentIndex < perfis.size) {

                                Column(
                                    modifier = Modifier
                                        .padding( start = 4.dp, end = 4.dp)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(0.dp)

                                ) {

                                    Image(
                                        rememberImagePainter(
                                            perfis[currentIndex].urlPerfil,
                                            builder = {
                                                placeholder(R.drawable.padrao)
                                            }
                                        ),
                                        contentDescription = stringResource(id = R.string.imagem_perfil),
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 8.dp)
                                            .size(300.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .graphicsLayer(alpha = if (isMatched) 0.3f else 1f)
                                            .align(Alignment.CenterHorizontally),
                                        alignment = Alignment.TopCenter,
                                        contentScale = ContentScale.FillBounds
                                    )

                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontSize = 16.sp , color = if (isMatched) Color.Gray else Color.Black)) {
                                                append(stringResource(id = R.string.nome_perfil) + " ")
                                            }
                                            withStyle(style = SpanStyle(fontSize = 20.sp , color = if (isMatched) Color.Gray else Color.Black)) {
                                                append(perfis[currentIndex].nome)
                                            }
                                        }, modifier = Modifier//.padding(top = 4.dp)
                                        .align(Alignment.Start)
                                    )

                                    perfis[currentIndex].idUsuario.let {
                                        calcularMediaFeedback(it){
                                            mediaAvaliacao = it
                                        }
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        textoPerfilUsuario("Avaliação: ", mediaAvaliacao)
                                        IconButton(onClick = { navController.navigate("${Constantes.URL_TELA_FEEDBACKS_RECEBIDOS}/${perfis[currentIndex]?.idUsuario}") }) {
                                            Icon(Icons.Default.StarOutline, contentDescription = "Visualizar FeedBacks")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontSize = 16.sp , color = if (isMatched) Color.Gray else Color.Black)) {
                                                append(stringResource(id = R.string.idade_perfil) + " ")
                                            }
                                            withStyle(style = SpanStyle(fontSize = 20.sp , color = if (isMatched) Color.Gray else Color.Black)) {
                                                append(perfis[currentIndex].idade.toString())
                                            }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontSize = 16.sp , color = if (isMatched) Color.Gray else Color.Black)) {
                                                append(stringResource(id = R.string.grau_instrucao_card) +": ")
                                            }
                                            withStyle(style = SpanStyle(fontSize = 20.sp , color = if (isMatched) Color.Gray else Color.Black)) {
                                                append(perfis[currentIndex].grauInstrucao)
                                            }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(5.dp))

                                    Text(
                                        text = ""+ perfis[currentIndex].opcoesSelecionadas?.let {
                                            hashTagsMentoria(
                                                it
                                            )
                                        },
                                        style = TextStyle(fontSize = 17.sp, color = if (isMatched) Color.Gray else Color.Black)
                                    )
                                }

                            } else {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.padrao),
                                        contentDescription = "Profile Image",
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 8.dp)
                                            .size(300.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .graphicsLayer(alpha = if (isMatched) 0.3f else 1f)
                                            .align(Alignment.CenterHorizontally),
                                        alignment = Alignment.TopCenter,
                                        contentScale = ContentScale.FillBounds
                                    )

                                    Text(
                                        text = stringResource(id = R.string.mensagem_acabaram_perfis),
                                        style = TextStyle(fontSize = 18.sp)
                                    )

                                }

                            }

                        } else {
                            if (currentIndex < perfis.size) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)

                                ) {

                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                                append(
                                                    text = stringResource(id =R.string.formacao_academica_card) + "\n")
                                            }
                                            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                                                append(perfis[currentIndex].formacaoAcademica)
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))


                                    Text(
                                        buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                                append(
                                                    text = stringResource(id =R.string.experiencia_profissional_perfil) + "\n")
                                            }
                                            withStyle(style = SpanStyle(fontSize = 18.sp)) {
                                                append(perfis[currentIndex].experiencia)
                                            }
                                        }
                                    )

                                }

                            }else{
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)

                                ) {
                                    Text(
                                        text = stringResource(id = R.string.mensagem_acabaram_perfis),
                                        style = TextStyle(fontSize = 20.sp)
                                    )

                                }
                            }
                        }
                        if (isMatched) {
                            AnimacaoMatch (){

                            }
                            LaunchedEffect(true) {
                                delay(2000)
                                isMatched = false
                            }
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Column( modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Bottom) {
                DropBoxFiltro(
                    opcoesFiltro,
                    filtroSelecionado
                ) { selecionado ->
                    filtroSelecionado = selecionado
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    Button(
                        colors = ButtonDefaults.buttonColors(DarkBlue),
                        onClick = {
                            if (offsetX.absoluteValue <= 200) {
                                offsetX -= 200f
                            }
                        }
                    ) {
                        Icon(Icons.Default.ThumbDown, contentDescription = "Descartar")
                    }

                    Button(colors = ButtonDefaults.buttonColors(DarkBlue),
                        onClick = {
                            navController.navigate(Constantes.URL_TELA_CONTATOS)

                        },
                        modifier = Modifier
                            .padding(5.dp)
                            .width(105.dp)
                    ) {
                        Text(stringResource(id = R.string.botao_contatos))
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(DarkBlue),
                        onClick = {
                            if (offsetX.absoluteValue <= 200) {
                                offsetX += 200f
                            }
                        }
                    ) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "Curtir")
                    }

                }

                BottomAppBar(
                    //backgroundColor = Color.White,
                    //contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(DarkBlue),

                            onClick = {
                                autenticacao.signOut()
                                navController.navigate(Constantes.URL_LOGIN)
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Curtir")
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                            onClick = {
                                navController.navigate(Constantes.URL_PERFIL_USUARIO)
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(stringResource(id = R.string.botao_perfil))
                        }
                    }
                }



            }

        }
    }
}

