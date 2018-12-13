package seb.musicplayer.dagger.components

import android.app.Application
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import seb.musicplayer.MyApp
import seb.musicplayer.dagger.ActivityBuilder
import seb.musicplayer.dagger.modules.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityBuilder::class, ApplicationModule::class])
interface ApplicationComponent: AndroidInjector<Application> {
    fun inject(app: MyApp)
}