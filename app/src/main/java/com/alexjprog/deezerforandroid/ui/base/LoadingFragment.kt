package com.alexjprog.deezerforandroid.ui.base

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexjprog.deezerforandroid.databinding.FragmentLoadingBinding

class LoadingFragment : Fragment() {

    private var _binding: FragmentLoadingBinding? = null
    private val binding: FragmentLoadingBinding
        get() = _binding!!

    private val loadAnimation: AnimatedVectorDrawable?
        get() = try {
            binding.ivLoad.drawable as? AnimatedVectorDrawable
        } catch (e: NullPointerException) {
            null
        }

    private lateinit var listener: LoadingFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as LoadingFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)

        with(binding) {
            btnTryAgain.setOnClickListener {
                listener.onTryAgain()
                startLoading()
            }
        }

        return binding.root
    }

    fun startLoading() {
        with(binding) {
            btnTryAgain.visibility = View.GONE
            tvErrorMessage.visibility = View.GONE
            startLoadAnimation()
        }
    }

    private fun startLoadAnimation() {
        with(binding) {
            loadAnimation?.apply {
                registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        ivLoad.post { loadAnimation?.start() }
                    }
                })
            }?.start()
            ivLoad.visibility = View.VISIBLE
        }
    }

    private fun stopLoadAnimation() {
        with(binding) {
            loadAnimation?.stop()
            ivLoad.visibility = View.GONE
        }
    }

    fun showError() {
        with(binding) {
            btnTryAgain.visibility = View.VISIBLE
            tvErrorMessage.visibility = View.VISIBLE
            stopLoadAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface LoadingFragmentListener {
        fun onTryAgain()
    }
}