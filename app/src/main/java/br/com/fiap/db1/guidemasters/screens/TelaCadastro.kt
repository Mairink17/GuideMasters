package br.com.fiap.db1.guidemasters.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.componentes.BotaoPegarData
import br.com.fiap.db1.guidemasters.componentes.CaixasDeSelecaoPorNome
import br.com.fiap.db1.guidemasters.componentes.CameraCaptureScreen
import br.com.fiap.db1.guidemasters.componentes.DropBoxGrauDeInstrucao
import br.com.fiap.db1.guidemasters.componentes.SelecaoFotoGaleria
import br.com.fiap.db1.guidemasters.constantes.TipoPerfil
import br.com.fiap.db1.guidemasters.funcionalidades.calcularIdade
import br.com.fiap.db1.guidemasters.funcionalidades.stringParaDate
import br.com.fiap.db1.guidemasters.funcionalidades.validaUrlLinkedIn
import br.com.fiap.db1.guidemasters.funcionalidades.validarCPF
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.repository.cadastrarFotoUsuarioBitmap
import br.com.fiap.db1.guidemasters.repository.cadastrarFotoUsuarioUri
import br.com.fiap.db1.guidemasters.repository.cadastrarNovoUsuario
import br.com.fiap.db1.guidemasters.repository.criarRetornarGrauDeInstrucao
import br.com.fiap.db1.guidemasters.repository.criarRetornarOpcoesMentoria
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import br.com.fiap.db1.guidemasters.ui.theme.LightRed
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

@Composable
fun TelaCadastro(navController: NavHostController) {
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

    var listaGrausDeInstrucao by remember { mutableStateOf(setOf<String>()) }

    criarRetornarGrauDeInstrucao{
        listaGrausDeInstrucao = listaGrausDeInstrucao.plus(it)
    }
    var grauDeInstrucao by remember { mutableStateOf("") }


    val emailUso = stringResource(id = R.string.email_em_uso)
    val mensagemErroOpcao = stringResource(id = R.string.erro_opcoes_mentoria)
    val mensagemLimiteIdade = stringResource(id = R.string.mensagem_idade_minima)
    val mensagemCadastroComSucesso = stringResource(id = R.string.mensagem_cadastro_sucesso)
    val senhaFraca = stringResource(id = R.string.senha_fraca)
    val erroCadastro = stringResource(id = R.string.erro_cadastrar_usuario)
    val erroCadastrarUsuario = stringResource(id = R.string.erro_cadastrar_usuario_2)

    var senhaVisivel by remember { mutableStateOf(false) }
    var fotoSelecionada: Uri? by remember { mutableStateOf(null) }
    var bitmapImagemSelecionada : Bitmap? by remember { mutableStateOf(null) }

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var perfil by remember { mutableStateOf(TipoPerfil.MENTORADO) }
    var cpf by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var opcoesSelecionadas by remember { mutableStateOf(setOf<String>()) }
    var formacaoAcademica by remember { mutableStateOf("") }

    var erroEmail by remember() {
        mutableStateOf(false)
    }

    var erroSenha by remember() {
        mutableStateOf(false)
    }

    var erroCpf by remember() {
        mutableStateOf(false)
    }

    var erroDataNascimento by remember{
        mutableStateOf(false)
    }

    var erroExperiencia by remember{
        mutableStateOf(false)
    }

    var erroNome by remember{
        mutableStateOf(false)
    }

    var erroOpcoesSelecionadas by remember{
        mutableStateOf(false)
    }

    var erroGrauInstrucao by remember{
        mutableStateOf(false)
    }

    var erroFormacaoAcademica by remember{
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



    var dataParser : String = ""
    var dataConvertida : Date
    val formato = "dd/MM/yyyy"


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
                                painter = painterResource(id = R.drawable.padrao),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(300.dp)
                            )
                            Text(
                                stringResource(id = R.string.nenha_foto),
                                modifier = Modifier.align(Alignment.Center)
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

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(id = R.string.email_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = erroEmail
                    )

                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = { Text(stringResource(id = R.string.senha_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                Icon(
                                    if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = stringResource(id = R.string.altera_visao_senha)
                                )
                            }
                        }
                        , isError = erroSenha
                    )

                    Column {
                        Text(stringResource(id = R.string.tipo_perfil))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = perfil == TipoPerfil.MENTOR,
                                onClick = { perfil = TipoPerfil.MENTOR }
                            )
                            Text(stringResource(id = R.string.mentor))
                            Spacer(modifier = Modifier.width(16.dp))
                            RadioButton(
                                selected = perfil == TipoPerfil.MENTORADO,
                                onClick = { perfil = TipoPerfil.MENTORADO }
                            )
                            Text(stringResource(id = R.string.mentorado))
                        }
                    }

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text(stringResource(id = R.string.nome_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true, isError = erroNome

                    )

                    OutlinedTextField(
                        value = cpf,
                        onValueChange = { cpf = it },
                        label = { Text(stringResource(id = R.string.cpf_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true , isError = erroCpf
                    )


                    Spacer(modifier = Modifier.height(10.dp))

                    dataParser = BotaoPegarData( stringResource(id = R.string.pegar_data), dataNascimento,
                        if(!erroDataNascimento){
                            DarkBlue
                        }else{
                            Color.Red
                        },50)

                    Spacer(modifier = Modifier.height(10.dp))


                    DropBoxGrauDeInstrucao(grausInstrucao = listaGrausDeInstrucao.toList(),
                        selectedGrauInstrucao = grauDeInstrucao,
                        onGrauInstrucaoSelected = { descricaoSelecionada ->
                            grauDeInstrucao = descricaoSelecionada
                        },isError = erroGrauInstrucao
                        )

                    OutlinedTextField(
                        value = formacaoAcademica,
                        onValueChange = { formacaoAcademica = it },
                        label = { Text(stringResource(id = R.string.formacao_academica)) },
                        modifier = Modifier.fillMaxWidth(), isError = erroFormacaoAcademica
                    )


                    Spacer(modifier = Modifier.height(10.dp))


                    OutlinedTextField(
                        value = experiencia,
                        onValueChange = { experiencia = it },
                        label = { Text(stringResource(id = R.string.experiencia_label)) },
                        modifier = Modifier.fillMaxWidth(), isError = erroExperiencia
                    )

                    OutlinedTextField(
                        value = linkedin,
                        onValueChange = { linkedin = it },
                        label = { Text(stringResource(id = R.string.url_linkedin_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        singleLine = true, isError = isLinkedinOk
                    )
                    Spacer(modifier = Modifier.height(15.dp))

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
                    Spacer(modifier = Modifier.height(15.dp))


                    Button(
                        colors = ButtonDefaults.buttonColors(DarkBlue),
                        onClick = {

                            erroEmail = email.isEmpty()

                            erroSenha = senha.isEmpty()

                            erroCpf = !validarCPF(cpf)

                            if(linkedin.isNotEmpty()){
                                isLinkedinOk = !validaUrlLinkedIn(linkedin)
                            }

                            erroImagem = (bitmapImagemSelecionada == null && fotoSelecionada == null)

                            erroExperiencia = experiencia.isEmpty()

                            erroNome = nome.isEmpty()

                            erroOpcoesSelecionadas = opcoesSelecionadas.isEmpty()

                            erroDataNascimento = dataParser.isEmpty()

                            erroGrauInstrucao = grauDeInstrucao.isEmpty()

                            erroFormacaoAcademica = formacaoAcademica.isEmpty()

                            if(!erroDataNascimento){
                                dataConvertida = stringParaDate(dataParser, formato)
                                if(calcularIdade(dataConvertida)  < 16){
                                    erroDataNascimento = true
                                    Toast.makeText(context, mensagemLimiteIdade, Toast.LENGTH_SHORT).show()
                                }

                            }
                            isCadastroOk = !(erroEmail ||
                                    erroSenha ||
                                    erroCpf ||
                                    erroImagem ||
                                    erroExperiencia ||
                                    erroOpcoesSelecionadas ||
                                    isLinkedinOk ||
                                     erroGrauInstrucao ||
                                    erroFormacaoAcademica
                                    )
                            if(isCadastroOk){

                                autenticacao.createUserWithEmailAndPassword(email, senha ).addOnSuccessListener {
                                    val idUsuarioLogado = autenticacao.currentUser?.uid

                                    it.user?.sendEmailVerification()

                                    val perfil = Perfil(
                                        nome = nome,
                                        email = email,
                                        experiencia = experiencia,
                                        cpf = cpf,
                                        linkedin = linkedin,
                                        senha =  senha,
                                        dataNascimento = dataParser,
                                        perfil = perfil,
                                        opcoesSelecionadas = opcoesSelecionadas.toList(),
                                        formacaoAcademica = formacaoAcademica,
                                        grauInstrucao = grauDeInstrucao
                                    )




                                    if (idUsuarioLogado != null) {

                                        cadastrarNovoUsuario(perfil , idUsuarioLogado)

                                        if(fotoSelecionada != null){

                                            cadastrarFotoUsuarioUri(fotoSelecionada , idUsuarioLogado)

                                        }
                                        if(bitmapImagemSelecionada != null){
                                            cadastrarFotoUsuarioBitmap(bitmapImagemSelecionada , idUsuarioLogado)
                                        }
                                        Toast.makeText(context, mensagemCadastroComSucesso, Toast.LENGTH_SHORT).show()

                                        navController.navigateUp()

                                    }




                                }.addOnFailureListener { exception ->
                                    val erro = when (exception) {

                                        is FirebaseAuthUserCollisionException -> emailUso
                                        is FirebaseAuthWeakPasswordException -> senhaFraca
                                        else -> erroCadastro
                                    }
                                    Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
                                }


                            }else{
                                if(erroOpcoesSelecionadas){
                                    Toast.makeText(context,mensagemErroOpcao , Toast.LENGTH_SHORT).show()

                                }


                                Toast.makeText(context, erroCadastrarUsuario, Toast.LENGTH_SHORT).show()

                            }



                                  },
                    ) {
                        Text(stringResource(id = R.string.cadastrar_botao))
                    }
                }
            }
        }
    }
}

