package com.gunishjain.servicesplayground.jobintentservice

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJIService : JobIntentService() {

    private var mRandomNumber: Int? = null

    private var isRandomNumberGeneratorOn: Boolean = false

    private val MIN = 0
    private val MAX = 100

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, MyJIService::class.java, 1, work)
    }
    private fun generateRandomNumber(starIdentifier: String?)  {

        for (i in 1..5) {

                try {
                    Thread.sleep(1000)
                    mRandomNumber = MIN + (Math.random() * ((MAX - MIN) + 1)).toInt()
                    Log.d("MyJIService", "Random Number: $mRandomNumber : $starIdentifier")
                } catch (e: InterruptedException) {
                    Log.i("MyJIService", "Thread Interrupted")
                }
        }
        stopSelf()
    }

    private fun stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false
    }

    fun getRandomNumber() = mRandomNumber


    override fun onHandleWork(intent: Intent) {
            isRandomNumberGeneratorOn = true

            Log.d("MyJIService", "Service started on Thread: ${Thread.currentThread().name}")
            generateRandomNumber(intent.getStringExtra("starter"))
        // No need to launch in coroutine since it already run on worker thread
        // if we use coroutine it will cause memory leak- Do POC for leak canary.

    }

    override fun onStopCurrentWork(): Boolean {
        Log.d("MyJIService", "onStopCurrentWork on Thread: ${Thread.currentThread().name}")
        return super.onStopCurrentWork()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomNumberGenerator()
        Log.d("MyJIService", "Service Stopped on Thread: ${Thread.currentThread().name}")

    }
}