package com.github.atlamp2023.wpreader.features.detail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.atlamp2023.wpreader.core.util.*
import com.github.atlamp2023.wpreader.features.detail.domain.DetailInteractor
import kotlinx.coroutines.launch

typealias Detail = Result<DetailItem>

class DetailViewModel(private val interactor: DetailInteractor,
                      stateValue: State): ViewModel() {

    private val _detail: MutableLiveData<Detail> = MutableLiveData()
    val detail: LiveData<Detail> = _detail

    fun update(state: State, id: Int) = viewModelScope.launch {
        _detail.value = interactor.showProgress()
        _detail.value = interactor.getDetail(state, id)
    }

}