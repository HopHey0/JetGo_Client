package com.hophey.jetgo.feature.searchFlights.data.repository

import com.hophey.jetgo.feature.searchFlights.data.dto.toDomain
import com.hophey.jetgo.feature.searchFlights.data.local.datastore.RecentSearchStorage
import com.hophey.jetgo.feature.searchFlights.domain.model.RecentSearch
import com.hophey.jetgo.feature.searchFlights.domain.model.toDto
import com.hophey.jetgo.feature.searchFlights.domain.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.ExperimentalTime

class RecentSearchRepositoryImpl(
    private val storage: RecentSearchStorage
) : RecentSearchRepository {

    override val searches: Flow<List<RecentSearch>> = storage.searches
        .map { list -> list.map { it.toDomain() } }

    @OptIn(ExperimentalTime::class)
    override suspend fun addSearch(search: RecentSearch) {
        val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())

        val current = storage.searches.first()
            .filter { LocalDate.parse(it.date) >= today }

        val updated = (listOf(search.toDto()) + current)
            .distinctBy { "${it.departureCode}_${it.arrivalCode}_${it.date}" }
            .take(3)

        storage.save(updated)
    }

    override suspend fun clearHistory() {
        storage.clear()
    }
}