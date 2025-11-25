package com.gunishjain.servicesplayground.boundservice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gunishjain.servicesplayground.ui.theme.ServicesPlaygroundTheme

@Composable
fun ServiceControlScreen(
    number: String?,
    getRandomNumber: () -> Unit,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    bindService: () -> Unit,
    unbindService: () -> Unit,
    goToJobIntentService: () -> Unit,
    goToJobScheduler: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = if(number!=null) "Number: $number" else "",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onStartService,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start Service")
        }


        Button(
            onClick = onStopService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Stop Service")
        }


        Button(
            onClick = bindService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Bind Service")
        }


        Button(
            onClick = unbindService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Unbind Service")
        }


        Button(
            onClick = getRandomNumber ,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Get Random Number")
        }

        Button(
            onClick = goToJobIntentService ,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Goto JobIntentService")
        }

        Button(
            onClick = goToJobScheduler ,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Goto JobScheduler")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun BindServicePreview() {
    ServicesPlaygroundTheme {
        ServiceControlScreen(
            number = "2",
            getRandomNumber = {},
            onStartService = {},
            onStopService = {},
            bindService = {},
            unbindService = {},
            goToJobIntentService = {},
            goToJobScheduler = {}
        )
    }
}