package com.maruchin.medihelper.device.update

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maruchin.medihelper.domain.device.DeviceUpdates
import java.util.concurrent.TimeUnit

class DeviceUpdatesImpl(
    private val context: Context
) : DeviceUpdates {

    companion object {
        private const val CONTINUOUS_PLANS_UPDATE_INTERVAL_HOURS = 24L
        private const val CONTINUOUS_PLANS_UPDATE_WORK = "continuous-plans-update-work"
    }

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    override suspend fun setupContinuousPlansUpdates() {
        val work = PeriodicWorkRequestBuilder<ExtendContinuousPlansWorker>(
            repeatInterval = CONTINUOUS_PLANS_UPDATE_INTERVAL_HOURS,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()
        workManager.enqueueUniquePeriodicWork(
            CONTINUOUS_PLANS_UPDATE_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }
}