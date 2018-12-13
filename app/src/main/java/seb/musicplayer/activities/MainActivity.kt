package seb.musicplayer.activities

import android.media.AudioManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import seb.musicplayer.R
import seb.musicplayer.viewmodels.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject lateinit var activityViewModel: MainActivityViewModel

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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
