package com.eyther.lumbridge.data.input.platform

import android.content.Context
import com.eyther.lumbridge.data.input.IFileReader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidFileReaderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IFileReader {

    override fun read(fileName: String): String {
        return context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}
