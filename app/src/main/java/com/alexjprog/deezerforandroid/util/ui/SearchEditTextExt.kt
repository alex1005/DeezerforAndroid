package com.alexjprog.deezerforandroid.util.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText

@SuppressLint("ClickableViewAccessibility")
fun EditText.onEndDrawableClicked(onClicked: (view: EditText) -> Unit) {
    setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingEnd) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onStartDrawableClicked(onClicked: (view: EditText) -> Unit) {
    setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x <= v.totalPaddingStart) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onStartOrEndDrawableClicked(
    onStartClicked: (view: EditText) -> Unit,
    onEndClicked: (view: EditText) -> Unit
) {
    setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x <= v.totalPaddingStart) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onStartClicked(this)
                }
                hasConsumed = true
            } else if (event.x > v.width - v.totalPaddingEnd) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onEndClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}