package com.danielyapura.covid19

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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

        actualizarIntefaz(context, appWidgetManager, appWidgetIds)

        configurarBotonActualizar(context, appWidgetManager, appWidgetIds)

        val intent = Intent(context, ServicesDownloadData::class.java)
        ServicesDownloadData.enqueueWork(context, intent)
    }

    private fun configurarBotonActualizar(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val intentBoton = Intent(context, WidgetProvider::class.java)
        intentBoton.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intentBoton, PendingIntent.FLAG_UPDATE_CURRENT)

        val views = RemoteViews(
            context.packageName,
            R.layout.coronavirus_widget_layout
        ).apply {
            setOnClickPendingIntent(R.id.actualizar_bt, pendingIntent)
        }

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun actualizarIntefaz(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val views = RemoteViews(context.packageName, R.layout.coronavirus_widget_layout)
        views.setTextViewText(R.id.confirmadosTv, "--")
        views.setTextViewText(R.id.muertesTv, "--")
        views.setTextViewText(R.id.fecha_txt, "Actualizando...")

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(
            context!!,
            WidgetProvider::class.java
        )

        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
        Log.d(TAG_LOG, "ONRECEIVE")
        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        Log.d(TAG_LOG, "OnENABLED")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        Log.d(TAG_LOG, "OnRESTORED")
    }

    override fun onDisabled(context: Context?) {
        Log.d(TAG_LOG, "OnDISABLED")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Log.d(TAG_LOG, "OnDELETE")
    }

}

