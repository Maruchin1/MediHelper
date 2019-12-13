package com.maruchin.medihelper.presentation.feature.profiles

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.domain.entities.Profile
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.presentation.model.PersonOptionsData
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonOptionsViewModel(
    private val personUseCases: PersonUseCases
) : ViewModel() {

    val personOptionsData: LiveData<PersonOptionsData> = MutableLiveData()
    val isUserLogged: LiveData<Boolean> = MutableLiveData(false)

    val personId: LiveData<Int>
        get() = _personId

    private val _personId = MutableLiveData<Int>()
    private val profile: LiveData<Profile> = MutableLiveData()

    init {
//        profile = Transformations.switchMap(_personId) { personUseCases.getPersonLiveById(it) }
//        personOptionsData = Transformations.map(profile) { mapPersonToPersonOptionsData(it) }
    }

//    fun setArgs(args: PersonOptionsFragmentArgs) {
//        _personId.value = args.personID
//    }

    fun deletePerson() = GlobalScope.launch {
//        _personId.value?.let { personUseCases.deletePersonById(it) }
    }

//    private fun mapPersonToPersonOptionsData(profile: Profile) = PersonOptionsData(
//        name = profile.name,
//        color = profile.color,
//        connectionKey = profile.connectionKey,
//        connectionKeyQrCode = profile.connectionKey?.let { createQrCode(it) }
//    )

    private fun createQrCode(text: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 500, 500)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}