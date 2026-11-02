package com.inci.campuscompanion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EventsScreen(viewModel: EventsViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = viewModel.uiState) {
            is EventsUiState.Loading -> CircularProgressIndicator()
            is EventsUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadEvents() }) {
                        Text("Retry")
                    }
                }
            }
            is EventsUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadEvents() }) {
                        Text("Retry")
                    }
                }
            }

                    // --- Add the "Load More" button right here inside the LazyColumn ---
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!state.endReached) {
                                Button(onClick = { viewModel.loadNextPage() }) {
                                    Text(if (viewModel.isPaginating) "Loading..." else "Load More")
                                }
                            } else {
                                Text("No more events", color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }}
    }
}