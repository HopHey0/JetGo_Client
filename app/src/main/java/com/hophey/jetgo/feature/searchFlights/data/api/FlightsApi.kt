package com.hophey.jetgo.feature.searchFlights.data.api

import android.util.Log
import com.hophey.jetgo.feature.searchFlights.data.dto.FlightsRequest
import com.hophey.jetgo.feature.searchFlights.data.dto.OffersResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class FlightApi(
    private val client: HttpClient
) {
    suspend fun getHotOffers(): OffersResponse {
        return client.get("/flights/hotOffers").body()
    }

    suspend fun searchFlights(request: FlightsRequest): OffersResponse {
        val response = client.post("/flights") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        Log.d("API", "raw response: ${response.bodyAsText()}")
        return response.body()
    }
}