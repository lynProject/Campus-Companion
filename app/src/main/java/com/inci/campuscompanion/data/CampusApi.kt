package com.inci.campuscompanion.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CampusApi {
    @GET("api/v1/events/event")
    suspend fun getEvents(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<EventDto>
}