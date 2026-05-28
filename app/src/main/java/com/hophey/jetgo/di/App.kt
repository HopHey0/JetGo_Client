package com.hophey.jetgo.di

import android.app.Application
import com.hophey.jetgo.core.network.HttpClientFactory
import com.hophey.jetgo.feature.searchFlights.data.api.FlightApi
import com.hophey.jetgo.feature.searchFlights.data.repository.FlightRepositoryImpl
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository
import com.hophey.jetgo.feature.searchFlights.domain.usecase.GetHotOffersUseCase
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.FlightSearchMainScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App: Application() {
    val flightsModule = module {
        single { HttpClientFactory.create() }
        single { FlightApi(get()) }
        single<FlightRepository> { FlightRepositoryImpl(get()) }

        factory { GetHotOffersUseCase(get()) }

        single { FlightSearchMainScreenViewModel(get()) }
    }


    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(flightsModule)
        }
    }
}