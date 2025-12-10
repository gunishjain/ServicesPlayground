package com.gunishjain.servicesplayground.foregroundService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ForegroundService  : Service(){

    private var mRandomNumber: Int? = null

    private var isRandomNumberGeneratorOn: Boolean = false

    private lateinit var notificationHelper: NotificationHelper


    private val MIN = 0
    private val MAX = 100

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        Log.d("ForegroundService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(1,notificationHelper.getNotification())
        isRandomNumberGeneratorOn = true

        serviceScope.launch {
            Log.d("ForegroundService", "Service started on Thread: ${Thread.currentThread().name}")
            generateRandomNumber()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        serviceJob.cancel() // comment it out just to test for memory leaks
        Log.d("ForegroundService", "Service destroyed")
    }

    private fun generateRandomNumber()  {
        while (isRandomNumberGeneratorOn){
            try {
                Thread.sleep(1000)
                mRandomNumber = MIN + (Math.random() * ((MAX - MIN) + 1)).toInt()
                Log.d("ForegroundService", "Random Number: $mRandomNumber")
            } catch (e: InterruptedException) {
                Log.i("ForegroundService", "Thread Interrupted")
            }
        }
    }

    private fun stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false
    }



}