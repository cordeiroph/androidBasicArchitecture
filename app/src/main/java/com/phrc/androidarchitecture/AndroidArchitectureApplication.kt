package com.phrc.androidarchitecture

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.phrc.androidarchitecture.dagger.AppComponent
import com.phrc.androidarchitecture.dagger.AppModule
import com.phrc.androidarchitecture.dagger.DaggerAppComponent
import com.phrc.androidarchitecture.dagger.Injectable
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AndroidArchitectureApplication : Application(), HasActivityInjector{

    lateinit var appComponent: AppComponent
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return this.dispatchingAndroidInjector
    }

    private fun initDagger(application: AndroidArchitectureApplication): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
        appComponent.inject(this)
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is Injectable) {
                    AndroidInjection.inject(activity)
                }
                if (activity is FragmentActivity) {
                    activity
                            .supportFragmentManager
                            .registerFragmentLifecycleCallbacks(
                                    object : FragmentManager.FragmentLifecycleCallbacks() {
                                        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
                                            super.onFragmentPreAttached(fm, f, context)
                                            if (f is Injectable) {
                                                AndroidSupportInjection.inject(f)
                                            }
                                        }

                                        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {

                                        }

                                    }, true)
                }
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })

    }

}
