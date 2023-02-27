package com.github.atlamp2023.wpreader.features.list.domain

import com.github.atlamp2023.wpreader.features.list.data.ListRepository
import com.github.atlamp2023.wpreader.features.list.presentation.ResultList
import com.github.atlamp2023.wpreader.core.util.State
import java.lang.IllegalArgumentException

class GetListUseCase(private val repository: ListRepository) {
    suspend fun execute(state: State?, pageNumber: Int? = null, tagId: Int? = null): ResultList {
        val result = when(state){
            State.REMOTE -> repository.getListRemoteAsync(pageNumber, tagId)
            State.LOCAL -> throw IllegalArgumentException("State.LOCAL -> haven't implementation yet")
            else -> throw IllegalArgumentException("State can't be null")
        }
        return result
    }
}