package com.github.atlamp2023.wpreader.features.detail.data

import com.github.atlamp2023.wpreader.core.sources.*
import com.github.atlamp2023.wpreader.features.detail.presentation.DetailItem

import okhttp3.Request
import java.io.IOException

class RemoteDetailSource(settings: OkHttpSettings)
    : OkHttpBaseSource(settings) {

    suspend fun getPostById(id: Int): DetailItem {
        val request = Request.Builder()
            .get()
            .endpoint("/wp-json/wp/v2/posts/${id}?_fields=id,title,content,link,tags")
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        val json = response.body!!.string()
        val result = gson.fromJson(json, com.github.atlamp2023.wpreader.features.detail.data.PostItem::class.java).toDetailItem()
        return result
    }
}