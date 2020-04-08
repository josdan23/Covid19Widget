package com.danielyapura.covid19

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.danielyapura.covid19.R
import com.danielyapura.covid19.RetrofitServiceGenerator
import com.danielyapura.covid19.WidgetProvider
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class ServicesDownloadData: JobIntentService() {

    companion object {

        val JOB_ID: Int = 1

        public fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ServicesDownloadData::class.java, JOB_ID, intent)
        }
    }


    override fun onHandleWork(intent: Intent) {
        Log.d("SERVICIO", "Servicio iniciado")

        val views: RemoteViews = RemoteViews(packageName, R.layout.coronavirus_widget_layout)
        val theWidget: ComponentName = ComponentName(this, WidgetProvider::class.java)
        val widgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)

        //mostrar fecha de actualización
        val currentDateTime = Calendar.getInstance()
        val format1 = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        val formatted = format1.format(currentDateTime.time)
        views.setTextViewText(R.id.ultimo_registro, formatted)

        val covidAPI: CovidAPI = RetrofitServiceGenerator.createService(CovidAPI::class.java)

        // OBTENER DATOS DE CONFIRMADOS
        val resultConfirmados: Call<List<Data>> = covidAPI.getConfirmed()

        try {
            val response: Response<List<Data>> = resultConfirmados.execute()
            val dataConfirmados: Data? = response.body()?.last()
            views.setTextViewText(R.id.confirmadosTv, dataConfirmados?.cases.toString())
            widgetManager.updateAppWidget(theWidget, views)
            Log.d("DATOS-OK", "CONFIRMADOS")
        } catch (ex: Exception){
            Log.d("DATOS-ERROR", "CONFIRMADOS")
            ex.printStackTrace()
        }

        //OBTENER DATOS DE MUERTES
        val resultMuertos: Call<List<Data>> = covidAPI.getDeaths()

        try {
            val responseMuertos: Response<List<Data>> = resultMuertos.execute()
            val dataMuertos: Data? = responseMuertos.body()?.last()
            views.setTextViewText(R.id.muertesTv, dataMuertos?.cases.toString())
            widgetManager.updateAppWidget(theWidget, views)
            Log.d("DATOS-OK", "MUERTES")

        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("DATOS-ERROR", "MUERTES")
        }

        widgetManager.updateAppWidget(theWidget, views)
        //this.stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SERVICIO", "Destruido")
    }
}