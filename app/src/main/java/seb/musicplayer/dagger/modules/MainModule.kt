package seb.musicplayer.dagger.modules

import androidx.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import dagger.Module
import dagger.Provides
import seb.musicplayer.activities.MainActivity
import seb.musicplayer.dagger.MediaSessionConnection
import seb.musicplayer.viewmodels.MainActivityViewModel

@Module
class MainModule {

    private fun provideMediaSessionConnection(context: Context): MediaSessionConnection {
        return MediaSessionConnection.getInstance(context,
            ComponentName(context, MusicService::class.java)
        )
    }
    @Provides
    fun provideMainActivityViewModel(activity: MainActivity): MainActivityViewModel {
        val applicationContext = activity.applicationContext
        val mediaSessionConnection = provideMediaSessionConnection(applicationContext)
        return ViewModelProviders.of(activity,MainActivityViewModel.Factory(mediaSessionConnection,mediaId))
            .get(MainActivityViewModel::class.java)
    }
}