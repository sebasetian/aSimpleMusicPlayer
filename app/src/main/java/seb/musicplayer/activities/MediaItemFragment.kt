package seb.musicplayer.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import seb.musicplayer.R
import seb.musicplayer.model.MediaItemData
import seb.musicplayer.viewmodels.MainActivityViewModel
import kotlinx.android.synthetic.main.fragment_mediaitem_list.list
import kotlinx.android.synthetic.main.fragment_mediaitem_list.loadingSpinner
import seb.musicplayer.viewmodels.MediaItemFragmentViewModel

class MediaItemFragment: Fragment() {
    private lateinit var mediaId: String
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var mediaItemFragmentViewModel: MediaItemFragmentViewModel

    private val listAdapter = MediaItemAdapter { clickedItem ->
        mainActivityViewModel.mediaItemClicked(clickedItem)
    }

    companion object {
        fun newInstance(mediaId: String): MediaItemFragment {

            return MediaItemFragment().apply {
                arguments = Bundle().apply {
                    putString(MEDIA_ID_ARG, mediaId)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mediaitem_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Always true, but lets lint know that as well.
        val context = activity ?: return
        mediaId = arguments?.getString(MEDIA_ID_ARG) ?: return

        mainActivityViewModel = ViewModelProviders
            .of(context, InjectorUtils.provideMainActivityViewModel(context))
            .get(MainActivityViewModel::class.java)

        mediaItemFragmentViewModel = ViewModelProviders
            .of(this, InjectorUtils.provideMediaItemFragmentViewModel(context, mediaId))
            .get(MediaItemFragmentViewModel::class.java)
        mediaItemFragmentViewModel.mediaItems.observe(this,
            Observer<List<MediaItemData>> { list ->
                val isEmptyList = list?.isEmpty() ?: true
                loadingSpinner.visibility = if (isEmptyList) View.VISIBLE else View.GONE
                listAdapter.submitList(list)
            })

        // Set the adapter
        if (list is androidx.recyclerview.widget.RecyclerView) {
            list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(list.context)
            list.adapter = listAdapter
        }
    }
}

private const val MEDIA_ID_ARG = "com.example.android.uamp.MediaItemFragment.MEDIA_ID"