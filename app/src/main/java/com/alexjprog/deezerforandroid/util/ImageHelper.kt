package com.alexjprog.deezerforandroid.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.alexjprog.deezerforandroid.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object ImageHelper {
    fun loadRoundedCornersCover(destPicture: ImageView, uri: String?, cornerRadius: Int) =
        Glide.with(destPicture)
            .load(uri)
            .placeholder(R.drawable.default_track_cover)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
            .into(destPicture)

    fun loadRoundCover(destPicture: ImageView, uri: String?) =
        Glide.with(destPicture)
            .load(uri)
            .placeholder(R.drawable.default_track_cover)
            .circleCrop()
            .into(destPicture)

    fun loadSimpleCover(destPicture: ImageView, uri: String?) =
        Glide.with(destPicture)
            .load(uri)
            .placeholder(R.drawable.default_track_cover)
            .into(destPicture)

    fun loadLargeIconForNotification(
        context: Context,
        uri: String?,
        onLoadComplete: (Bitmap?) -> Unit
    ) =
        Glide.with(context)
            .asBitmap()
            .placeholder(R.drawable.default_track_cover)
            .error(R.drawable.default_track_cover)
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onLoadComplete(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    onLoadComplete(errorDrawable?.toBitmap())
                }
            })

    fun loadUserIcon(context: Context, uri: String?, onLoadComplete: (Drawable?) -> Unit) =
        Glide.with(context)
            .asDrawable()
            .error(R.drawable.account_icon)
            .load(uri)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    onLoadComplete(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    onLoadComplete(errorDrawable)
                }

            })
}