package br.com.fiap.db1.guidemasters.screens

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.funcionalidades.fazerLogin
import br.com.fiap.db1.guidemasters.screenViewModel.TelaLoginScreenViewModel
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun TelaLogin(telaLoginScreenViewModel: TelaLoginScreenViewModel, navController: NavController){

    val mensagemEmailNaoCadastrado = stringResource(id = R.string.mensagem_email_nao_cadastrado)
    val tituloEmailNaoCadastrado = stringResource(id = R.string.titulo_email_nao_cadastrado)
    val mensagemVerificarEmail = stringResource(id = R.string.mensagem_email_verificar)

    val mensagemEmailNaoVerificado = stringResource(id = R.string.email_nao_verificado)
    val senha by telaLoginScreenViewModel.senha.observeAsState(initial = "")
    val login by telaLoginScreenViewModel.login.observeAsState(initial = "")
    var senhaVisivel by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val erroLoginSenha = stringResource(id = R.string.erro_login_senha)
    val autenticacao by lazy{
        FirebaseAuth.getInstance()
    }

    if(autenticacao.uid != null){
        navController.navigate(Constantes.URL_TELA_PRINCIPAL)
    }
    var isResetEmail by remember { mutableStateOf(false) }

    if (isResetEmail) {
        TelaResetSenha(
            onConfirmar = {
                isResetEmail = false
            },
            onCancelar = {
                isResetEmail = false
            }
        )
    }

    Box(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoguidemasters),
            contentDescription = "logo Guide Masters",
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = login,
                onValueChange = { telaLoginScreenViewModel.onLoginChanged(it)  },
                label = { Text(stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { telaLoginScreenViewModel.onSenhaChanged(it) },
                label = { Text(stringResource(id = R.string.senha_label)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = stringResource(id = R.string.altera_visao_senha)
                        )
                    }
                }, singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    fazerLogin(login.trimEnd(), senha) { loginSucesso : Boolean ->
                        if (loginSucesso) {
                            val user = autenticacao.currentUser
                            if (user != null) {
                                if (user.isEmailVerified) {
                                    val db = Firebase.firestore
                                    val docRef = db.collection(Constantes.COLLECTION_PERFIL)
                                        .document(user.uid)

                                    docRef
                                        .update(Constantes.CAMPO_PERFIL_ATIVO, true)
                                        .addOnSuccessListener {
                                            Log.d(TAG, "Campo 'perfilAtivo' atualizado com sucesso!")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Erro ao atualizar campo 'perfilAtivo'", e)
                                        }


                                    navController.navigate(Constantes.URL_TELA_PRINCIPAL)
                                }else{
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle(tituloEmailNaoCadastrado)
                                    builder.setMessage(mensagemEmailNaoCadastrado)

                                    builder.setPositiveButton("Sim") { dialog, which ->
                                        user.sendEmailVerification()
                                    }

                                    builder.setNegativeButton("Não") { dialog, which ->

                                        Toast.makeText(context, mensagemVerificarEmail, Toast.LENGTH_SHORT).show()

                                    }


                                    val dialog = builder.create()
                                    dialog.show()
                                    autenticacao.signOut()
                                    Toast.makeText(context,mensagemEmailNaoVerificado, Toast.LENGTH_SHORT).show()


                                }

                            }


                        } else {
                            val builder = AlertDialog.Builder(context)
                            builder.setMessage(erroLoginSenha)
                                .setCancelable(false)
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }
                            val alert = builder.create()
                            alert.show()

                        }
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(DarkBlue)
            ) {
                Text(stringResource(id = R.string.login_label))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.mensagem_sem_conta),
                    color = Color.DarkGray
                )
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.cadastre_se)),
                    onClick = {
                              navController.navigate(Constantes.URL_CRIAR_CADASTRO)
                    },
                    style = MaterialTheme.typography
                        .bodyMedium
                        .merge()
                        .copy(textDecoration = TextDecoration.Underline)
                )

            }
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.mensagem_esqueceu_senha),
                    color = Color.DarkGray
                )
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.mensagem_clique_aqui)),
                    onClick = {
                        isResetEmail = true
                    },
                    style = MaterialTheme.typography
                        .bodyMedium
                        .merge()
                        .copy(textDecoration = TextDecoration.Underline)
                )

            }

        }


    }


}


