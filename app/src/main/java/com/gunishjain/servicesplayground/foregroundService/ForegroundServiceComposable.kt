package com.gunishjain.servicesplayground.foregroundService

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gunishjain.servicesplayground.ForegroundServiceScreen
import com.gunishjain.servicesplayground.ui.theme.ServicesPlaygroundTheme

@Composable
fun ForegroundServiceComposable(
    onStartService: () -> Unit,
    onStopService: () -> Unit,
    onBack: () -> Unit
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
            Text(text = "Start Foreground Service")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStopService,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Stop Job")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Back")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ServicesPlaygroundTheme {
        ForegroundServiceComposable(
            onStartService = {},
            onStopService = {},
            onBack = {}
        )
    }
}