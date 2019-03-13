package com.phrc.androidarchitecture.dagger

import com.phrc.androidarchitecture.ui.main.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}