package com.github.atlamp2023.wpreader.core.di


import com.github.atlamp2023.wpreader.features.detail.presentation.DetailViewModel
import com.github.atlamp2023.wpreader.features.list.presentation.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel <ListViewModel> () {
        params ->
        ListViewModel(interactor = get(), stateValue = params.get())
    }

    viewModel <DetailViewModel> () {
            params ->
        DetailViewModel(interactor = get(), stateValue = params.get())
    }

}