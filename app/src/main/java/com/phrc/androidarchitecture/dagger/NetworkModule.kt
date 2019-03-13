package com.phrc.androidarchitecture.dagger

import com.phrc.androidarchitecture.api.ApiClientConstant
import com.phrc.androidarchitecture.api.ApiRetrofitClient
import com.phrc.androidarchitecture.api.requester.RepositoryRequester
import com.phrc.androidarchitecture.sharedPreference.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Named(ApiClientConstant.BASE_URL)
    fun provideBaseUrlString(): String {
        return ApiClientConstant.BASE_URL
    }

    @Provides
    @Singleton
    fun provideRxJava2CallAdapter(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideGsonConverter(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideApiRetrofitClient(): ApiRetrofitClient {
        return ApiRetrofitClient()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferencesManager: SharedPreferencesManager, apiRetrofitClient: ApiRetrofitClient): OkHttpClient {
        return ApiClientConstant.initOkHttpClient(sharedPreferencesManager, apiRetrofitClient)
    }

    @Provides
    @Singleton
    fun provideRetrofit(callAdapter: CallAdapter.Factory,
                                 converter: Converter.Factory,
                                 @Named(ApiClientConstant.BASE_URL) baseUrl: String,
                                 httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(callAdapter)
                .addConverterFactory(converter)
                .client(httpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideRepositoryRequester(retrofit: Retrofit): RepositoryRequester {
        return retrofit.create(RepositoryRequester::class.java)
    }
}
