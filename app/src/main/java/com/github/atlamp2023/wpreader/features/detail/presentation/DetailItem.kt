package com.github.atlamp2023.wpreader.features.detail.presentation

data class DetailItem(
    val id: Int? = null,
    val title: String? = null,
    val content: String? = null,
    val link: String? = null,
    val tags: Array<Int>? = null
)