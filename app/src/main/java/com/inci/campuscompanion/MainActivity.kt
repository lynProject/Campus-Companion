package com.inci.campuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inci.campuscompanion.data.CampusApi
import com.inci.campuscompanion.data.EventRepository
import com.inci.campuscompanion.ui.EventsScreen
import com.inci.campuscompanion.ui.EventsViewModel
import com.inci.campuscompanion.ui.theme.CampusCompanionTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Build Retrofit Instance & Events Repository
        val retrofit = Retrofit.Builder()
            .baseUrl("https://6ad70838f71f154ac4879fac.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val campusApi = retrofit.create(CampusApi::class.java)
        val eventRepository = EventRepository(campusApi)

        val eventsViewModel: EventsViewModel by viewModels {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EventsViewModel(eventRepository) as T
                }
            }
        }

        setContent {
            CampusCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        EventsScreen(viewModel = eventsViewModel)
                    }
                }
            }
        }
    }
}