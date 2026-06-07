package com.hophey.jetgo.di

import android.app.Application
import com.hophey.jetgo.core.network.HttpClientFactory
import com.hophey.jetgo.feature.searchFlights.data.api.AirportApi
import com.hophey.jetgo.feature.searchFlights.data.api.FlightApi
import com.hophey.jetgo.feature.searchFlights.data.repository.AirportRepositoryImpl
import com.hophey.jetgo.feature.searchFlights.data.repository.FlightRepositoryImpl
import com.hophey.jetgo.feature.searchFlights.domain.repository.AirportRepository
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository
import com.hophey.jetgo.feature.searchFlights.domain.usecase.GetHotOffersUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchAirportsUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchFlightsUseCase
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.FlightSearchMainScreenViewModel
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SearchFlightsSharedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App : Application() {
    val flightsModule = module {
        single { HttpClientFactory.create() }

        single { FlightApi(get()) }
        single { AirportApi(get()) }

        single<FlightRepository> { FlightRepositoryImpl(get()) }
        single<AirportRepository> { AirportRepositoryImpl(get()) }

        factory { GetHotOffersUseCase(get()) }
        factory { SearchAirportsUseCase(get()) }
        factory { SearchFlightsUseCase(get()) }

        factory { FlightSearchMainScreenViewModel(get(), get()) }
        factory { SearchFlightsSharedViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(flightsModule)
        }
    }
}