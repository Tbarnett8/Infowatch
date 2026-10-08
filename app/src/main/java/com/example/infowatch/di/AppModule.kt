package com.example.infowatch.di

import com.example.infowatch.api.HeroesApi
import com.example.infowatch.api.MapsApi
import com.example.infowatch.api.infrastructure.ApiClient
import com.example.infowatch.data.HeroesRepositoryImpl
import com.example.infowatch.data.MapsRepositoryImpl
import com.example.infowatch.domain.HeroesRepository
import com.example.infowatch.domain.MapsRepository
import com.example.infowatch.ui.heroes.HeroDetailViewModel
import com.example.infowatch.ui.heroes.HeroesViewModel
import com.example.infowatch.ui.maps.MapDetailViewModel
import com.example.infowatch.ui.maps.MapsViewModel
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private const val BASE_URL = "https://overfast-api.tekrop.fr"

val appModule = module {

    single { BASE_URL }

    // OkHttp client for network
    single { OkHttpClient.Builder().build() }

    // API client
    single { ApiClient(baseUrl = BASE_URL, client = get()) }

    // API interface
    single { HeroesApi(get()) }
    single { MapsApi(get()) }

    // Repository
    single<HeroesRepository> { HeroesRepositoryImpl(get()) }
    single<MapsRepository> { MapsRepositoryImpl(get()) }

    // ViewModel
    viewModelOf(::HeroesViewModel)
    viewModel { (heroKey: String) ->
        HeroDetailViewModel(repository = get(), heroKey = heroKey)
    }
    viewModelOf(::MapsViewModel)
    viewModel { (mapKey: String) ->
        MapDetailViewModel(repository = get(), mapKey = mapKey)
    }
}