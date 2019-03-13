package com.phrc.androidarchitecture.api

import com.phrc.androidarchitecture.sharedPreference.SharedPreferencesManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*

object ApiClientConstant {

    const val BASE_URL = "https://api.github.com/"

    fun initOkHttpClient(sharedPreferencesManager: SharedPreferencesManager, apiRetrofitClient: ApiRetrofitClient): OkHttpClient {
        val token = sharedPreferencesManager.token
        if (token.isNotBlank()) {
            apiRetrofitClient.addHeader(ApiRetrofitClient.TOKEN, token)
        }

        apiRetrofitClient.addHeader("timeZone", "" + TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000)
        apiRetrofitClient.addHeader("language", Locale.getDefault().toString())

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient()
                .newBuilder()
                .addInterceptor { apiRetrofitClient.addHeaders(it) }
                .addInterceptor(httpLoggingInterceptor)
                .build()

    }
}