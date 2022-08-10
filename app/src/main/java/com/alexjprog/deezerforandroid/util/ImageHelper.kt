package com.alexjprog.deezerforandroid.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

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
}