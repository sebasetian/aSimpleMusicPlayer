package seb.musicplayer

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import seb.musicplayer.dagger.components.DaggerApplicationComponent
import javax.inject.Inject

class MyApp: Application(), HasActivityInjector {
    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.create().inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }
}