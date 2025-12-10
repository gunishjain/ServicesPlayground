package com.gunishjain.servicesplayground.jobservice

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class JobServiceDemo: JobService() {

    private var jobParameters: JobParameters? = null
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    private var mRandomNumber: Int? = null

    private var isRandomNumberGeneratorOn: Boolean = false

    private val MIN = 0
    private val MAX = 100


    @RequiresApi(Build.VERSION_CODES.S)
    private fun generateRandomNumber(starIdentifier: String?)  {
        var count = 0
        while (count<5) {
            try {
                Thread.sleep(1000)
                mRandomNumber = MIN + (Math.random() * ((MAX - MIN) + 1)).toInt()
                Log.d("MyJobService", "Random Number: $mRandomNumber : $starIdentifier")
            } catch (e: InterruptedException) {
                Log.i("MyJobService", "Thread Interrupted")
            }
            count++
        }

        jobFinished(jobParameters,true)


    }

    private fun stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun doBackGroundWork() {
        serviceScope.launch {
            generateRandomNumber("JobServiceStarter")
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("MyJobService", "onStartJob")
        jobParameters = p0
        doBackGroundWork()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("MyJobService", "onStopJob")
        return true //meaning it will get rescheduled if killed
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        Log.d("MyJobService", "Service Stopped on Thread: ${Thread.currentThread().name}")

    }

}