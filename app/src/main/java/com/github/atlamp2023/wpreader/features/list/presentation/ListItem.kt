package com.github.atlamp2023.wpreader.features.list.presentation

import com.google.gson.annotations.SerializedName

data class ListItem(
    val id: Int? = null,
    val title: String? = null,
    val preview: String? = null,
    val link: String? = null
)
