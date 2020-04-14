package com.danielyapura.covid19

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DownloadDataIntentService : IntentService("Mi servicio") {
    override fun onHandleIntent(p0: Intent?) {
        Log.d("INTENT_SERVICE", "INTENTE SERVICE EJECUTADO")

        val views = RemoteViews(packageName, R.layout.coronavirus_widget_layout)
        val theWidget = ComponentName(this, WidgetProvider::class.java)
        val widgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)

        //mostrar fecha de actualización
        val currentDateTime = Calendar.getInstance()
        val format1 = SimpleDateFormat("dd/MM/yyyy - HH:mm")
        val formatted = format1.format(currentDateTime.time)
        views.setTextViewText(R.id.fecha_txt, formatted)

        val covidAPI: CovidAPI = RetrofitServiceGenerator.createService(CovidAPI::class.java)

        // OBTENER DATOS DE CONFIRMADOS
        val resultConfirmados: Call<List<Data>> = covidAPI.getConfirmed()

        resultConfirmados.enqueue(object: Callback<List<Data>>{
            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                t.printStackTrace()
                Log.d("DATOS-ERROR", "CONFIRMADOS")
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                Log.d("DATOS-OK", "OK-CONFIRMADOS")
                val dataConfirmados: Data? = response.body()?.last()
                views.setTextViewText(R.id.confirmadosTv, dataConfirmados?.cases.toString())
                widgetManager.updateAppWidget(theWidget, views)
            }
        })

        //OBTENER LOS MUERTOS
        covidAPI.getDeaths().enqueue(object: Callback<List<Data>>{
            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.d("DATOS-ERROR", "CONFIRMADOS")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {

                Log.d("DATOS-OK", "OK-MUERTES")
                val dataMuertos: Data? = response.body()?.last()
                views.setTextViewText(R.id.muertesTv, dataMuertos?.cases.toString())
                widgetManager.updateAppWidget(theWidget, views)
                Log.d("DATOS-OK", "MUERTES")
            }
        })

        //OBTENER LOS RECUPERADOS
        covidAPI.getRecovered().enqueue(object: Callback<List<Data>>{
            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.d("DATOS-ERROR", "CONFIRMADOS")
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {

                Log.d("DATOS-OK", "OK-MUERTES")
                val dataRecuperados: Data? = response.body()?.last()
                views.setTextViewText(R.id.recuperadosTv, dataRecuperados?.cases.toString())
                widgetManager.updateAppWidget(theWidget, views)
                Log.d("DATOS-OK", "MUERTES")
            }
        })

        widgetManager.updateAppWidget(theWidget, views)
    }
}