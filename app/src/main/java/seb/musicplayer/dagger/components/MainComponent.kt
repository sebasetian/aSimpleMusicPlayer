package seb.musicplayer.dagger.components

import dagger.Component
import seb.musicplayer.activities.MainActivity
import seb.musicplayer.dagger.modules.MainModule


@Component(modules=[MainModule::class])
interface MainComponent {
    //fun inject(app: MainActivity)
}