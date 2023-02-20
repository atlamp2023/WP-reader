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
            val raw_result = getDetailUseCase.execute(state, id)
            // val new_result = raw_result.copy()
            Result.Success<DetailItem>(raw_result)
        }catch (e: Exception) {
            Log.d(TAG, "$e")
            Result.Error<DetailItem>(error = e)
        }
        return result
    }
}