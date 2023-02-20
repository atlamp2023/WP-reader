package com.github.atlamp2023.wpreader.features.list.data

import android.util.Log
import com.github.atlamp2023.wpreader.core.sources.OkHttpBaseSource
import com.github.atlamp2023.wpreader.core.sources.OkHttpSettings
import com.github.atlamp2023.wpreader.features.list.presentation.ResultList
import com.google.gson.annotations.SerializedName

import okhttp3.Request
import java.io.IOException
import java.util.*

const val MAX_POSTS = 50

class RemoteListSource(settings: OkHttpSettings)
    : OkHttpBaseSource(settings) {

    data class WPError(
        @SerializedName("code")
        val code: String? = null,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("data")
        val data: dataW? = null
    ){
        data class dataW(
            val status: Int? = null
        )
    }

    suspend fun detectMaxPages(): Int {
        val request = Request.Builder()
            .get()
            .endpoint("/wp-json/wp/v2/posts?_fields=id&per_page=$MAX_POSTS&page=1")
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        val headers = response.headers.toMap()
        headers.forEach { it.key.lowercase(Locale.getDefault()) }
        //"X-WP-TotalPages"
        val total = headers["x-wp-totalpages"]?.toInt() ?: 1
        return total
    }

    fun getListRemote(pageNumber: Int? = null, tagId: Int? = null): ResultList {

        var urlForRequest = if(tagId == null){
            "/wp-json/wp/v2/posts?_fields=id,link,title,excerpt,content&per_page=$MAX_POSTS"
        } else {
            "/wp-json/wp/v2/posts?tags=${tagId}&_fields=id,link,title,excerpt,content&per_page=$MAX_POSTS"
        }

        pageNumber?.let {  urlForRequest += "&page=${it}" }

        val request = Request.Builder()
            .get()
            .endpoint(urlForRequest)
            .build()

        val response = client.newCall(request).execute()

        val json = if (!response.isSuccessful) {
            if(response.code == 400){
                val err: WPError = gson.fromJson(response.body!!.string(), WPError::class.java)
                if (err.code == "rest_post_invalid_page_number") {
                    "[]"
                } else throw IOException("Unexpected code $response")
            } else throw IOException("Unexpected code $response")
        } else {
            response.body!!.string()
        }

        val listObjects = gson.fromJson(json, Array<PostPreviewItem>::class.java)
            .asList()
            .map { it.toListItem()  }

        return listObjects
    }
}