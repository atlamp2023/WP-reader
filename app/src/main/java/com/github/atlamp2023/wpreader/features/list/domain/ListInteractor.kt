package com.github.atlamp2023.wpreader.features.list.domain

import android.util.Log
import com.github.atlamp2023.wpreader.TAG
import com.github.atlamp2023.wpreader.features.list.presentation.*
import com.github.atlamp2023.wpreader.core.util.State
import com.github.atlamp2023.wpreader.core.util.Result
import com.github.atlamp2023.wpreader.features.list.presentation.DEFAULT_MAX_PAGES
import com.github.atlamp2023.wpreader.features.list.presentation.Items


class ListInteractor(private val getListUseCase: GetListUseCase,
                     private val detectMaxPagesUseCase: DetectMaxPagesUseCase
) {

    fun showProgress(): Items = Result.Pending()

    suspend fun detectMaxPages(): Int {
        return try {
            detectMaxPagesUseCase.execute()
        } catch (e: Throwable) {
            Log.d(TAG, "${e}")
            DEFAULT_MAX_PAGES
        }
    }

    suspend fun getList(state: State?, pageNumber: Int?, tagId: Int?): Items {
        var result: Result<ResultList>
        try {
            val resultRaw = getListUseCase.execute(state, pageNumber, tagId)
            result = if(resultRaw.isNullOrEmpty()){
                Result.Empty()
            } else {
                Result.Success( resultRaw )
            }
        } catch (e: Throwable) {
            result = Result.Error(error = e)
            Log.d(TAG, "${e}")
        }
        return result
    }
}