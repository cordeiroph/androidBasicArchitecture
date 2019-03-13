package com.phrc.androidarchitecture.dagger

import com.phrc.androidarchitecture.ui.main.MainViewModel
import dagger.Component

@Component( modules = arrayOf(
        ViewModelModule::class
))
interface ViewModelComponent {

    fun inject( mainViewModel: MainViewModel)

}