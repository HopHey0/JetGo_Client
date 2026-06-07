package com.hophey.jetgo.feature.searchFlights.domain.repository


import com.hophey.jetgo.feature.searchFlights.data.dto.RecentSearchDto
import com.hophey.jetgo.feature.searchFlights.domain.model.RecentSearch
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    val searches: Flow<List<RecentSearch>>
    suspend fun addSearch(search: RecentSearch)

    suspend fun clearHistory()
}