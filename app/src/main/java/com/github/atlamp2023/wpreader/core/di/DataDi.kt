package com.github.atlamp2023.wpreader.core.di

import com.github.atlamp2023.wpreader.features.detail.data.*
import com.github.atlamp2023.wpreader.features.list.data.*
import com.github.atlamp2023.wpreader.core.sources.OkHttpSettings
import com.github.atlamp2023.wpreader.core.sources.OkHttpSettingsProvider

import org.koin.dsl.module

val dataDetailModule = module {

    single<OkHttpSettings> {
        OkHttpSettingsProvider.settings
    }

    single<RemoteDetailSource> {
        RemoteDetailSource(settings = get())
    }

    single<LocalDetailSource> {
        LocalDetailSource(settings = get())
    }

    single<DetailRepository> {
        DetailRepository(
            remote = get(),
            local = get()
        )
    }
}

val dataListModule = module {

    single<OkHttpSettings> {
        OkHttpSettingsProvider.settings
    }

    single<LocalListSource> {
        LocalListSource(context = get())
    }

    single<RemoteListSource> {
        RemoteListSource(settings = get())
    }

    single<ListRepository> {
        ListRepository(remote = get(), local = get())
    }
}
