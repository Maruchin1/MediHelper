package com.maruchin.medihelper.device.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maruchin.medihelper.domain.usecases.plannedmedicines.CheckNotTakenMedicinesUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class CheckNotTakenMedicinesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val checkUseCase: CheckNotTakenMedicinesUseCase by inject()

    override suspend fun doWork(): Result {
        checkUseCase.execute()
        return Result.success()
    }
}