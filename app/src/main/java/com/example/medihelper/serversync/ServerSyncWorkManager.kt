package com.example.medihelper.serversync

import androidx.work.*

class ServerSyncWorkManager(private val workManager: WorkManager) {
    private val TAG = "ServerSyncWorkManager"

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