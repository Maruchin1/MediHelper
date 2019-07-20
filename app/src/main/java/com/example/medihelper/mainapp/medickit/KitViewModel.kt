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

    val medicinesListLive = Repository.getMedicinesLive()
    val medicineTypesListLive = Repository.getMedicineTypesLive()

    companion object {
        const val STATE_GOOD_LIMIT = 0.75f
        const val STATE_MEDIUM_LIMIT = 0.4f
    }
}