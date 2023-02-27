package com.github.atlamp2023.wpreader.features.list.domain

import com.github.atlamp2023.wpreader.features.list.data.ListRepository

class DetectMaxPagesUseCase(private val repository: ListRepository) {
    suspend fun execute(): Int {
        val result = repository.detectMaxPagesAsync()
        return result
    }
}