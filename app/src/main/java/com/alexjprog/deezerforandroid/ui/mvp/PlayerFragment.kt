package com.alexjprog.deezerforandroid.ui.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentPlayerBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.model.MediaTypeParam
import com.alexjprog.deezerforandroid.service.MediaPlayerService
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.mvp.contract.PlayerContract
import com.alexjprog.deezerforandroid.util.ImageHelper
import javax.inject.Inject

class PlayerFragment : Fragment(), PlayerContract.View {
    @Inject
    lateinit var presenter: PlayerContract.Presenter

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!

    private val args: PlayerFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
        presenter.onAttach(this)

        Intent(context, MediaPlayerService::class.java).apply {
            requireActivity().startService(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        with(binding) {
            btnPlayPause.setOnClickListener {
                presenter.playOrPauseMedia()
            }
            btnNext.setOnClickListener {
                presenter.nextTrack()
            }
            btnPrevious.setOnClickListener {
                presenter.previousTrack()
            }

            setNextButtonAvailability(false)
            setPreviousButtonAvailability(false)
            setPlayButtonState(null)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Intent(requireContext(), MediaPlayerService::class.java).apply {
            requireActivity().bindService(
                this,
                presenter.playerConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideAllNavigation()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).showAllNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().unbindService(presenter.playerConnection)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.onDetach()
    }

    override fun setPlayButtonState(playing: Boolean?) {
        try {
            with(binding) {
                if (playing == null) {
                    btnPlayPause.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_play_arrow_24
                        )
                    )
                    btnPlayPause.isEnabled = false
                } else {
                    val drawableState =
                        if (playing) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
                    btnPlayPause.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            drawableState
                        )
                    )
                    btnPlayPause.isEnabled = true
                }
            }
        } catch (e: NullPointerException) {
        }
    }

    override fun setPreviousButtonAvailability(enabled: Boolean) {
        binding.btnPrevious.isEnabled = enabled
    }

    override fun setNextButtonAvailability(enabled: Boolean) {
        binding.btnNext.isEnabled = enabled
    }

    override fun setTrackData(data: TrackModel) {
        with(binding) {
            tvTitle.text = data.title ?: resources.getString(R.string.unknown)
            ImageHelper.loadSimplePicture(tvCover, data.pictureLink)
        }
    }

    override fun getPlaylistSource(): MediaItemModel? {
        val id = args.playableMediaId
        return when (args.playableMediaType) {
            MediaTypeParam.TRACK -> TrackModel(id = id)
            MediaTypeParam.ALBUM -> AlbumModel(id = id)
            else -> null
        }
    }
}