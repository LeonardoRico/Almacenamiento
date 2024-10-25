package com.example.almacenamiento

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.almacenamiento.ui.theme.AlmacenamientoTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlmacenamientoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        guardarDatosEnSharedPreferences(this)
        verificarDatosEnSharedPreferences(this)
    }
}

fun guardarDatosEnSharedPreferences(context: Context){
    // Accede al archivo en la carpeta assets
    try{
        //Abre el archivo y el método devuelve un inputSream, que es una secuencia de datos del archivo
        val inputStream = context.assets.open("co2_data.txt")
        //El bufferReader permite leer un archivo linea por linea y el Inputstreamreader
        // convierte el flujo de bytes (InputStream) en un flujo de caracteres.
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Obtener las SharedPreferences
        //se instancia SharedPreferences con el nombre co2_data
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("CO2_DATA", Context.MODE_PRIVATE)
        //se crea un editor para modificar los valores de las preferencias
        val editor = sharedPreferences.edit()

        //se declara una variable, puede ser null o contener una cadena de texto.
        var line: String?
        //contador
        var index = 1

        //lee el archivo linea por linea y le asigna a line cada línea usando el bloque also hasta que sea null
        while (reader.readLine().also { line = it } != null) {
            //separa la línea en dos partes
            //En Kotlin, los dobles signos de exclamación !! se utilizan para realizar una
            // asserción nula. Esto significa que estás afirmando de manera explícita que el
            // valor al que se aplica no será nulo. En otras palabras, estás diciendo al compilador:
            // "Confío en que este valor nunca será nulo, así que si lo es, por favor,
            // genera una excepción".
            val parts = line!!.split(",")
            val date = parts[0]
            //se convierte a Float
            val co2Value = parts[1].toFloat()

            // Guardar los datos en SharedPreferences
            editor.putString("date_$index", date)
            editor.putFloat("co2_$index", co2Value)
            index++
        }

        // Aplicar los cambios en SharedPreferences
        editor.apply()

        // Cerrar el BufferedReader
        reader.close()
    }catch (e: Exception) {
        e.printStackTrace()
    }
}

fun verificarDatosEnSharedPreferences(context: Context) {
    // Acceder a las SharedPreferences
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("CO2_DATA", Context.MODE_PRIVATE)

    //  Se recorre el sharedpreferences
    for (index in 1..31) {
        // Leer cada valor de SharedPreferences
        val fecha = sharedPreferences.getString("date_$index", "N/A")
        val co2 = sharedPreferences.getFloat("co2_$index", 0.0f)

        // Mostrar los datos en Logcat
        Log.d("VerificacionSharedPref", "Fecha: $fecha, CO2: $co2")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlmacenamientoTheme {
        Greeting("Android")
    }
}