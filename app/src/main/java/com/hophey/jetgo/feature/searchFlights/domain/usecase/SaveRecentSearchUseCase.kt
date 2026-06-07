package com.hophey.jetgo.feature.searchFlights.domain.usecase

import com.hophey.jetgo.feature.searchFlights.domain.model.RecentSearch
import com.hophey.jetgo.feature.searchFlights.domain.repository.RecentSearchRepository

class SaveRecentSearchUseCase(private val repo: RecentSearchRepository) {
    suspend operator fun invoke(search: RecentSearch) = repo.addSearch(search)
}