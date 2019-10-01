package com.example.medihelper.mainapp.persons

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.services.SharedPrefService
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonOptionsViewModel(
    private val personRepository: PersonRepository,
    private val registeredUserApi: RegisteredUserApi,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {

    val personNameLive: LiveData<String>
    val personColorResIDLive: LiveData<Int>
    val personTempKeyLive: LiveData<String>
    val personTempKeyQRCodeLive: LiveData<Bitmap>

    private val personIDLive = MutableLiveData<Int>()
    private val personItemLive: LiveData<PersonItem>

    init {
        personItemLive = Transformations.switchMap(personIDLive) { personID ->
            personRepository.getItemLive(personID)
        }
        personNameLive = Transformations.map(personItemLive) { it.personName }
        personColorResIDLive = Transformations.map(personItemLive) { it.personColorResID }
        personTempKeyLive = loadPersonTempKey()
        personTempKeyQRCodeLive = Transformations.map(personTempKeyLive) { createTempKeyQrCodeBitmap(it) }
    }

    fun setArgs(args: PersonOptionsFragmentArgs) {
        personIDLive.value = args.personID
    }

    fun deletePerson() = GlobalScope.launch {
        personIDLive.value?.let { personRepository.delete(it) }
    }

    fun getPersonID() = personIDLive.value

    private fun loadPersonTempKey() = liveData {
        val authToken = sharedPrefService.getLoggedUserAuthToken()
        val personID = personIDLive.value?.let { personRepository.getRemoteID(it) }
        if (authToken != null && personID != null) {
            try {
                val personTempKey = registeredUserApi.getPersonTempKey(authToken, personID)
                emit(personTempKey)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun createTempKeyQrCodeBitmap(personTempKey: String): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(personTempKey, BarcodeFormat.QR_CODE, 500, 500)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}