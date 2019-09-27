package com.example.medihelper.services

import androidx.work.*

class ServerSyncService(
    private val workManager: WorkManager
) {
    private val TAG = "ServerSyncService"

    fun synchronizeData(authToken: String) {
        val serverSyncWork = OneTimeWorkRequestBuilder<ServerSyncWorker>()
            .setInputData(
                workDataOf(ServerSyncWorker.KEY_AUTH_TOKEN to authToken)
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueue(serverSyncWork)
    }


}