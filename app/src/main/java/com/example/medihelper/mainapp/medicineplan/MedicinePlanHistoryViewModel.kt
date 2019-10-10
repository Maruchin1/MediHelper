package com.example.medihelper.mainapp.medicineplan

import androidx.lifecycle.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistoryCheckbox
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.services.PersonProfileService


class MedicinePlanHistoryViewModel(
    private val medicinePlanRepository: MedicinePlanRepository,
    personProfileService: PersonProfileService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val medicineNameLive: LiveData<String>
    val historyItemDisplayDataListLive: LiveData<List<HistoryItemDisplayData>>
    private val selectedMedicinePlanIDLive = MutableLiveData<Int>()
    private val medicinePlanHistoryLive: LiveData<MedicinePlanHistory>
    private val selectedPersonItemLive = personProfileService.getCurrPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        medicinePlanHistoryLive = Transformations.switchMap(selectedMedicinePlanIDLive) { medicinePlanID ->
            medicinePlanRepository.getHistoryLive(medicinePlanID)
        }
        medicineNameLive = Transformations.map(medicinePlanHistoryLive) { medicinePlanHistory ->
            medicinePlanHistory.medicineName
        }
        historyItemDisplayDataListLive = Transformations.map(medicinePlanHistoryLive) { medicinePlanHistory ->
            mutableListOf<HistoryItemDisplayData>().apply {
                medicinePlanHistory.medicinePlanHistoryCheckboxList.groupBy { medicinePlanHistoryCheckbox ->
                    medicinePlanHistoryCheckbox.plannedDate
                }.forEach { entry ->
                    val currDate = AppDate.currDate()
                    this.add(
                        HistoryItemDisplayData(
                            plannedDate = entry.key,
                            historyCheckboxList = entry.value,
                            isToday = currDate == entry.key
                        )
                    )
                }
            }.sortedBy { it.plannedDate }
        }
    }

    fun setArgs(args: MedicinePlanHistoryDialogArgs) {
        selectedMedicinePlanIDLive.value = args.medicinePlanID
    }

    data class HistoryItemDisplayData(
        val plannedDate: AppDate,
        val historyCheckboxList: List<MedicinePlanHistoryCheckbox>,
        val isToday: Boolean
    )
}