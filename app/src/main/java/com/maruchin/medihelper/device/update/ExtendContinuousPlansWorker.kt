package com.maruchin.medihelper.device.update

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maruchin.medihelper.domain.usecases.plans.ExtendContinuousPlansUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class ExtendContinuousPlansWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val extendContinuousPlansUseCase: ExtendContinuousPlansUseCase by inject()

    override suspend fun doWork(): Result {
        extendContinuousPlansUseCase.execute()
        return Result.success()
    }
}