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
import com.alexjprog.deezerforandroid.service.MediaPlayerService
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.mvp.contract.PlayerContract
import com.alexjprog.deezerforandroid.util.MEDIA_ID_KEY
import com.alexjprog.deezerforandroid.util.MEDIA_TYPE_KEY
import javax.inject.Inject

class PlayerFragment : Fragment(), PlayerContract.View {
    @Inject
    lateinit var presenter: PlayerContract.Presenter

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding get() = _binding!!

    override var isPlaying: Boolean = false
        set(value) {
            field = value
            setPlayButtonState(value)
        }

    private val args: PlayerFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
        presenter.onAttach(this)

        Intent(requireContext(), MediaPlayerService::class.java).apply {
            putExtra(MEDIA_TYPE_KEY, args.playableMediaType)
            putExtra(MEDIA_ID_KEY, args.playableMediaId)

            requireActivity().bindService(
                this,
                presenter.playerConnection,
                Context.BIND_AUTO_CREATE
            )
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
                if (isPlaying) presenter.pauseMedia()
                else presenter.playMedia()
            }
        }

        (requireActivity() as MainActivity).setBottomNavigationVisibility(false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as MainActivity).setBottomNavigationVisibility(true)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().unbindService(presenter.playerConnection)
        presenter.onDetach()
    }

    private fun setPlayButtonState(playing: Boolean) {
        try {
            val drawableState =
                if (playing) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
            binding.btnPlayPause.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    drawableState
                )
            )
        } catch (e: NullPointerException) {
        }
    }
}