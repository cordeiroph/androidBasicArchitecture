package com.phrc.androidarchitecture.dagger

import com.phrc.androidarchitecture.AndroidArchitectureApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    ActivitiesModule::class,
    AppModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    ViewModelModule::class])
interface AppComponent {

    val retrofit: Retrofit

    fun inject(androidArchitectureApplication: AndroidArchitectureApplication)

}