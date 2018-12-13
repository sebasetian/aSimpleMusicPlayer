package seb.musicplayer.activities

import androidx.lifecycle.Observer
import android.media.AudioManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import seb.musicplayer.R
import seb.musicplayer.viewmodels.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject lateinit var viewModel: MainActivityViewModel

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        volumeControlStream = AudioManager.STREAM_MUSIC

        /**
         * Observe changes to the [MainActivityViewModel.rootMediaId]. When the app starts,
         * and the UI connects to [MusicService], this will be updated and the app will show
         * the initial list of media items.
         */
        viewModel.rootMediaId.observe(this,
            Observer<String> { rootMediaId ->
                if (rootMediaId != null) {
                    navigateToMediaItem(rootMediaId)
                }
            })

        /**
         * Observe [MainActivityViewModel.navigateToMediaItem] for [Event]s indicating
         * the user has requested to browse to a different [MediaItemData].
         */
        viewModel.navigateToMediaItem.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { mediaId ->
                navigateToMediaItem(mediaId)
            }
        })
    }

    private fun navigateToMediaItem(mediaId: String) {
        var fragment: MediaItemFragment? = getBrowseFragment(mediaId)

        if (fragment == null) {
            fragment = MediaItemFragment.newInstance(mediaId)

            supportFragmentManager.beginTransaction()
                .apply {
                    replace(R.id.browseFragment, fragment, mediaId)

                    // If this is not the top level media (root), we add it to the fragment
                    // back stack, so that actionbar toggle and Back will work appropriately:
                    if (!isRootId(mediaId)) {
                        addToBackStack(null)
                    }
                }
                .commit()
        }
    }

    private fun isRootId(mediaId: String) = mediaId == viewModel.rootMediaId.value

    private fun getBrowseFragment(mediaId: String): MediaItemFragment? {
        return supportFragmentManager.findFragmentByTag(mediaId) as MediaItemFragment?
    }
}
