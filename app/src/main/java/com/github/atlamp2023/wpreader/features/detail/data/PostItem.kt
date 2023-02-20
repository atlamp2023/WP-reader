package com.github.atlamp2023.wpreader.features.detail.data

import com.github.atlamp2023.wpreader.features.detail.presentation.DetailItem
import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("title")
    val title: PostDescription? = null,
    @SerializedName("content")
    val content: PostDescription? = null,
    @SerializedName("tags")
    val tags: Array<Int>? = null
) {
    data class PostDescription (
        val rendered: String? = null,
        val protected: Boolean? = null
    )

    fun toDetailItem() = DetailItem(
        id = id,
        title = title?.rendered,
        content = content?.rendered,
        link = link,
        tags = tags
    )
}
