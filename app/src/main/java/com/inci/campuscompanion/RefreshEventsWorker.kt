package com.inci.campuscompanion

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.inci.campuscompanion.data.AppDatabase
import com.inci.campuscompanion.data.TaskRepository
import com.inci.campuscompanion.data.UserPreferencesRepository
import com.inci.campuscompanion.data.dataStore
import kotlinx.coroutines.flow.first

class RefreshEventsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val repository = TaskRepository(AppDatabase.getDatabase(applicationContext).taskDao())
        val preferences = UserPreferencesRepository(applicationContext.dataStore)

        // Step 6: the refresh tells us which events changed in a meaningful way.
        val changes = try {
            repository.refreshEvents()
        } catch (e: Exception) {
            return Result.retry()
        }

        // Nothing meaningful changed -> no notification.
        if (changes.isEmpty()) return Result.success()

        // Respect the user's choice from the toggle (Step 3).
        val notificationsOn = preferences.eventNotificationsEnabled.first()
        if (!notificationsOn) return Result.success()

        // One notification per changed event. Using the event id means a
        // second update to the same event replaces the old notification.
        changes.forEach { change ->
            EventNotifications.showEventUpdate(
                context = applicationContext,
                title = "${change.title} updated",
                text = change.summary,
                notificationId = EVENT_NOTIFICATION_BASE_ID + change.eventId
            )
        }
        return Result.success()
    }

    companion object {
        private const val EVENT_NOTIFICATION_BASE_ID = 2000
    }
}
