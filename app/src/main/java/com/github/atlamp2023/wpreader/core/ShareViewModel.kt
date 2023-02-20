package com.github.atlamp2023.wpreader.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.atlamp2023.wpreader.core.util.State

class ShareViewModel: ViewModel() {
    val state: MutableLiveData<State> = MutableLiveData()
}