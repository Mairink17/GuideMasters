package br.com.fiap.db1.guidemasters.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.componentes.CaixasDeSelecaoPorNome
import br.com.fiap.db1.guidemasters.componentes.CameraCaptureScreen
import br.com.fiap.db1.guidemasters.componentes.DropBoxGrauDeInstrucao
import br.com.fiap.db1.guidemasters.componentes.SelecaoFotoGaleria
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.funcionalidades.textoPerfilUsuario
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.repository.atualizarDadosUsuario
import br.com.fiap.db1.guidemasters.repository.cadastrarFotoUsuarioBitmap
import br.com.fiap.db1.guidemasters.repository.cadastrarFotoUsuarioUri
import br.com.fiap.db1.guidemasters.repository.calcularMediaFeedback
import br.com.fiap.db1.guidemasters.repository.criarRetornarGrauDeInstrucao
import br.com.fiap.db1.guidemasters.repository.criarRetornarOpcoesMentoria
import br.com.fiap.db1.guidemasters.repository.getDadosUsuarioLogado
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import br.com.fiap.db1.guidemasters.ui.theme.LightRed
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


@Composable
fun TelaPerfilUsuario(navController: NavHostController) {
    val context = LocalContext.current

    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }
    val bancoDados by lazy{
        FirebaseFirestore.getInstance()
    }
    val armazenamentoStorage by lazy{
        FirebaseStorage.getInstance()
    }

    var listaOpcoes by remember { mutableStateOf(setOf<String>()) }

    criarRetornarOpcoesMentoria { listaRetornada ->
        listaOpcoes = listaOpcoes.plus(listaRetornada)
    }

    var perfilUsuarioLogado by remember { mutableStateOf<Perfil?>(null) }

   getDadosUsuarioLogado { perfil ->
       perfil?.let {

           perfilUsuarioLogado = it
       }
   }
    val mensagemErroOpcao = stringResource(id = R.string.erro_opcoes_mentoria)

    var senhaVisivel by remember { mutableStateOf(false) }
    var senhaVisivelNova by remember { mutableStateOf(false) }

    var fotoSelecionada: Uri? by remember { mutableStateOf(null) }
    var bitmapImagemSelecionada : Bitmap? by remember { mutableStateOf(null) }

    var email by remember { mutableStateOf("") }
    var senhaAntiga by remember { mutableStateOf("") }
    var senhaNova by remember { mutableStateOf("") }

    var cpf by remember { mutableStateOf("") }

    var experiencia by remember { mutableStateOf("") }

    perfilUsuarioLogado?.experiencia?.let {
        experiencia = it
    }

    var linkedin by remember { mutableStateOf("") }

    perfilUsuarioLogado?.linkedin?.let {
        linkedin = it
    }

    var formacaoAcademica by remember { mutableStateOf("") }

    perfilUsuarioLogado?.formacaoAcademica?.let {
        formacaoAcademica = it
    }


    var listaGrausDeInstrucao by remember { mutableStateOf(setOf<String>()) }

    criarRetornarGrauDeInstrucao{
        listaGrausDeInstrucao = listaGrausDeInstrucao.plus(it)
    }
    var grauDeInstrucao  by remember { mutableStateOf("") }

    perfilUsuarioLogado?.grauInstrucao?.let{
        grauDeInstrucao = it
    }

    var mediaAvaliacao by remember { mutableStateOf("") }

    var nome by remember { mutableStateOf("") }

    var opcoesSelecionadas by remember { mutableStateOf(setOf<String>()) }


    perfilUsuarioLogado?.opcoesSelecionadas?.let {
        opcoesSelecionadas = it.toSet()
    }

    var erroSenha by remember() {
        mutableStateOf(false)
    }

    var erroExperiencia by remember{
        mutableStateOf(false)
    }

    var erroOpcoesSelecionadas by remember{
        mutableStateOf(false)
    }

    var erroImagem by remember{
        mutableStateOf(false)
    }

    var isCadastroOk by remember{
        mutableStateOf(false)
    }

    var isLinkedinOk by remember{
        mutableStateOf(false)
    }

    var isFormacaoAcademicaErro by remember{
        mutableStateOf(false)
    }

    var isErroGrauInstrucao by remember{
        mutableStateOf(false)
    }


    var dataParser : String = ""

    var trocarSenhaDialogAberta by remember { mutableStateOf(false) }

    if (trocarSenhaDialogAberta) {
        TelaTrocaSenhaPerfil(
            onConfirmar = {
                trocarSenhaDialogAberta = false
            },
            onCancelar = {
                trocarSenhaDialogAberta = false
            }
        )
    }
    Text(stringResource(id = R.string.app_name), color = DarkBlue, fontSize = 12.sp)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            item {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(
                                if (erroImagem) {
                                    LightRed
                                } else {
                                    Color.LightGray
                                }
                            )
                            .size(300.dp)
                    ) {


                        if (fotoSelecionada != null) {
                            Image(
                                painter = rememberImagePainter(fotoSelecionada),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(300.dp)
                            )
                        }else if(bitmapImagemSelecionada != null){
                            Image(
                                bitmap = bitmapImagemSelecionada!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(300.dp)
                            )
                        } else {

                            Image(
                                rememberImagePainter(
                                   perfilUsuarioLogado?.urlPerfil,
                                    builder = {
                                        placeholder(R.drawable.padrao)
                                    }

                                ),
                                contentDescription = stringResource(id = R.string.imagem_perfil),
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(300.dp)
                            )

                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        CameraCaptureScreen(context) { bitmap ->
                            bitmapImagemSelecionada = bitmap
                            fotoSelecionada = null
                        }


                        SelecaoFotoGaleria(context) { uri ->
                            fotoSelecionada = uri
                            bitmapImagemSelecionada = null
                        }

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                append(stringResource(id = R.string.email_perfil))
                            }
                            withStyle(style = SpanStyle(fontSize = 20.sp)) {
                                append(perfilUsuarioLogado?.email ?: "")
                            }
                        }
                    )


                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            trocarSenhaDialogAberta = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(DarkBlue)

                        ) {

                        Text(stringResource(id = R.string.trocar_senha))

                    }

                    Spacer(modifier = Modifier.height(6.dp))


                    textoPerfilUsuario(texto1 = stringResource(id = R.string.tipo_perfil_perfil), texto2 = perfilUsuarioLogado?.perfil.toString())


                    Spacer(modifier = Modifier.height(6.dp))

                    textoPerfilUsuario(stringResource(id = R.string.nome_perfil) , perfilUsuarioLogado?.nome ?: "")

                    Spacer(modifier = Modifier.height(6.dp))

                    perfilUsuarioLogado?.let {
                        calcularMediaFeedback(it.idUsuario){
                            mediaAvaliacao = it
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textoPerfilUsuario("Avaliação:", mediaAvaliacao)
                        IconButton(onClick = { navController.navigate("${Constantes.URL_TELA_FEEDBACKS_RECEBIDOS}/${perfilUsuarioLogado?.idUsuario}") }) {
                            Icon(Icons.Default.StarOutline, contentDescription = "Visualizar FeedBacks")
                        }
                    }


                    Spacer(modifier = Modifier.height(6.dp))


                    textoPerfilUsuario(texto1 = stringResource(id = R.string.cpf_perfil), texto2 = perfilUsuarioLogado?.cpf ?: "")

                    Spacer(modifier = Modifier.height(6.dp))

                    perfilUsuarioLogado?.dataNascimento?.let { textoPerfilUsuario(texto1 = stringResource(
                        id = R.string.data_nascimento
                    ), texto2 = it) }


                    Spacer(modifier = Modifier.height(10.dp))

                    perfilUsuarioLogado?.grauInstrucao?.let {
                        DropBoxGrauDeInstrucao(grausInstrucao = listaGrausDeInstrucao.toList(),
                            selectedGrauInstrucao = grauDeInstrucao,
                            onGrauInstrucaoSelected = { descricaoSelecionada ->
                                grauDeInstrucao = descricaoSelecionada
                            },isError = isErroGrauInstrucao
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = formacaoAcademica,
                        onValueChange = { formacaoAcademica = it },
                        label = { Text(stringResource(id = R.string.formacao_academica)) },
                        modifier = Modifier.fillMaxWidth(), isError = isFormacaoAcademicaErro
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = experiencia,
                        onValueChange = { experiencia = it },
                        label = { Text(stringResource(id = R.string.experiencia_label)) },
                        modifier = Modifier.fillMaxWidth(), isError = erroExperiencia
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = linkedin,
                        onValueChange = { linkedin = it },
                        label = { Text(stringResource(id = R.string.url_linkedin_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(5.dp))


                        CaixasDeSelecaoPorNome(
                            opcoes = listaOpcoes.toList(),
                            opcoesSelecionadas = opcoesSelecionadas,
                            onSelecaoChanged = { opcao, selecionada ->
                                opcoesSelecionadas = if (selecionada) {
                                    opcoesSelecionadas + opcao
                                } else {
                                    opcoesSelecionadas - opcao
                                }
                            }
                        )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(colors = ButtonDefaults.buttonColors(DarkBlue),

                            onClick = {
                                navController.navigateUp()
                            }
                        ) {
                            Text(stringResource(id = R.string.botao_voltar))
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                            onClick = {

                                erroExperiencia = experiencia.isEmpty()


                                erroOpcoesSelecionadas = opcoesSelecionadas.isEmpty()

                                isCadastroOk = !(
                                                erroExperiencia ||
                                                erroOpcoesSelecionadas
                                        )
                                if (isCadastroOk) {

                                            val idUsuarioLogado = autenticacao.currentUser?.uid

                                            val perfil = Perfil(
                                                nome = nome,
                                                email = email,
                                                experiencia = experiencia,
                                                cpf = cpf,
                                                linkedin = linkedin,
                                                opcoesSelecionadas = opcoesSelecionadas.toList(),
                                                formacaoAcademica = formacaoAcademica,
                                                grauInstrucao = grauDeInstrucao

                                            )

                                            atualizarDadosUsuario( perfil , idUsuarioLogado , context)

                                            if (idUsuarioLogado != null) {

                                                if (fotoSelecionada != null ) {

                                                    cadastrarFotoUsuarioUri(
                                                        fotoSelecionada,
                                                        idUsuarioLogado
                                                    )

                                                }
                                                if (bitmapImagemSelecionada != null) {
                                                    cadastrarFotoUsuarioBitmap(
                                                        bitmapImagemSelecionada,
                                                        idUsuarioLogado
                                                    )
                                                }

                                            }

                                }

                            },
                        ) {
                            Text(stringResource(id = R.string.botao_atualizar_cadastro))
                        }
                    }
                }
            }
        }
    }
}

