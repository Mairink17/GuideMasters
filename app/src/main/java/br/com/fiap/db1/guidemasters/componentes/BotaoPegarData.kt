package br.com.fiap.db1.guidemasters.componentes

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.io.Serializable
import java.util.Calendar
import br.com.fiap.db1.guidemasters.R


@SuppressLint("SuspiciousIndentation")
@Composable
fun BotaoPegarData(textoBotao: String, dataConsulta: Serializable, corBotao : Color, tamanho : Int) : String{


    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("") }

    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "${selectedDayOfMonth.toString().padStart(2, '0')}/${
                (selectedMonth + 1).toString()
                    .padStart(2, '0')}/$selectedYear"
        }, year, month, dayOfMonth
    )

    datePicker.datePicker.maxDate = calendar.timeInMillis

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(corBotao),
            modifier = Modifier.fillMaxWidth().height(tamanho.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                datePicker.show()


            }
        ) {
            Text(text = if (selectedDateText.isNotEmpty() ) {
                "$selectedDateText"

            } else {
                if (textoBotao.isNotEmpty()){
                    textoBotao
                }else{
                    stringResource(id = R.string.pegar_data)

                }
            }
            )
        }
    }
    return selectedDateText
}