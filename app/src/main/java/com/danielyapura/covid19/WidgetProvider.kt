package com.danielyapura.covid19

import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi

/**
 * Implementation of App Widget functionality.
 */

private const val TAG_LOG = "TAG-CORONAVIRUS"

class WidgetProvider : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG_LOG, "OnUPDATE")

        resetInterfaz(context, appWidgetManager, appWidgetIds)

        configurarBotonActualizar(context, appWidgetManager, appWidgetIds)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG_LOG, "DENTRO DEL IF")
            val powerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
                val intent = Intent();
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = (Uri.parse("package:" + context.packageName))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }

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
        views.apply {
            setTextViewText(R.id.fecha_txt, "Actualizando...")
        }

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun obtenerDatos(context: Context){
        Log.d(TAG_LOG, "OBTENIENDO LOS DATOS")

        //LLAMAR AL SERVICIO
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(context, DownloadDataJob::class.java)
        val jobInfo: JobInfo.Builder = JobInfo.Builder(1, componentName)

        jobInfo.setOverrideDeadline(0)
        jobScheduler.schedule(jobInfo.build())
    }

}

