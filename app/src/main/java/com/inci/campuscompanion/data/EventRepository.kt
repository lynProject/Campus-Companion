
package com.inci.campuscompanion.data

import com.inci.campuscompanion.model.CampusEvent

class EventRepository(private val api: CampusApi) {
    suspend fun fetchEvents(page: Int, limit: Int): List<CampusEvent> {
        val remoteDtos = api.getEvents(page = page, limit = limit)
        return remoteDtos.map { dto ->
            CampusEvent(
                id = dto.id,
                title = dto.title,
                description = dto.description ?: "",
                date = dto.date ?: "",
                location = dto.location ?: ""
            )
        }
    }
}