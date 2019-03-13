package com.phrc.androidarchitecture.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.phrc.androidarchitecture.sharedPreference.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Named("computation")
    fun provideComputationScheduler(): Scheduler {
        return Schedulers.computation()
    }

    @Provides
    @Named("io")
    fun provideIOScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    fun provideMode(): Int {
        return PRIVATE_MODE
    }

    @Provides
    fun providePreferencesName(): String {
        return PREFERENCES_NAME
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context, mode: Int, preferencesName: String): SharedPreferences {
        return context.getSharedPreferences(preferencesName, mode)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(sharedPreferences: SharedPreferences): SharedPreferencesManager {
        return SharedPreferencesManager(sharedPreferences)
    }

    companion object {
        const val PREFERENCES_NAME = "UserAuthSession"
        const  val PRIVATE_MODE = 0
    }

}
