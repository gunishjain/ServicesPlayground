package com.gunishjain.servicesplayground

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    private var mRandomNumber: Int? = null

    private var isRandomNumberGeneratorOn: Boolean = false

    private val MIN = 0
    private val MAX = 100

    private fun generateRandomNumber()  {
        while (isRandomNumberGeneratorOn){
            try {
                Thread.sleep(1000)
                mRandomNumber = MIN + (Math.random() * ((MAX - MIN) + 1)).toInt()
                Log.d("MyService", "Random Number: $mRandomNumber")
            } catch (e: InterruptedException) {
                Log.i("MyService", "Thread Interrupted")
            }
        }
    }

    private fun stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false
    }

     fun getRandomNumber() = mRandomNumber

    private val binder = MyServiceBinder()

    // using inner class since it can access members of its outer class and its instance including private ones
    inner class MyServiceBinder: Binder() {

        fun getService()  =  this@MyService

    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d("MyService", "In onBind")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        isRandomNumberGeneratorOn = true

        serviceScope.launch {
            Log.d("MyService", "Service started on Thread: ${Thread.currentThread().name}")
            generateRandomNumber()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        serviceJob.cancel() // comment it out just to test for memory leaks
        Log.d("MyService", "Service destroyed")
    }
}