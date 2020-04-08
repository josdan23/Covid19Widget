package com.danielyapura.covid19

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.danielyapura.covid19.ServicesDownloadData


/**
 * Implementation of App Widget functionality.
 */
class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        Toast.makeText(context, "Actualizando", Toast.LENGTH_SHORT).show()

        Log.d("TAG-CORONAVIRUS", "OnUpdate")

        val intentBoton = Intent(context, WidgetProvider::class.java)
        intentBoton.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intentBoton, PendingIntent.FLAG_UPDATE_CURRENT)

        val views = RemoteViews(
            context.packageName,
            R.layout.coronavirus_widget_layout
        ).apply {
            setOnClickPendingIntent(R.id.actualizar_bt, pendingIntent)
        }
        appWidgetManager.updateAppWidget(appWidgetIds, views)

        val intent = Intent(context, ServicesDownloadData::class.java)
        ServicesDownloadData.enqueueWork(context, intent)

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidget = ComponentName(
            context!!.packageName,
            WidgetProvider::class.java.getName()
        )
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
        Log.d("TAG-CORONAVIRUS", "ONRECEIVE")
        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        Log.d("TAG-CORONAVIRUS", "ENABLED")
        //super.onEnabled(context)
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        Log.d("TAG-CORONAVIRUS", "RESTORED")
        //super.onRestored(context, oldWidgetIds, newWidgetIds)
    }

    override fun onDisabled(context: Context?) {
        Log.d("TAG-CORONAVIRUS", "DISABLED")
        //super.onDisabled(context)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Log.d("TAG-CORONAVIRUS", "DELETE")
        //super.onDeleted(context, appWidgetIds)
    }
}

