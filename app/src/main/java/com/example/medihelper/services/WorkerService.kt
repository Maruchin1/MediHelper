package com.example.medihelper.services

import androidx.work.*

class WorkerService(private val workManager: WorkManager) {
    private val TAG = "WorkerService"

    fun enqueueLoggedUserSync() {
        val syncWork = OneTimeWorkRequestBuilder<LoggedUserSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueue(syncWork)
    }

    fun enqueueConnectedPersonSync() {
        val syncWork = OneTimeWorkRequestBuilder<ConnectedPersonSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueue(syncWork)
    }
}