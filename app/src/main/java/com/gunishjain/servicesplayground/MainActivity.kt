package com.gunishjain.servicesplayground

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
    private var service: MyService? = null
    private var isBound = false
    private var number: Int? = null
    val connection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            service = (binder as MyService.MyServiceBinder).getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = true
        }

    }

    private fun getRandomNumber() : Int? {
        return service?.getRandomNumber()
    }
    private fun bindMyService(context: Context) {
        val serviceIntent = Intent(context, MyService::class.java)
        context.bindService(serviceIntent, connection, BIND_AUTO_CREATE)
    }

    private fun unbindMyService(context: Context) {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ServicesPlaygroundTheme {
                    ServiceControlScreen(
                        getRandomNumber = { getRandomNumber() },
                        number = number ,
                        onStartService = { startMyService(context = applicationContext) },
                        onStopService = { stopMyService(context = applicationContext)},
                        bindService = { bindMyService(context = applicationContext) },
                        unbindService = { unbindMyService(context = applicationContext)}
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
    number: Int?,
    getRandomNumber: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    bindService: () -> Unit,
    unbindService: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = if(number!=null) "Number: $number" else "Service not bound",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = bindService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Bind Service")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = unbindService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Unbind Service")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = getRandomNumber ,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Get Random Number")
        }

    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ServicesPlaygroundTheme {
    }
}