package com.danielyapura.covid19

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidAPI {

    @GET("/country/argentina/status/confirmed/live")
    fun getConfirmed(): Call<List<Data>>

    @GET("/country/argentina/status/deaths/live")
    fun getDeaths(): Call<List<Data>>

    @GET("/country/argentina/status/recovered/live")
    fun getRecovered(): Call<List<Data>>

    @GET("/v1/jh/daily-reports/")
    fun getAllData(@Query("last_update_from") date: String, @Query("country") country: String): Call<List<Data>>


}