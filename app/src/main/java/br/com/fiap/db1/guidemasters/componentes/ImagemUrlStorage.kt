package br.com.fiap.db1.guidemasters.componentes

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import br.com.fiap.db1.guidemasters.R
import coil.compose.rememberImagePainter


@Composable
fun ImagemRemota(url: String) {
    val imageURL = url
    Image(
        rememberImagePainter(
            imageURL,
            builder = {
                placeholder(R.drawable.padrao)
            }
        ),
        contentDescription = null,
    )
}

