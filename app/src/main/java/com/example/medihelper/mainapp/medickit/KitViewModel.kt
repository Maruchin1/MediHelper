package com.example.medihelper.mainapp.medickit

import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType

class KitViewModel : ViewModel() {
    private val TAG = KitViewModel::class.simpleName

    val medicinesListLive: LiveData<List<Medicine>>
    val medicineTypesListLive: LiveData<List<MedicineType>>

    val stateGoodLive: LiveData<String>
    val stateMediumLive: LiveData<String>
    val stateSmallLive: LiveData<String>

    init {
        medicinesListLive = Repository.getMedicinesLive()
        medicineTypesListLive = Repository.getMedicineTypesLive()
        stateGoodLive = Transformations.map(medicinesListLive) {
            Log.d(TAG, "stateGood medicinesList = $it")
            stateGoodMedicinesCount(it).toString()
        }
        stateMediumLive = Transformations.map(medicinesListLive) {
            Log.d(TAG, "stateMedium medicinesList = $it")
            stateMediumMedicinesCount(it).toString()
        }
        stateSmallLive = Transformations.map(medicinesListLive) {
            Log.d(TAG, "stateSmall medicinesList = $it")
            stateSmallMedicinesCount(it).toString()
        }
    }

    private fun stateGoodMedicinesCount(list: List<Medicine>): Int {

        if (list.isEmpty()) return 0
        return list.count { medicine ->
            val medicinePercentState = medicine.currState / medicine.packageSize
            medicinePercentState >= STATE_GOOD_LIMIT
        }
    }

    private fun stateMediumMedicinesCount(list: List<Medicine>): Int {
        if (list.isEmpty()) return 0
        return list.count { medicine ->
            val medicinePercentState = medicine.currState / medicine.packageSize
            medicinePercentState < STATE_GOOD_LIMIT && medicinePercentState > STATE_MEDIUM_LIMIT
        }
    }

    private fun stateSmallMedicinesCount(list: List<Medicine>): Int {
        if (list.isEmpty()) return 0
        return list.count { medicine ->
            val medicinePercentState = medicine.currState / medicine.packageSize
            medicinePercentState <= STATE_MEDIUM_LIMIT
        }
    }

    companion object {
        const val STATE_GOOD_LIMIT = 0.75f
        const val STATE_MEDIUM_LIMIT = 0.4f
    }
}