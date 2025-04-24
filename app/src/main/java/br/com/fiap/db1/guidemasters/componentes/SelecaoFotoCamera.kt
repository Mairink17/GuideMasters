package br.com.fiap.db1.guidemasters.componentes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.ui.theme.DarkBlue


@Composable
fun CameraCaptureScreen(context: Context , onPhotoCaptured: (Bitmap) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val imageBitmap = result.data?.extras?.get("data") as? Bitmap
        if (imageBitmap != null) {
            onPhotoCaptured(imageBitmap)
        }
    }
    val mensagemPermissaoCamera = stringResource(id = R.string.permissao_camera)

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.launch(intent)
        } else {
            Toast.makeText(context , mensagemPermissaoCamera, Toast.LENGTH_SHORT).show()
        }
    }

    Button(modifier = Modifier.padding(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(DarkBlue),
        onClick = {

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                launcher.launch(intent)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

        }
    ) {
        Icon(
        painter = painterResource(id = R.drawable.photo_24),
        contentDescription = stringResource(id = R.string.camera_content)
    )
        Text(stringResource(id = R.string.botao_camera))
    }
}
