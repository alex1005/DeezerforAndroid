package com.alexjprog.deezerforandroid.ui.view

import android.content.Context
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View

class WaveLoadingView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private val animatedVectorDrawable = drawable as? AnimatedVectorDrawable

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            View.VISIBLE -> startWaveAnimation()
            else -> stopWaveAnimation()
        }
    }

    private fun startWaveAnimation() {
        animatedVectorDrawable?.apply {
            registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable?) {
                    super.onAnimationEnd(drawable)
                    this@WaveLoadingView.post { this@apply.start() }
                }
            })
        }?.start()
    }

    private fun stopWaveAnimation() {
        animatedVectorDrawable?.stop()
    }
}