package com.alexjprog.deezerforandroid.model

import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam

enum class ContentCategory(val titleResId: Int) {
    CHARTS(R.string.charts) {
        override fun toCategoryParam(): ContentCategoryParam =
            ContentCategoryParam.CHARTS
    };

    abstract fun toCategoryParam(): ContentCategoryParam
}