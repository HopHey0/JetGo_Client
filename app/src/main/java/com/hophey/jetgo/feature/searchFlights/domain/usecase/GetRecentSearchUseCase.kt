package com.hophey.jetgo.feature.searchFlights.domain.usecase


import com.hophey.jetgo.feature.searchFlights.domain.repository.RecentSearchRepository

class GetRecentSearchesUseCase(private val repo: RecentSearchRepository) {
    operator fun invoke() = repo.searches
}