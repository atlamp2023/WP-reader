package com.github.atlamp2023.wpreader.core.sources

import com.google.gson.Gson
import okhttp3.*

open class OkHttpBaseSource(
    private val settings: OkHttpSettings
) {
    val client: OkHttpClient = settings.client
    val gson: Gson = settings.gson

    fun Request.Builder.endpoint(endpoint: String): Request.Builder {
        url("${settings.baseUrl}$endpoint")
        return this
    }
}