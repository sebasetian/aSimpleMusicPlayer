package seb.musicplayer.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import seb.musicplayer.activities.MainActivity
import seb.musicplayer.dagger.modules.MainModule

@Module
abstract class ActivityBuilder {
    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    abstract fun providesMainActivityInjector(): MainActivity
}