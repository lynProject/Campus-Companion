package com.inci.campuscompanion

/**
 * Step 6: decide which Event changes are worth a notification.
 */
data class CampusEvent(
    val id: Int,
    val title: String,
    val startTime: String,
    val location: String,
    val isCancelled: Boolean = false,
    val description: String = ""
)

/** One change the user should hear about. */
data class EventChange(
    val eventId: Int,
    val title: String,
    val summary: String
)

/**
 * Compares the events we had before with the freshly fetched ones.
 * Only MEANINGFUL updates count: cancelled, new time, new location.
 * Cosmetic edits (for example a description typo fix) and unchanged events are ignored,
 * so the user is not spammed.
 */
fun findMeaningfulChanges(
    old: List<CampusEvent>,
    new: List<CampusEvent>
): List<EventChange> {
    val oldById = old.associateBy { it.id }

    return new.mapNotNull { fresh ->
        val before = oldById[fresh.id] ?: return@mapNotNull null // brand-new event: not an "update"

        val summary = when {
            !before.isCancelled && fresh.isCancelled ->
                "This event has been cancelled."
            before.startTime != fresh.startTime && before.location != fresh.location ->
                "Moved to ${fresh.location}, now at ${fresh.startTime}."
            before.startTime != fresh.startTime ->
                "New time: ${fresh.startTime} (was ${before.startTime})."
            before.location != fresh.location ->
                "New location: ${fresh.location} (was ${before.location})."
            else -> null // nothing meaningful changed
        }

        summary?.let { EventChange(fresh.id, fresh.title, it) }
    }
}
