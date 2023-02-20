package com.github.atlamp2023.wpreader.features.detail.data

import com.github.atlamp2023.wpreader.features.detail.presentation.DetailItem
import kotlinx.coroutines.*

class DetailRepository(private val remote:RemoteDetailSource,
                       private val local: LocalDetailSource
) {
    fun getDetailRemoteAsync(id: Int): Deferred<DetailItem> = CoroutineScope(Dispatchers.IO).async {
        val detail = remote.getPostById(id)
        return@async detail
    }
}