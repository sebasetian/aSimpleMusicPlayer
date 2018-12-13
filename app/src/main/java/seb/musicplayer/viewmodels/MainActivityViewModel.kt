package seb.musicplayer.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import seb.musicplayer.dagger.MediaSessionConnection
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
}