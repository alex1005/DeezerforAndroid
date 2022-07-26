package com.alexjprog.deezerforandroid.model

import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam

enum class ContentCategory(val titleResId: Int) {
    CHARTS(R.string.charts) {
        override fun toCategoryParam(): ContentCategoryParam =
            ContentCategoryParam.CHARTS
    },
    FLOW(R.string.flow) {
        override fun toCategoryParam(): ContentCategoryParam =
            ContentCategoryParam.FLOW
    },
    RECOMMENDATIONS(R.string.recommendations) {
        override fun toCategoryParam(): ContentCategoryParam =
            ContentCategoryParam.RECOMMENDATIONS
    };

    abstract fun toCategoryParam(): ContentCategoryParam
}