package com.danielyapura.covid19

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import android.widget.Toast


class DownloadDataJob: JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {

        Log.d("JOB-SERVICE", "TERMINADO")
        return false //es true si se sigue ejecutando, llamar a jobFinished
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("JOB-SERVICE", "INICIADO")

        ejecutarTarea()
        return true
    }

    private fun ejecutarTarea() {
        Log.d("JOB-SERVICES", "EJECUTAR TAREA")
        Toast.makeText(applicationContext, "Actualizando", Toast.LENGTH_SHORT).show()

        val intent = Intent(applicationContext, DownloadDataJobIntentService::class.java)
        //startService(intent)
        DownloadDataJobIntentService.enqueueWork(applicationContext, intent)
    }

}