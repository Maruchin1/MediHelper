package com.example.medihelper.mainapp.persons

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.medihelper.localdatabase.pojos.PersonOptionsData
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.RegisteredUserApi
import com.example.medihelper.services.SharedPrefService
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonOptionsViewModel(private val personRepository: PersonRepository) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResIDLive: LiveData<Int>
    val connectionKeyLive: LiveData<String>
    val connectionKeyQrCodeLive: LiveData<Bitmap>

    private val personIDLive = MutableLiveData<Int>()
    private val personItemLive: LiveData<PersonOptionsData>

    init {
        personItemLive = Transformations.switchMap(personIDLive) { personID ->
            personRepository.getOptionsDataLive(personID)
        }
        personNameLive = Transformations.map(personItemLive) { it.personName }
        personColorResIDLive = Transformations.map(personItemLive) { it.personColorResID }
        connectionKeyLive = Transformations.map(personItemLive) { it.connectionKey }
        connectionKeyQrCodeLive = Transformations.map(connectionKeyLive) { connectionKey ->
            connectionKey?.let { createTempKeyQrCodeBitmap(it) }
        }
    }

    fun setArgs(args: PersonOptionsFragmentArgs) {
        personIDLive.value = args.personID
    }

    fun deletePerson() = GlobalScope.launch {
        personIDLive.value?.let { personRepository.delete(it) }
    }

    fun getPersonID() = personIDLive.value

    private fun createTempKeyQrCodeBitmap(personTempKey: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(personTempKey, BarcodeFormat.QR_CODE, 500, 500)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}