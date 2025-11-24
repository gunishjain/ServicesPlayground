package com.gunishjain.servicesplayground

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gunishjain.servicesplayground.boundservice.MyService
import com.gunishjain.servicesplayground.boundservice.ServiceControlScreen
import com.gunishjain.servicesplayground.jobintentservice.JobIntentServiceComposable
import com.gunishjain.servicesplayground.jobintentservice.MyJIService
import com.gunishjain.servicesplayground.ui.theme.ServicesPlaygroundTheme

class MainActivity : ComponentActivity() {

    var counter = 0
    private var service: MyService? = null
    private var myJIService: MyJIService  = MyJIService()
    private var isBound = false
    var number by mutableStateOf<String?>(null)
        private set
    val connection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            service = (binder as MyService.MyServiceBinder).getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    private fun getRandomNumber()  {
        if (isBound) {
            val numberFromService = service?.getRandomNumber()
            number = numberFromService?.toString() ?: "Service not ready"
        } else {
            number = "Service not bound"
        }
    }
    private fun bindMyService(context: Context) {
        val serviceIntent = Intent(context, MyService::class.java)
        context.bindService(serviceIntent, connection, BIND_AUTO_CREATE)
    }

    private fun unbindMyService(context: Context) {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
            service = null
            number = "Service unbound"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ServicesPlaygroundTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = ServiceControl
                ) {
                    composable<ServiceControl> {
                        ServiceControlScreen(
                            number = number,
                            getRandomNumber = { getRandomNumber() },
                            onStartService = { startMyService(applicationContext) },
                            onStopService = { stopMyService(applicationContext) },
                            bindService = { bindMyService(applicationContext) },
                            unbindService = { unbindMyService(applicationContext) },
                            goToJobIntentService = {
                                navController.navigate(JobIntentScreen)
                            }
                        )
                    }

                    composable<JobIntentScreen> {
                        JobIntentServiceComposable(
                            onStartService = { startJIService(myJIService,applicationContext,++counter)},
                            onStopService = { stopJiService(applicationContext)},
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}


private fun startJIService(service: MyJIService, context: Context, counter: Int) {
    val JIServiceIntent: Intent? = Intent(context, MyJIService::class.java)
    JIServiceIntent?.putExtra("starter","starter $counter")
    service.enqueueWork(context, JIServiceIntent!!)
}


private fun stopJiService(context: Context) {
    val JIServiceIntent: Intent? = Intent(context, MyJIService::class.java)
    context.stopService(JIServiceIntent)
}

private fun startMyService(context: Context) {
    val serviceIntent = Intent(context, MyService::class.java)
    context.startService(serviceIntent)
}

private fun stopMyService(context: Context){
    context.stopService(Intent(context, MyService::class.java))
}




