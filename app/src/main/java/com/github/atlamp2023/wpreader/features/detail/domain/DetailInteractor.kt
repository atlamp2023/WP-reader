package com.github.atlamp2023.wpreader.features.detail.domain

import android.util.Log
import com.github.atlamp2023.wpreader.TAG
import com.github.atlamp2023.wpreader.core.util.Result
import com.github.atlamp2023.wpreader.core.util.State
import com.github.atlamp2023.wpreader.features.detail.presentation.Detail
import com.github.atlamp2023.wpreader.features.detail.presentation.DetailItem

class DetailInteractor(private val getDetailUseCase: GetDetailUseCase) {

    fun showProgress(): Detail = Result.Pending()

    suspend fun getDetail(state: State?, id :Int): Detail {
        val result = try {
            val rawResult = getDetailUseCase.execute(state, id)
            // val newResult = rawResult.copy()
            Result.Success<DetailItem>(rawResult)
        }catch (e: Exception) {
            Log.d(TAG, "$e")
            Result.Error<DetailItem>(error = e)
        }
        return result
    }
}