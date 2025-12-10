package com.gunishjain.servicesplayground.workmanagerdemo

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
import com.gunishjain.servicesplayground.ui.theme.ServicesPlaygroundTheme

@Composable
fun WorkManagerComposable(
    onStartWorker: () -> Unit,
    onStopWorker: () -> Unit,
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
            onClick = onStartWorker,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start Worker")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStopWorker,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Stop Worker")
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
        WorkManagerComposable(
            onStartWorker = {},
            onStopWorker = {},
            onBack = {}
        )
    }
}