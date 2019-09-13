package com.example.medihelper.mainapp.medicineplanlist

import androidx.lifecycle.*
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistoryCheckbox
import java.util.*

class MedicinePlanHistoryViewModel : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val medicineNameLive: LiveData<String>
    val historyItemDisplayDataListLive: LiveData<List<HistoryItemDisplayData>>
    private val selectedMedicinePlanIDLive = MutableLiveData<Int>()
    private val medicinePlanHistoryLive: LiveData<MedicinePlanHistory>
    private val selectedPersonItemLive = AppRepository.getSelectedPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        medicinePlanHistoryLive = Transformations.switchMap(selectedMedicinePlanIDLive) { medicinePlanID ->
            AppRepository.getMedicinePlanHistoryLive(medicinePlanID)
        }
        medicineNameLive = Transformations.map(medicinePlanHistoryLive) { medicinePlanHistory ->
            medicinePlanHistory.medicineName
        }
        historyItemDisplayDataListLive = Transformations.map(medicinePlanHistoryLive) { medicinePlanHistory ->
            mutableListOf<HistoryItemDisplayData>().apply {
                medicinePlanHistory.medicinePlanHistoryCheckboxList.groupBy { medicinePlanHistoryCheckbox ->
                    medicinePlanHistoryCheckbox.plannedDate
                }.forEach { entry ->
                    this.add(
                        HistoryItemDisplayData(
                            plannedDate = entry.key,
                            historyCheckboxList = entry.value
                        )
                    )
                }
            }
        }
    }

    fun setArgs(args: MedicinePlanHistoryDialogArgs) {
        selectedMedicinePlanIDLive.value = args.medicinePlanID
    }

    data class HistoryItemDisplayData(
        val plannedDate: Date,
        val historyCheckboxList: List<MedicinePlanHistoryCheckbox>
    )
}