package com.github.atlamp2023.wpreader.core.sources

import com.google.gson.Gson
import okhttp3.OkHttpClient

class OkHttpSettings(
    val client: OkHttpClient,
    val gson: Gson,
    val baseUrl: String
)