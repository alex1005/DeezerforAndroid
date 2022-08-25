package com.alexjprog.deezerforandroid.util

import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.model.ContentCategory

typealias OpenPlayerFragmentAction = (MediaItemModel) -> Unit
typealias OpenMoreContentFragmentAction = (ContentCategory) -> Unit