package com.example.medihelper.mainapp.medicineplanlist

import androidx.lifecycle.*
import com.example.medihelper.AppDateTime
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistoryCheckbox
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.services.PersonProfileService
import java.util.*

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
                    val currDate = AppDateTime.getCurrCalendar().time
                    this.add(
                        HistoryItemDisplayData(
                            plannedDate = entry.key,
                            historyCheckboxList = entry.value,
                            isToday = AppDateTime.compareDates(currDate, entry.key) == 0
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
        val historyCheckboxList: List<MedicinePlanHistoryCheckbox>,
        val isToday: Boolean
    )
}