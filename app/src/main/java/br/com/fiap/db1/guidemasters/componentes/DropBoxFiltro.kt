package br.com.fiap.db1.guidemasters.componentes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.db1.guidemasters.R

@Composable
fun DropBoxFiltro(
    grausInstrucao: List<String>,
    selectedGrauInstrucao: String,
    onGrauInstrucaoSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(stringResource(id = R.string.filtro_skill))
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGrauInstrucao,
                onValueChange = { },
                readOnly = true,
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown
                            , contentDescription = stringResource(id = R.string.dropbox_content))
                    }
                }, textStyle = TextStyle(fontSize = 14.sp)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },

            ) {
                DropdownMenuItem(text = { Text(stringResource(id = R.string.limpar_filtro)) }, onClick = {
                    onGrauInstrucaoSelected("")
                    expanded = false
                })

                grausInstrucao.forEach { grau ->
                    if (!grau.isNullOrBlank()) {
                        DropdownMenuItem(text = { Text(grau) }, onClick = {
                            onGrauInstrucaoSelected(grau)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}
