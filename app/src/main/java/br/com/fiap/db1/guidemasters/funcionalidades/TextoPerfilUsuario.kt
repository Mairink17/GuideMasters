package br.com.fiap.db1.guidemasters.funcionalidades

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun textoPerfilUsuario(texto1 : String, texto2 : String){
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 16.sp)) {
                append(texto1)
            }
            withStyle(style = SpanStyle(fontSize = 20.sp)) {
                append(texto2)
            }
        }
    )
}