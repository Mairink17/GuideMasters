package br.com.fiap.db1.guidemasters.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.fiap.db1.guidemasters.R

@Composable
fun CaixasDeSelecaoPorNome(
    opcoes: List<String>,
    opcoesSelecionadas: Set<String>,
    onSelecaoChanged: (String, Boolean) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = R.string.preferencias_mentoria),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(modifier = Modifier.padding(horizontal = 8.dp), thickness = 1.dp)
        opcoes.forEachIndexed { index, opcao ->
            CaixaDeSelecao(
                opcao = opcao,
                selecionadaInicialmente = opcoesSelecionadas.contains(opcao),
                onSelecaoChanged = { opcao, selecionada ->
                    onSelecaoChanged(opcao, selecionada)
                }
            )
            if (index < opcoes.size - 1) {
                Divider(modifier = Modifier.padding(horizontal = 8.dp), thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun CaixaDeSelecao(
    opcao: String,
    selecionadaInicialmente: Boolean,
    onSelecaoChanged: (String, Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Switch(
            checked = selecionadaInicialmente,
            onCheckedChange = { isChecked ->
                onSelecaoChanged(opcao, isChecked)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = opcao)
    }
}
