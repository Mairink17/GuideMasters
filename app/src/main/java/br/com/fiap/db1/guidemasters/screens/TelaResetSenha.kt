package br.com.fiap.db1.guidemasters.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TelaResetSenha(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var confirmarEmail by remember { mutableStateOf("") }
    var erroEmail by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mensagemSucesso = stringResource(id = R.string.mensagem_reset_senha_sucesso)
    val mensagemErro = stringResource(id = R.string.mensagem_reset_senha_erro)
    val mensagemErroEmailDiferente = stringResource(id = R.string.mensagem_reset_senha_erro_email_diferente)

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(stringResource(id = R.string.texto_reset_senha)) },
        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.preencha_campos_reset_senha),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.email_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = erroEmail
                )

                OutlinedTextField(
                    value = confirmarEmail,
                    onValueChange = { confirmarEmail = it },
                    label = { Text(stringResource(id = R.string.confirme_email)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = erroEmail
                )
            }
        },
        confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(DarkBlue),
                onClick = {
                    if (email == confirmarEmail) {
                        // Lógica para reset de senha
                        val auth = FirebaseAuth.getInstance()
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, mensagemSucesso, Toast.LENGTH_SHORT).show()
                                    onConfirmar()
                                } else {
                                    Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // Emails não correspondem, exibir uma mensagem de erro
                        Toast.makeText(context, mensagemErroEmailDiferente, Toast.LENGTH_SHORT).show()
                        erroEmail = true
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
