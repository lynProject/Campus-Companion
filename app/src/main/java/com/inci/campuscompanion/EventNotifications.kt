package com.inci.campuscompanion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

/**
 * Everything related to "Event updates" notifications lives here.
 */
object EventNotifications {

    // The ID never changes once shipped: Android remembers the user's
    // settings for the channel under this ID.
    const val CHANNEL_ID = "event_updates"

    /**
     * Step 2: create the "Event updates" channel.
     * Safe to call many times: if the channel already exists, Android ignores the call.
     */
    fun createChannel(context: Context) {
        // Channels only exist on Android 8.0 (API 26) and newer. Our minSdk is 24.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.channel_event_updates_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.channel_event_updates_description)
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}
