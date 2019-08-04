package com.example.medihelper.mainapp.medickit

import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository

class KitViewModel : ViewModel() {
    private val TAG = KitViewModel::class.simpleName

    val medicinesListLive = AppRepository.getMedicineListLive()
    val medicineTypesListLive = AppRepository.getMedicineTypeListLive()

    companion object {
        const val STATE_GOOD_LIMIT = 0.75f
        const val STATE_MEDIUM_LIMIT = 0.4f
    }
}