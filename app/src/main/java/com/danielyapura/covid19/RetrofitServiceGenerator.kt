package com.danielyapura.covid19

import android.util.Log
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceGenerator {

    companion object {
        val BASE_URL = "https://api.covid19api.com"
        val URL_API = "https://api.covid19data.cloud"

        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <T> createService (serviceClass: Class<T>) : T{
            Log.d("RETROFIT", "Objeto creado")
            return retrofit.create(serviceClass)
        }
    }

}
