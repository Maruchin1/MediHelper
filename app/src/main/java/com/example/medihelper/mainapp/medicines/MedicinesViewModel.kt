package com.example.medihelper.mainapp.medicines

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.pojos.MedicineItem
import java.io.File

class MedicinesViewModel : ViewModel() {
    private val TAG = MedicinesViewModel::class.simpleName

    val searchQueryLive = MutableLiveData("")
    val medicineItemListLive: LiveData<List<MedicineItem>>
    val medicineAvailableLive: LiveData<Boolean>

    init {
        medicineItemListLive = Transformations.switchMap(searchQueryLive) { searchQuery ->
            Log.d(TAG, "searchQuery change = $searchQuery")
            if (searchQuery.isNullOrEmpty()) {
                AppRepository.getMedicineItemListLive()
            } else {
                AppRepository.getMedicineItemFilteredListLive(searchQuery)
            }
        }
        medicineAvailableLive = Transformations.map(medicineItemListLive) { list ->
            list != null && list.isNotEmpty()
        }
    }

    fun getMedicineKitItemDisplayData(medicineItem: MedicineItem): MedicineItemDisplayData {
        val medicineState = medicineItem.calcMedicineState()
        return MedicineItemDisplayData(
            medicineID = medicineItem.medicineID,
            medicineName = medicineItem.medicineName,
            medicineUnit = medicineItem.medicineUnit,
            stateAvailable = medicineState != null,
            medicineState = "${medicineItem.currState}/${medicineItem.packageSize}",
            stateLayoutWeight = medicineState,
            emptyLayoutWeight = medicineState?.let { 1 - it },
            stateColorId = medicineState?.let {
                when {
                    medicineState >= STATE_GOOD_LIMIT -> R.color.colorStateGood
                    medicineState > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                    else -> R.color.colorStateSmall
                }
            },
            medicineImageFile = medicineItem.photoFilePath?.let { File(it) }
        )
    }

    companion object {
        const val STATE_GOOD_LIMIT = 0.75f
        const val STATE_MEDIUM_LIMIT = 0.4f
    }

    data class MedicineItemDisplayData(
        val medicineID: Int,
        val medicineName: String,
        val medicineUnit: String,
        val stateAvailable: Boolean,
        val medicineState: String,
        val stateLayoutWeight: Float?,
        val emptyLayoutWeight: Float?,
        val stateColorId: Int?,
        val medicineImageFile: File?
    )
}