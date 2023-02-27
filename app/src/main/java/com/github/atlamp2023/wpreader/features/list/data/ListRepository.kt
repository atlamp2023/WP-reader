package com.github.atlamp2023.wpreader.features.list.data

import com.github.atlamp2023.wpreader.features.list.presentation.ResultList
import kotlinx.coroutines.*

class ListRepository(private val remote: RemoteListSource,
                     private val local: LocalListSource) {

    suspend fun detectMaxPagesAsync(): Int = CoroutineScope(Dispatchers.IO).async {
        return@async remote.detectMaxPages()
    }.await()

    suspend fun getListRemoteAsync(pageNumber: Int? = null,
                           tagId: Int? = null): ResultList = CoroutineScope(Dispatchers.IO).async {
        val result = remote.getListRemote(pageNumber = pageNumber, tagId = tagId)
        return@async result
    }.await()
}