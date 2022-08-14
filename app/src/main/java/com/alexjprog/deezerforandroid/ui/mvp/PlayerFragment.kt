package com.alexjprog.deezerforandroid.ui.mvp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PlayerFragment : Fragment(), PlayerContract.View, MediaPlayerService.MediaPlayerListener {
    @Inject
    lateinit var presenter: PlayerContract.Presenter

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!

    private val args: PlayerFragmentArgs by navArgs()
    private val timeFormatter = SimpleDateFormat("m:ss")

    private var mediaPlayerService: MediaPlayerService? = null
    private val playerConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mediaPlayerService = (service as MediaPlayerService.MediaPlayerBinder)
                .getMediaPlayerService()
            mediaPlayerService?.addMediaPlayerListener(this@PlayerFragment)
            mediaPlayerService?.playlistSource = getPlaylistSource()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaPlayerService?.removeMediaPlayerListener(this@PlayerFragment)
            mediaPlayerService = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
        presenter.onAttach(this)

        Intent(context, MediaPlayerService::class.java).apply {
            context.applicationContext.startService(this)
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
                mediaPlayerService?.playStopTrack()
            }
            btnNext.setOnClickListener {
                mediaPlayerService?.nextTrack()
            }
            btnPrevious.setOnClickListener {
                mediaPlayerService?.previousTrack()
            }
            sbSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvElapsedTime.text = timeFormatter.format(Date(progress.toLong()))
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    mediaPlayerService?.startSeek()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.progress?.let { mediaPlayerService?.endSeek(it) }
                }
            })

            updateCurrentTrack(hasPrevious = false, hasNext = false, currentTrack = null)
            onProgressChanged(0)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideAllNavigation()
        Intent(requireContext(), MediaPlayerService::class.java).apply {
            requireActivity().bindService(
                this,
                playerConnection,
                0
            )
        }
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).showAllNavigation()
        releaseBind()
    }

    private fun releaseBind() {
        mediaPlayerService?.removeMediaPlayerListener(this)
        requireActivity().unbindService(playerConnection)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        presenter.onDetach()
    }

    private fun setPlayButtonState(playing: Boolean?) {
        try {
            with(binding) {
                if (playing == null) {
                    btnPlayPause.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.play
                        )
                    )
                    btnPlayPause.isEnabled = false
                } else {
                    val drawableState =
                        if (playing) R.drawable.pause else R.drawable.play
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

    private fun setPreviousButtonAvailability(enabled: Boolean) {
        binding.btnPrevious.isEnabled = enabled
    }

    private fun setNextButtonAvailability(enabled: Boolean) {
        binding.btnNext.isEnabled = enabled
    }

    private fun setTrackData(data: TrackModel?) {
        with(binding) {
            tvTitle.text = data?.title ?: ""
            sbSeekBar.apply {
                min = 0
                max = data?.duration ?: 0
                progress = 0
            }
            tvDuration.text = timeFormatter.format(Date(data?.duration?.toLong() ?: 0L))
            ImageHelper.loadSimplePicture(tvCover, data?.pictureLink)
        }
    }

    private fun getPlaylistSource(): MediaItemModel? {
        val id = args.playableMediaId
        return when (args.playableMediaType) {
            MediaTypeParam.TRACK -> TrackModel(id = id)
            MediaTypeParam.ALBUM -> AlbumModel(id = id)
            else -> null
        }
    }

    override fun onPlayMedia() {
        setPlayButtonState(true)
    }

    override fun onPauseMedia() {
        setPlayButtonState(false)
    }

    override fun onProgressChanged(progress: Int) {
        if (mediaPlayerService?.isPlaying == null) return
        try {
            binding.sbSeekBar.progress = progress
        } catch (e: NullPointerException) {
        }
    }

    override fun updateCurrentTrack(
        hasPrevious: Boolean,
        hasNext: Boolean,
        currentTrack: TrackModel?
    ) {
        setPlayButtonState(null)
        setPreviousButtonAvailability(hasPrevious)
        setNextButtonAvailability(hasNext)
        setTrackData(currentTrack)
    }
}