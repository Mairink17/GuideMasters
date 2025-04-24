package br.com.fiap.db1.guidemasters.componentes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue


@Composable
fun SelecaoFotoGaleria(context : Context, onFotoSelecionada: (Uri) -> Unit) {

    context.contentResolver

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onFotoSelecionada(uri)
        }
    }
    Button(
        modifier = Modifier.padding(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(DarkBlue),
        onClick = {

            launcher.launch("image/*")

        },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.galeria_24),
            contentDescription = stringResource(id = R.string.galeria_content)
        )
        Text(stringResource(id = R.string.botao_galeria))
    }

}

fun Activity.abrirGaleria() {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, SELECAO_FOTO_REQUEST_CODE)
}

fun Activity.obterUriSelecionada(data: Intent?): Uri? {
    return data?.data
}

const val SELECAO_FOTO_REQUEST_CODE = 123