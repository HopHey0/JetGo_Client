package com.hophey.jetgo.feature.searchFlights.domain.usecase

import com.hophey.jetgo.feature.searchFlights.domain.repository.RecentSearchRepository

class ClearSearchHistoryUseCase(private val repo: RecentSearchRepository) {
    suspend operator fun invoke() = repo.clearHistory()
}