package br.com.fiap.db1.guidemasters.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.repository.darFeedBack
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue

@Composable
fun TelaFeedback(navController: NavController , idOrigem : String, idDestino : String , nomeUsuarioFeedback : String) {
    var iconeSelecionado by remember { mutableStateOf<FeedbackIcon?>(null) }
    var descricaoFeedback by remember { mutableStateOf("") }
    var isDescricaoErro by remember { mutableStateOf(false) }
    var isIconeSelecionadoErro by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val mensagemFeedBackSucesso = stringResource(id = R.string.mensagem_feedback_sucesso)
    val mensagemFeedBackCamposNaoPreenchidos = stringResource(id = R.string.mensagem_preenchimento_feedback)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(stringResource(id = R.string.mensagem_topo_feedback))
        Spacer(modifier = Modifier.height(16.dp))

        // Emotion icons
        EmotionIcons(
            selectedIcon = iconeSelecionado,
            onIconSelected = { newIcon -> iconeSelecionado = newIcon }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descricaoFeedback,
            onValueChange = { newDescription -> descricaoFeedback = newDescription },
            label = { Text(stringResource(id =  R.string.label_feedback) )},
            modifier = Modifier.fillMaxWidth(), isError = isDescricaoErro
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
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
                Button(colors = ButtonDefaults.buttonColors(DarkBlue),
                    onClick = {
                        if(descricaoFeedback.isEmpty()){
                            isDescricaoErro = true
                        }else {
                            isDescricaoErro = false
                        }
                        if((iconeSelecionado?.rating ?: 0) == 0){
                            isIconeSelecionadoErro = true

                        }else{
                            isIconeSelecionadoErro = false

                        }

                        if (!isDescricaoErro && !isIconeSelecionadoErro){
                            iconeSelecionado?.let {
                                darFeedBack(
                                    idDestino,
                                    nota = it.rating,
                                    descricaoFeedback,
                                    idOrigem,
                                    nomeUsuarioFeedback
                                )
                                Toast.makeText(context, mensagemFeedBackSucesso, Toast.LENGTH_SHORT).show()
                                navController.navigateUp()
                            }
                        }else{
                            Toast.makeText(context, mensagemFeedBackCamposNaoPreenchidos, Toast.LENGTH_SHORT).show()
                        }
                    },
                    //modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(id = R.string.botao_enviar))
                }
            }
        }


    }
}

enum class FeedbackIcon(val icon: ImageVector, val rating: Int) {
    WORST(Icons.Filled.SentimentVeryDissatisfied, 1),
    BAD(Icons.Filled.SentimentDissatisfied, 2),
    OK(Icons.Filled.SentimentSatisfied, 3),
    GOOD(Icons.Filled.SentimentSatisfiedAlt, 4),
    BEST(Icons.Filled.SentimentVerySatisfied, 5)
}
@Composable
fun EmotionIcons(selectedIcon: FeedbackIcon?, onIconSelected: (FeedbackIcon) -> Unit) {
    Row {
        for (icon in FeedbackIcon.entries) {
            val isSelected = icon == selectedIcon
            Icon(
                imageVector = icon.icon,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onIconSelected(icon) }
                    .size(48.dp)
                    .padding(4.dp)
                    .alpha(if (isSelected) 1f else 0.3f)
            )
        }
    }
}