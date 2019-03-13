package com.phrc.androidarchitecture.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

class ApiRetrofitClient {

    private val mapHeaders: HashMap<String, String> = HashMap()

    @Throws(IOException::class)
    fun addHeaders(chain: Interceptor.Chain): Response {
        Log.e("Thread - API", "Header interceptor" + Thread.currentThread().threadGroup.name)
        val builder = chain
                .request()
                .newBuilder()

        //        if(mapHeaders != null) {
        for (key in mapHeaders.keys) {
            builder.addHeader(key, mapHeaders[key]!!)
        }
        //        }
        return chain.proceed(builder.build())
    }

    fun addHeader(key: String?, value: String?) {
        if (key == null || value == null)
            return
        //        if(mapHeaders == null) {
        //            mapHeaders = new HashMap<>();
        //        }
        mapHeaders[key] = value
    }

    fun removeHeader(key: String?) {
        if (key == null)
            return
        //        if (mapHeaders != null) {
        if (mapHeaders.containsKey(key)) {
            mapHeaders.remove(key)
        }
        //        }
    }

    fun getHeaderValue(key: String): String? {
        return if (mapHeaders.containsKey(key)) {
            mapHeaders[key]
        } else null
    }

    companion object {
        val TOKEN = "Authorization"
    }
}
