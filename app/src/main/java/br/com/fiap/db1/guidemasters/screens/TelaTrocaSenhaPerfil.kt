package br.com.fiap.db1.guidemasters.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


@Composable
fun TelaTrocaSenhaPerfil(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    var senhaAntiga by remember { mutableStateOf("") }
    var senhaNova by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var senhaVisivelNova by remember { mutableStateOf(false) }
    var erroSenha by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mensagemSucesso = stringResource(id = R.string.mensagem_troca_senha_sucesso)
    val mensagemErroTrocaSenha = stringResource(id = R.string.mensagem_troca_senha_erro)
    val mensagemErroValidacaoSenha = stringResource(id = R.string.mensagem_troca_senha_erro_validacao)

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(stringResource(id = R.string.texto_trocar_senha)) },
        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.preencha_campos_senha),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = senhaAntiga,
                    onValueChange = { senhaAntiga = it },
                    label = { Text(stringResource(id = R.string.senha_antiga)) },
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
                    },
                    isError = erroSenha
                )

                OutlinedTextField(
                    value = senhaNova,
                    onValueChange = { senhaNova = it },
                    label = { Text(stringResource(id = R.string.mensagem_troca_senha)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (senhaVisivelNova) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { senhaVisivelNova = !senhaVisivelNova }) {
                            Icon(
                                if (senhaVisivelNova) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.altera_visao_senha)
                            )
                        }
                    },
                    isError = erroSenha
                )
            }
        },
        confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(DarkBlue),
                onClick = {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let { user ->
                        val credential = EmailAuthProvider.getCredential(user.email!!, senhaAntiga)
                        user.reauthenticate(credential)
                            .addOnCompleteListener { reauthTask ->
                                if (reauthTask.isSuccessful) {
                                    user.updatePassword(senhaNova)
                                        .addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                Toast.makeText(context, mensagemSucesso, Toast.LENGTH_SHORT).show()
                                                onConfirmar()

                                            } else {
                                                Toast.makeText(context, mensagemErroTrocaSenha, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    Toast.makeText(context, mensagemErroValidacaoSenha, Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            ) {
                Text(stringResource(id = R.string.botao_confirmar))
            }
        },
        dismissButton = {
            Button(colors = ButtonDefaults.buttonColors(DarkBlue),
                onClick = onCancelar
            ) {
                Text(stringResource(id = R.string.botao_cancelar))
            }
        }
    )
}


