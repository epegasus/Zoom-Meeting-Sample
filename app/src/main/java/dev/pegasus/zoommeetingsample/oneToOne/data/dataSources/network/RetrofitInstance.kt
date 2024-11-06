package dev.pegasus.zoommeetingsample.oneToOne.data.dataSources.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *   Developer: Sohaib Ahmed
 *   Date: 11/5/2024
 *   Profile:
 *     -> github.com/epegasus
 *     -> linkedin.com/in/epegasus
 */

object RetrofitInstance {

    val accessTokenApi: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://zoom.us/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    val zakApi: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.zoom.us/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}