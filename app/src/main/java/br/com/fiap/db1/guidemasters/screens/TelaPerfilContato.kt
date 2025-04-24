package br.com.fiap.db1.guidemasters.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.funcionalidades.hashTagsMentoria
import br.com.fiap.db1.guidemasters.funcionalidades.textoPerfilUsuario
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.repository.calcularMediaFeedback
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue
import coil.compose.rememberImagePainter

@Composable
fun TelaPerfilContato(navController: NavHostController , perfil: Perfil) {

    var mediaAvaliacao by remember { mutableStateOf("") }

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
                                Color.LightGray
                            )
                            .size(300.dp)
                    ) {


                        Image(
                            painter = rememberImagePainter(perfil.urlPerfil),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(300.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    textoPerfilUsuario(stringResource(id = R.string.nome_perfil) , perfil.nome ?: "")

                    calcularMediaFeedback(perfil.idUsuario){
                        mediaAvaliacao = it
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        textoPerfilUsuario("Avaliação:", mediaAvaliacao)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { navController.navigate("${Constantes.URL_TELA_FEEDBACKS_RECEBIDOS}/${perfil.idUsuario}") }) {
                            Icon(Icons.Default.StarOutline, contentDescription = "Visualizar FeedBacks")
                        }
                    }



                    Spacer(modifier = Modifier.height(6.dp))

                    textoPerfilUsuario(texto1 = stringResource(id = R.string.tipo_perfil_perfil), texto2 = perfil.perfil.toString())

                    Spacer(modifier = Modifier.height(6.dp))

                    textoPerfilUsuario(texto1 = stringResource(id = R.string.idade_perfil), texto2 = perfil.idade.toString())

                    Spacer(modifier = Modifier.height(6.dp))

                    textoPerfilUsuario(
                        texto1 = stringResource(id = R.string.grau_instrucao_dropbox),
                        texto2 = perfil.grauInstrucao
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    perfil.formacaoAcademica.let {
                        textoPerfilUsuario(
                            texto1 = stringResource(id = R.string.formacao_academica),
                            texto2 = it
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    textoPerfilUsuario(
                        texto1 = stringResource(id = R.string.experiencia_profissional_perfil),
                        texto2 = ""
                    )

                    textoPerfilUsuario(
                        texto1 = "",
                        texto2 = perfil.experiencia
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    textoPerfilUsuario(
                        texto1 = stringResource(id = R.string.url_linkedin_label),
                        texto2 = perfil.linkedin
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text( text = ""+ perfil.opcoesSelecionadas?.let {
                        hashTagsMentoria(
                            it
                        )
                    })

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                            onClick = {
                                navController.navigateUp()
                            }
                        ) {
                            Text(stringResource(id = R.string.botao_voltar))
                        }
                    }
                }
            }
        }
    }
}






