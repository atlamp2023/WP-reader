package com.github.atlamp2023.wpreader.features.list.data

import com.github.atlamp2023.wpreader.features.list.presentation.ListItem
import com.google.gson.annotations.SerializedName

data class PostPreviewItem(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("title")
    val title: PostDescription? = null,
    @SerializedName("excerpt")
    val excerpt: PostDescription? = null,
) {
    data class PostDescription (
        val rendered: String? = null,
        val protected: Boolean? = null
    )

    fun toListItem() = ListItem(
        id = id,
        title = title?.rendered,
        preview = excerpt?.rendered,
        link = link
    )
}
