package com.shaneoosthuizen.assessment.clonedstackoverflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaneoosthuizen.assessment.clonedstackoverflow.core.networkmonitor.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StackOverflowApplicationViewModel @Inject constructor(
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            networkMonitor.isCurrentlyConnected()
        )
}