package com.danielyapura.covid19

import retrofit2.Call
import retrofit2.http.GET

interface CovidAPI {

    @GET("/country/argentina/status/confirmed/live")
    fun getConfirmed(): Call<List<Data>>

    @GET("/country/argentina/status/deaths/live")
    fun getDeaths(): Call<List<Data>>
}