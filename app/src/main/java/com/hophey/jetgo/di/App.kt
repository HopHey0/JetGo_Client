package com.hophey.jetgo.di

import android.app.Application
import com.hophey.jetgo.core.datastorage.SessionStorage
import com.hophey.jetgo.core.network.HttpClientFactory
import com.hophey.jetgo.feature.auth.data.api.AuthApi
import com.hophey.jetgo.feature.auth.data.repository.AuthRepositoryImpl
import com.hophey.jetgo.feature.auth.domain.repository.AuthRepository
import com.hophey.jetgo.feature.auth.domain.usecase.CheckIfLoggedUseCase
import com.hophey.jetgo.feature.auth.domain.usecase.LoginUseCase
import com.hophey.jetgo.feature.auth.domain.usecase.LogoutUseCase
import com.hophey.jetgo.feature.auth.domain.usecase.RegisterUseCase
import com.hophey.jetgo.feature.auth.presentation.viewmodel.ProfileViewModel
import com.hophey.jetgo.feature.searchFlights.data.api.AirportApi
import com.hophey.jetgo.feature.searchFlights.data.api.FlightApi
import com.hophey.jetgo.feature.searchFlights.data.local.datastore.RecentSearchStorage
import com.hophey.jetgo.feature.searchFlights.data.repository.AirportRepositoryImpl
import com.hophey.jetgo.feature.searchFlights.data.repository.FlightRepositoryImpl
import com.hophey.jetgo.feature.searchFlights.data.repository.RecentSearchRepositoryImpl
import com.hophey.jetgo.feature.searchFlights.domain.repository.AirportRepository
import com.hophey.jetgo.feature.searchFlights.domain.repository.FlightRepository
import com.hophey.jetgo.feature.searchFlights.domain.repository.RecentSearchRepository
import com.hophey.jetgo.feature.searchFlights.domain.usecase.ClearSearchHistoryUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.GetHotOffersUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.GetRecentSearchesUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SaveRecentSearchUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchAirportsUseCase
import com.hophey.jetgo.feature.searchFlights.domain.usecase.SearchFlightsUseCase
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.FlightSearchMainScreenViewModel
import com.hophey.jetgo.feature.searchFlights.presentation.viewModel.SearchFlightsSharedViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

class App : Application() {

    val flightsModule = module {
        single { RecentSearchStorage(androidContext()) }
        single { SessionStorage(androidContext()) }

        single { HttpClientFactory.create(get()) }

        single { FlightApi(get()) }
        single { AirportApi(get()) }
        single { AuthApi(get()) }

        single<FlightRepository> { FlightRepositoryImpl(get()) }
        single<AirportRepository> { AirportRepositoryImpl(get()) }
        single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

        single<RecentSearchRepository> { RecentSearchRepositoryImpl(get()) }

        factory { SaveRecentSearchUseCase(get()) }
        factory { GetRecentSearchesUseCase(get()) }
        factory { GetHotOffersUseCase(get()) }
        factory { SearchAirportsUseCase(get()) }
        factory { SearchFlightsUseCase(get()) }
        factory { ClearSearchHistoryUseCase(get()) }

        factory { LoginUseCase(get()) }
        factory { RegisterUseCase(get()) }
        factory { LogoutUseCase(get()) }
        factory { CheckIfLoggedUseCase(get()) }

        viewModel { ProfileViewModel(get(), get(), get(), get()) }
        viewModel { FlightSearchMainScreenViewModel(get(), get(), get(), get(), get()) }
        viewModel { SearchFlightsSharedViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(flightsModule)
        }
    }
}