package com.sublime.videochat.tooling.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

@JvmSynthetic
internal fun Context.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
