package com.inci.campuscompanion.data

import com.inci.campuscompanion.CampusEvent
import com.inci.campuscompanion.EventChange
import com.inci.campuscompanion.findMeaningfulChanges
import com.inci.campuscompanion.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getTasks()

    suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    suspend fun toggleTaskCompletion(task: Task) {
        taskDao.updateTaskCompletion(task.id, !task.isCompleted)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    /**
     * Step 6: refresh events and return ONLY the meaningful changes.
     *
     * Placeholder: simulates fetching from the server. When the real Events API
     * is connected, replace [cachedEvents] with what is saved locally and
     * [fetchedEvents] with the API response — the rest stays the same.
     */
    suspend fun refreshEvents(): List<EventChange> {
        delay(1000) // simulate network

        val cachedEvents = listOf(
            CampusEvent(1, "Career Fair", "10:00 AM", "Gym"),
            CampusEvent(2, "Coding Club", "4:00 PM", "Lab 3", description = "Bring a laptp"),
            CampusEvent(3, "Library Tour", "1:00 PM", "Library"),
            CampusEvent(4, "Movie Night", "8:00 PM", "Auditorium")
        )
        val fetchedEvents = listOf(
            CampusEvent(1, "Career Fair", "2:00 PM", "Main Hall"),                           // moved -> notify
            CampusEvent(2, "Coding Club", "4:00 PM", "Lab 3", description = "Bring a laptop"), // typo fix -> ignore
            CampusEvent(3, "Library Tour", "1:00 PM", "Library"),                            // unchanged -> ignore
            CampusEvent(4, "Movie Night", "8:00 PM", "Auditorium", isCancelled = true)        // cancelled -> notify
        )

        return findMeaningfulChanges(cachedEvents, fetchedEvents)
    }
}
