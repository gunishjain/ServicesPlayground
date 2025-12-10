package com.gunishjain.servicesplayground.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class RandomNumberGeneratorWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    private var mRandomNumber: Int? = null

    private var isRandomNumberGeneratorOn: Boolean = false

    private val MIN = 0
    private val MAX = 100

    private fun generateRandomNumber()  {
        while (isRandomNumberGeneratorOn && !isStopped){
            try {
                Thread.sleep(1000)
                mRandomNumber = MIN + (Math.random() * ((MAX - MIN) + 1)).toInt()
                Log.d("Worker", "Random Number: $mRandomNumber")
            } catch (e: InterruptedException) {
                Log.i("Worker", "Thread Interrupted")
            }
        }
    }

    override fun doWork(): Result {
        isRandomNumberGeneratorOn = true
        generateRandomNumber()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        isRandomNumberGeneratorOn = false
        Log.i("Worker", "Worker Stopped")

    }

}