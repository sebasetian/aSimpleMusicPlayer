package seb.musicplayer.viewmodels

import android.arch.lifecycle.*
import android.util.Log
import seb.musicplayer.dagger.MediaSessionConnection
import seb.musicplayer.model.Event
import seb.musicplayer.model.MediaItemData
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val mediaSessionConnection: MediaSessionConnection
) : ViewModel() {

    val rootMediaId: LiveData<String> =
        Transformations.map(mediaSessionConnection.isConnected) { isConnected ->
            if (isConnected) {
                mediaSessionConnection.rootMediaId
            } else {
                null
            }
        }
    /**
     * [navigateToMediaItem] acts as an "event", rather than state. [Observer]s
     * are notified of the change as usual with [LiveData], but only one [Observer]
     * will actually read the data. For more information, check the [Event] class.
     */
    val navigateToMediaItem: LiveData<Event<String>> get() = _navigateToMediaItem
    private val _navigateToMediaItem = MutableLiveData<Event<String>>()

    /**
     * This method takes a [MediaItemData] and routes it depending on whether it's
     * browsable (i.e.: it's the parent media item of a set of other media items,
     * such as an album), or not.
     *
     * If the item is browsable, handle it by sending an event to the Activity to
     * browse to it, otherwise play it.
     */
    fun mediaItemClicked(clickedItem: MediaItemData) {
        if (clickedItem.browsable) {
            browseToItem(clickedItem)
        } else {
            playMedia(clickedItem)
        }
    }

    /**
     * This posts a browse [Event] that will be handled by the
     * observer in [MainActivity].
     */
    private fun browseToItem(mediaItem: MediaItemData) {
        _navigateToMediaItem.value = Event(mediaItem.mediaId)
    }

    /**
     * This method takes a [MediaItemData] and does one of the following:
     * - If the item is *not* the active item, then play it directly.
     * - If the item *is* the active item, check whether "pause" is a permitted command. If it is,
     *   then pause playback, otherwise send "play" to resume playback.
     */
    fun playMedia(mediaItem: MediaItemData) {
        val nowPlaying = mediaSessionConnection.nowPlaying.value
        val transportControls = mediaSessionConnection.transportControls

        val isPrepared = mediaSessionConnection.playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.mediaId == nowPlaying?.id) {
            mediaSessionConnection.playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Log.w(TAG, "Playable item clicked but neither play nor pause are enabled!" +
                                " (mediaId=${mediaItem.mediaId})")
                    }
                }
            }
        } else {
            transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    class Factory(private val mediaSessionConnection: MediaSessionConnection
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(mediaSessionConnection) as T
        }
    }
}
private const val TAG = "MainActivitytVM"