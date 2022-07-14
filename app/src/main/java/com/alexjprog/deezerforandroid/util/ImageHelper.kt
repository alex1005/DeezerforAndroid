package com.alexjprog.deezerforandroid.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object ImageHelper {
    fun loadRoundedPicture(destPicture: ImageView, uri: String, cornerRadius: Int) =
        Glide.with(destPicture)
            .load(uri)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(cornerRadius)))
            .into(destPicture)
}