package com.github.atlamp2023.wpreader.core.sources


import com.google.gson.Gson
import okhttp3.OkHttpClient
import com.github.atlamp2023.wpreader.BuildConfig
import javax.net.ssl.*

const val BASE_URL = BuildConfig.BASE_URL // "https://10.0.2.2:443"

object OkHttpSettingsProvider {

    /*private val customHostnameVerifier = object: HostnameVerifier {
        override fun verify(p0: String?, p1: SSLSession?): Boolean {
            return true;
        }
    }*/

    private val client = OkHttpClient.Builder()
            //.hostnameVerifier(customHostnameVerifier)
            .build()

    val settings = OkHttpSettings(client = client, gson = Gson(), baseUrl = BASE_URL)
}