package com.gunishjain.servicesplayground

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gunishjain.servicesplayground.ui.theme.ServicesPlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ServicesPlaygroundTheme {
                    ServiceControlScreen(
                        onStartService = { startMyService(context = applicationContext) },
                        onStopService = { stopMyService(context = applicationContext)}
                    )
            }
        }
    }
}


private fun startMyService(context: Context) {
    val serviceIntent = Intent(context, MyService::class.java)
    context.startService(serviceIntent)
}

private fun stopMyService(context: Context){
    context.stopService(Intent(context, MyService::class.java))
}


@Composable
fun ServiceControlScreen(
    onStartService: () -> Unit,
    onStopService: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onStartService,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start Service")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStopService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Stop Service")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ServicesPlaygroundTheme {
    }
}