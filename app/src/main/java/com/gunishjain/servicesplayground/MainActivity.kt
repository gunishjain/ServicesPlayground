package com.gunishjain.servicesplayground

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.gunishjain.servicesplayground.boundservice.MyService
import com.gunishjain.servicesplayground.boundservice.ServiceControlScreen
import com.gunishjain.servicesplayground.foregroundService.ForegroundService
import com.gunishjain.servicesplayground.foregroundService.ForegroundServiceComposable
import com.gunishjain.servicesplayground.jobintentservice.JobIntentServiceComposable
import com.gunishjain.servicesplayground.jobintentservice.MyJIService
import com.gunishjain.servicesplayground.jobservice.JobSchedulerComposable
import com.gunishjain.servicesplayground.jobservice.JobServiceDemo
import com.gunishjain.servicesplayground.ui.theme.ServicesPlaygroundTheme
import com.gunishjain.servicesplayground.workmanagerdemo.RandomNumberGeneratorWorker
import com.gunishjain.servicesplayground.workmanagerdemo.WorkManagerComposable
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {

    var counter = 0
    private var service: MyService? = null
    private var myJIService: MyJIService  = MyJIService()

    private var jobScheduler: JobScheduler? = null
    private var isBound = false

    private lateinit var workManager: WorkManager
    private lateinit var workRequest: WorkRequest

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


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            101
        )

        enableEdgeToEdge()
        setContent {
            ServicesPlaygroundTheme {
                val navController = rememberNavController()

                jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

                workManager = WorkManager.getInstance(this)
                workRequest = PeriodicWorkRequest.Builder(
                    RandomNumberGeneratorWorker::class.java,
                    15,
                    java.util.concurrent.TimeUnit.MINUTES
                ).build()

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
                            },
                            goToJobScheduler = {
                                navController.navigate(JobSchedulerScreen)
                            },
                            goToForegroundService = {
                                navController.navigate(ForegroundServiceScreen)
                            },
                            goToWorkManager = {
                                navController.navigate(WorkManagerScreen)
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

                    composable<JobSchedulerScreen> {
                        JobSchedulerComposable(
                            onStartService = { startJob(applicationContext, jobScheduler!! )},
                            onStopService = { stopJob(jobScheduler!!)},
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable<ForegroundServiceScreen> {
                        ForegroundServiceComposable(
                            onStartService = { startForeGroundService(applicationContext)},
                            onStopService = { stopForegroundService(applicationContext)},
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable<WorkManagerScreen> {
                        WorkManagerComposable(
                            onStartWorker = { startWorker(workManager,workRequest)},
                            onStopWorker = { stopWorker(workManager,workRequest)},
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

private fun startWorker(workManager: WorkManager,workRequest: WorkRequest){
    workManager.enqueue(workRequest)
}

private fun stopWorker(workManager: WorkManager,workRequest: WorkRequest){
    workManager.cancelWorkById(workRequest.id)
}


private fun startForeGroundService(context: Context) {
    val serviceIntent = Intent(context, ForegroundService::class.java)
    ContextCompat.startForegroundService(context, serviceIntent)
}

private fun stopForegroundService(context: Context) {
    val serviceIntent: Intent? = Intent(context, ForegroundService::class.java)
    context.stopService(serviceIntent)
}


@RequiresApi(Build.VERSION_CODES.P)
private fun startJob(context: Context, scheduler: JobScheduler) {

    val componentName = ComponentName(context, JobServiceDemo::class.java)

    val jobInfo : JobInfo = JobInfo.Builder(101, componentName)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .setPeriodic(15*60*1000)
        .setRequiresCharging(false)
        .setPersisted(true)
        .build()

    if(scheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS) {
        Log.i("JobScheduler", "Job scheduled successfully")
    } else {
        Log.i("JobScheduler", "Job scheduling failed")
    }

}

private fun stopJob(scheduler: JobScheduler) {
    scheduler.cancel(101)
    Log.i("JobScheduler", "Job cancelled")
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




