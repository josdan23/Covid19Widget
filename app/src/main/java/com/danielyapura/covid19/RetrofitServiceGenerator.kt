package com.danielyapura.covid19

import android.util.Log
import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceGenerator {

    companion object {
        val BASE_URL = "https://api.covid19api.com"

        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <T> createService (serviceClass: Class<T>) : T{
            Log.d("RETROFIT", "Objeto creado")
            return retrofit.create(serviceClass)
        }
    }

}
