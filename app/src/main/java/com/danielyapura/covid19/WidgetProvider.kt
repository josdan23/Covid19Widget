package com.danielyapura.covid19

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */

private const val TAG_LOG = "TAG-CORONAVIRUS"

class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG_LOG, "OnUPDATE")

        resetInterfaz(context, appWidgetManager, appWidgetIds)

        configurarBotonActualizar(context, appWidgetManager, appWidgetIds)

        obtenerDatos(context)

    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG_LOG, "DELETED")
    }

    private fun configurarBotonActualizar(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val intentBoton = Intent(context, WidgetProvider::class.java)
        intentBoton.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intentBoton.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intentBoton, PendingIntent.FLAG_CANCEL_CURRENT)

        val views = RemoteViews(
            context.packageName,
            R.layout.coronavirus_widget_layout
        ).apply {
            setOnClickPendingIntent(R.id.actualizar_bt, pendingIntent)
        }

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun resetInterfaz(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val views = RemoteViews(context.packageName, R.layout.coronavirus_widget_layout)
        views.setTextViewText(R.id.confirmadosTv, "--")
        views.setTextViewText(R.id.muertesTv, "--")
        views.setTextViewText(R.id.recuperadosTv, "--")
        views.setTextViewText(R.id.fecha_txt, "Actualizando...")

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun obtenerDatos(context: Context){
        Log.d(TAG_LOG, "OBTENIENDO LOS DATOS")

        //LLAMAR AL SERVICIO
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(context, DownloadDataJob::class.java)
        val jobInfo: JobInfo.Builder = JobInfo.Builder(1, componentName)


        val periodicMillis: Long = 15 * 60 * 1000
        jobInfo.setPeriodic(periodicMillis)

        jobScheduler.schedule(jobInfo.build())
    }

    private fun obtenerDatosAlarma(context: Context){

        Log.d(TAG_LOG, "OBTENIENDO LOS DATOS")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DownloadDataIntentService::class.java)
        val pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(),20 * 60 * 1000 , pendingIntent)
    }}

