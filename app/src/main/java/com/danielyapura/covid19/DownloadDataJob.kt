package com.danielyapura.covid19

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.util.Log
import android.widget.RemoteViews
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private const val TAG_LOG = "DOWNLOAD_DATA_JOB"

class DownloadDataJob: JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG_LOG, "JOB SERVICE DETENIDO")
        return false //es true si se sigue ejecutando, llamar a jobFinished
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d(TAG_LOG, "JOB SERVICE INICIADO")
        val thread = Thread {
            descargarDatos(p0)
        }.start()
        return true
    }

    private fun descargarDatos(p0: JobParameters?) {
        Log.d(TAG_LOG, "DESCARGANDO DATOS")

        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DATE, -2) // recuperar datos de hace dos dias atras
        val dateForQuery = currentDate.formatearFechaToString("yyyy-MM-dd")

        val covidAPI: CovidAPI = RetrofitServiceGenerator.createService(CovidAPI::class.java)
        request(covidAPI.getAllData(dateForQuery, "Argentina"))
        jobFinished(p0, false)
    }


    private fun request(response: Call<List<Data>>){
        response.enqueue(object: Callback<List<Data>> {

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Log.d(TAG_LOG, "LA DESCARGA DE DATOS FALLO")
                actualizarCampo("No se pudo actualizar", R.id.fecha_txt)
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                Log.d(TAG_LOG, "DESCARGA OK")

                val deaths = response.body()?.last()?.deaths.toString()
                val recovered = response.body()?.last()?.recovered.toString()
                val confirmed = response.body()?.last()?.confirmed.toString()

                actualizarInterfaz(deaths, confirmed, recovered)
            }
        })
    }

    private fun actualizarCampo(dato: String, viewId: Int){
        val views = RemoteViews(packageName, R.layout.coronavirus_widget_layout)
        val theWidget = ComponentName(this, WidgetProvider::class.java)
        val widgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)

        views.setTextViewText(viewId, dato)
        widgetManager.updateAppWidget(theWidget, views)
    }

    private fun actualizarInterfaz(deaths: String, confirmed: String, recovered: String) {

        val dateUpdate = Calendar.getInstance().formatearFechaToString("dd/MM/yyyy - HH:mm")

        actualizarCampo(dateUpdate, R.id.fecha_txt)
        actualizarCampo(deaths, R.id.muertesTv)
        actualizarCampo(confirmed, R.id.confirmadosTv)
        actualizarCampo(recovered, R.id.recuperadosTv)
    }

    private fun Calendar.formatearFechaToString(formato: String) : String {
        val format = SimpleDateFormat(formato)
        return format.format(this.time)
    }
}