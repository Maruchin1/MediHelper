package com.example.medihelper.mainapp.person

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.medihelper.localdata.pojo.PersonOptionsData
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonOptionsViewModel(
    private val personService: PersonService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResIDLive: LiveData<Int>
    val connectionKeyLive: LiveData<String>
    val connectionKeyQrCodeLive: LiveData<Bitmap>
    val isUserLoggedLive: LiveData<Boolean>

    private val personIDLive = MutableLiveData<Int>()
    private val personItemLive: LiveData<PersonOptionsData>

    init {
        personItemLive = Transformations.switchMap(personIDLive) { personID ->
            personService.getOptionsDataLive(personID)
        }
        personNameLive = Transformations.map(personItemLive) { it.personName }
        personColorResIDLive = Transformations.map(personItemLive) { it.personColorResId }
        connectionKeyLive = Transformations.map(personItemLive) { it.connectionKey }
        connectionKeyQrCodeLive = Transformations.map(connectionKeyLive) { connectionKey ->
            connectionKey?.let { createTempKeyQrCodeBitmap(it) }
        }
        isUserLoggedLive = Transformations.map(serverApiService.getUserEmailLive()) { !it.isNullOrEmpty() }
    }

    fun setArgs(args: PersonOptionsFragmentArgs) {
        personIDLive.value = args.personID
    }

    fun deletePerson() = GlobalScope.launch {
        personIDLive.value?.let { personService.delete(it) }
    }

    fun getPersonID() = personIDLive.value

    private fun createTempKeyQrCodeBitmap(personTempKey: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(personTempKey, BarcodeFormat.QR_CODE, 500, 500)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}