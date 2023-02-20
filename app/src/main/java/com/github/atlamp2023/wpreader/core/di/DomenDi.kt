package com.github.atlamp2023.wpreader.core.di

import com.github.atlamp2023.wpreader.features.detail.domain.*
import com.github.atlamp2023.wpreader.features.list.domain.*

import org.koin.dsl.module

val domainDetailModule = module {

    factory<GetDetailUseCase> {
        GetDetailUseCase(repository = get())
    }

    single<DetailInteractor> {
        DetailInteractor(getDetailUseCase = get())
    }
}

val domainListModule = module {

    factory<GetListUseCase> {
        GetListUseCase(repository = get())
    }

    factory<DetectMaxPagesUseCase> {
        DetectMaxPagesUseCase(repository = get())
    }

    single<ListInteractor> {
        ListInteractor(
            getListUseCase = get(),
            detectMaxPagesUseCase = get()
        )
    }
}