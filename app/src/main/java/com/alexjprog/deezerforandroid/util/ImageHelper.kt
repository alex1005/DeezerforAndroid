package com.alexjprog.deezerforandroid.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object ImageHelper {
    fun loadRoundedCornersPicture(destPicture: ImageView, uri: String?, cornerRadius: Int) =
        Glide.with(destPicture)
            .load(uri)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
            .into(destPicture)

    fun loadRoundPicture(destPicture: ImageView, uri: String?) =
        Glide.with(destPicture)
            .load(uri)
            .circleCrop()
            .into(destPicture)

    fun loadSimplePicture(destPicture: ImageView, uri: String?) =
        Glide.with(destPicture)
            .load(uri)
            .into(destPicture)

    fun loadLargeIconForNotification(
        context: Context,
        uri: String?,
        onLoadComplete: (Bitmap) -> Unit
    ) =
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onLoadComplete(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }

            })
}