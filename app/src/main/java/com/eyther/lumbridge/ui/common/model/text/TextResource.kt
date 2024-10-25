package com.eyther.lumbridge.ui.common.model.text

import android.content.Context
import androidx.annotation.StringRes

sealed interface TextResource {
    fun getString(context: Context): String

    data class Text(val value: String) : TextResource {
        override fun getString(context: Context): String = value
    }

    data class Resource(@StringRes val resId: Int) : TextResource {
        override fun getString(context: Context): String = context.getString(resId)
    }

    data class ResourceWithArgs(@StringRes val resId: Int, val args: List<Any>) : TextResource {
        override fun getString(context: Context): String = context.getString(resId, *args.toTypedArray())
    }
}
