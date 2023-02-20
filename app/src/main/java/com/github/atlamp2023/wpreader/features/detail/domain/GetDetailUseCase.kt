package com.github.atlamp2023.wpreader.features.detail.domain

import com.github.atlamp2023.wpreader.core.util.State
import com.github.atlamp2023.wpreader.features.detail.presentation.DetailItem
import java.lang.IllegalArgumentException

class GetDetailUseCase(private val repository: com.github.atlamp2023.wpreader.features.detail.data.DetailRepository) {
    suspend fun execute(state: State?, id: Int): DetailItem {
        val result = when(state){
            State.REMOTE -> repository.getDetailRemoteAsync(id).await()
            State.LOCAL -> throw IllegalArgumentException("State.LOCAL -> haven't implementation yet")
            else -> throw IllegalArgumentException("State can't be null")
        }
        return result
    }
}