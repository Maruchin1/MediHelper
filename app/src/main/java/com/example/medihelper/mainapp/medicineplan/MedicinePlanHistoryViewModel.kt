package com.example.medihelper.mainapp.medicineplan

import androidx.lifecycle.*
import com.example.medihelper.custom.AppDate
import com.example.medihelper.localdatabase.pojo.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojo.MedicinePlanHistoryCheckbox
import com.example.medihelper.service.DateTimeService
import com.example.medihelper.service.MedicinePlanService
import com.example.medihelper.service.PersonService


class MedicinePlanHistoryViewModel(
    private val personService: PersonService,
    private val medicinePlanService: MedicinePlanService,
    private val dateTimeService: DateTimeService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val medicineNameLive: LiveData<String>
    val historyItemDisplayDataListLive: LiveData<List<HistoryItemDisplayData>>
    private val selectedMedicinePlanIDLive = MutableLiveData<Int>()
    private val medicinePlanHistoryLive: LiveData<MedicinePlanHistory>
    private val selectedPersonItemLive = personService.getCurrPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(selectedPersonItemLive) { personItem ->
            personItem.personColorResID
        }
        medicinePlanHistoryLive = Transformations.switchMap(selectedMedicinePlanIDLive) { medicinePlanID ->
            medicinePlanService.getHistoryLive(medicinePlanID)
        }
        medicineNameLive = Transformations.map(medicinePlanHistoryLive) { medicinePlanHistory ->
            medicinePlanHistory.medicineName
        }
        historyItemDisplayDataListLive = Transformations.map(medicinePlanHistoryLive) { medicinePlanHistory ->
            mutableListOf<HistoryItemDisplayData>().apply {
                medicinePlanHistory.medicinePlanHistoryCheckboxList.groupBy { medicinePlanHistoryCheckbox ->
                    medicinePlanHistoryCheckbox.plannedDate
                }.forEach { entry ->
                    val currDate = dateTimeService.getCurrDate()
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