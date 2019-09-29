package com.example.medihelper.services

import androidx.work.*
import com.example.medihelper.localdatabase.repositories.*

class WorkerService(private val workManager: WorkManager) {
    private val TAG = "WorkerService"

    fun enqueueSynchronizeData() {
        val serverSyncWork = OneTimeWorkRequestBuilder<ServerSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueue(serverSyncWork)
    }
}