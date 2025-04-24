package br.com.fiap.db1.guidemasters

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.model.Perfil
import br.com.fiap.db1.guidemasters.notificacoes.NotificacoesEmBackground
import br.com.fiap.db1.guidemasters.notificacoes.checkNotificationPermission
import br.com.fiap.db1.guidemasters.repository.RetornarPerfilUnico
import br.com.fiap.db1.guidemasters.repository.isUserLoggedIn
import br.com.fiap.db1.guidemasters.screenViewModel.TelaLoginScreenViewModel
import br.com.fiap.db1.guidemasters.screens.TelaCadastro
import br.com.fiap.db1.guidemasters.screens.TelaChat
import br.com.fiap.db1.guidemasters.screens.TelaFeedback
import br.com.fiap.db1.guidemasters.screens.TelaFeedbacksRecebidos
import br.com.fiap.db1.guidemasters.screens.TelaListaContatosLikes
import br.com.fiap.db1.guidemasters.screens.TelaLogin
import br.com.fiap.db1.guidemasters.screens.TelaPerfilContato
import br.com.fiap.db1.guidemasters.screens.TelaPerfilUsuario
import br.com.fiap.db1.guidemasters.screens.TelaPrincipal
import br.com.fiap.db1.guidemasters.ui.theme.GuideMastersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        startService(Intent(this, NotificacoesEmBackground::class.java))

        setContent {

            GuideMastersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var usuarioLogado by remember { mutableStateOf(isUserLoggedIn()) }
                    var direcionaContatos by remember { mutableStateOf(false) }

                    if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {

                        checkNotificationPermission(this@MainActivity)
                        }
                    if (intent.hasExtra("destination")) {
                        val destination = intent.getStringExtra("destination")
                        if (destination == "telaContatos") {
                            direcionaContatos = true
                        }else{
                            direcionaContatos = false
                        }

                    }

                    NavHost(navController = navController, startDestination = if(!usuarioLogado) Constantes.URL_LOGIN else
                        if(direcionaContatos)  Constantes.URL_TELA_CONTATOS else Constantes.URL_TELA_PRINCIPAL) {

                            composable(Constantes.URL_LOGIN){
                                TelaLogin(telaLoginScreenViewModel = TelaLoginScreenViewModel(),navController)
                            }
                            composable(Constantes.URL_TELA_PRINCIPAL){
                                TelaPrincipal(navController)
                            }

                            composable(Constantes.URL_PERFIL_USUARIO){
                                TelaPerfilUsuario(navController)
                            }

                            composable(Constantes.URL_CRIAR_CADASTRO){
                                TelaCadastro(navController)
                            }

                            composable(Constantes.URL_TELA_CONTATOS) {
                                TelaListaContatosLikes(navController)
                            }

                        composable("${Constantes.URL_TELA_FEEDBACK}/{idOrigem}/{idDestino}/{nomeFeedBack}") {
                            val backStackEntry = navController.currentBackStackEntryAsState().value
                            val idOrigem = backStackEntry?.arguments?.getString("idOrigem")
                            val idDestino = backStackEntry?.arguments?.getString("idDestino")
                            val nomeFeedback = backStackEntry?.arguments?.getString("nomeFeedBack")

                            if (idOrigem != null) {
                                if (idDestino != null) {
                                    if (nomeFeedback != null) {
                                        TelaFeedback(navController, idOrigem = idOrigem , idDestino = idDestino , nomeUsuarioFeedback = nomeFeedback)
                                    }
                                }
                            }
                        }

                        composable("${Constantes.URL_TELA_FEEDBACKS_RECEBIDOS}/{idUsuario}") {
                            val backStackEntry = navController.currentBackStackEntryAsState().value
                            val idUsuario = backStackEntry?.arguments?.getString("idUsuario")

                            if (idUsuario != null) {
                                TelaFeedbacksRecebidos(navController, idUsuario = idUsuario )
                            }
                        }

                        composable("${Constantes.URL_TELA_CHAT}/{idPerfil}") {
                            val backStackEntry = navController.currentBackStackEntryAsState().value
                            val idPerfil = backStackEntry?.arguments?.getString("idPerfil")
                            var existeContato by remember { mutableStateOf(Perfil(opcoesSelecionadas = null)) }

                            if (idPerfil != null) {

                                RetornarPerfilUnico(idPerfil){
                                    perfil ->
                                    if(perfil != null){
                                        existeContato = perfil
                                    }
                                }

                                if(!existeContato.idUsuario.isNullOrEmpty()){
                                    TelaChat(navController = navController, perfil = existeContato )
                                }
                            }

                        }

                        composable("${Constantes.URL_TELA_CONTATOS_PERFIL}/{idPerfil}") {
                            val backStackEntry = navController.currentBackStackEntryAsState().value
                            val idPerfil = backStackEntry?.arguments?.getString("idPerfil")
                            var existeContato by remember { mutableStateOf(Perfil(opcoesSelecionadas = null)) }

                            if (idPerfil != null) {

                                RetornarPerfilUnico(idPerfil){
                                        perfil ->
                                    if(perfil != null){
                                        existeContato = perfil

                                    }
                                }

                                if(!existeContato.idUsuario.isNullOrEmpty()){
                                    TelaPerfilContato(navController = navController, perfil = existeContato )
                                }
                            }

                        }

                        }

                }
            }
        }
    }
}
