package com.maruchin.medihelper.presentation.feature.personsprofiles

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.domain.entities.AppMode
import com.maruchin.medihelper.domain.entities.Person
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.ServerConnectionUseCases
import com.maruchin.medihelper.presentation.model.PersonOptionsData
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonOptionsViewModel(
    private val personUseCases: PersonUseCases,
    private val serverConnectionUseCases: ServerConnectionUseCases
) : ViewModel() {

    val personOptionsData: LiveData<PersonOptionsData>
    val isUserLogged: LiveData<Boolean>

    val personId: LiveData<Int>
        get() = _personId

    private val _personId = MutableLiveData<Int>()
    private val person: LiveData<Person>

    init {
        person = Transformations.switchMap(_personId) { personUseCases.getPersonLiveById(it) }
        personOptionsData = Transformations.map(person) { mapPersonToPersonOptionsData(it) }
        isUserLogged = Transformations.map(serverConnectionUseCases.getAppModeLive()) { it == AppMode.LOGGED }
    }

    fun setArgs(args: PersonOptionsFragmentArgs) {
        _personId.value = args.personID
    }

    fun deletePerson() = GlobalScope.launch {
        _personId.value?.let { personUseCases.deletePersonById(it) }
    }

    private fun mapPersonToPersonOptionsData(person: Person) = PersonOptionsData(
        name = person.name,
        colorId = person.colorId,
        connectionKey = person.connectionKey,
        connectionKeyQrCode = person.connectionKey?.let { createQrCode(it) }
    )

    private fun createQrCode(text: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 500, 500)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}