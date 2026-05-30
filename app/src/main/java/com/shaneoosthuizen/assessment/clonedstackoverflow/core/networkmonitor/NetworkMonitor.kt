package com.shaneoosthuizen.assessment.clonedstackoverflow.core.networkmonitor

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isConnected: Flow<Boolean>
    fun isCurrentlyConnected(): Boolean
}