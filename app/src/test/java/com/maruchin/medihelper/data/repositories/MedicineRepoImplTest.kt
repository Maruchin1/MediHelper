package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maruchin.medihelper.data.utils.DataSharedPref
import com.maruchin.medihelper.data.mappers.MedicineMapper
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.testingframework.mock

class MedicineRepoImplTest {

    private val auth: FirebaseAuth = mock()
    private val db: FirebaseFirestore = mock()
    private val storage: FirebaseStorage = mock()
    private val dataSharedPref: DataSharedPref = mock()
    private val mapper: MedicineMapper = mock()

    private val repo: MedicineRepo by lazy { MedicineRepoImpl(db, auth, storage, dataSharedPref, mapper) }

//    @Test
//    fun searchMedicineInfo_ResultFound() {
//        val medicineName = "hitaxa"
//
//        val result = runBlocking {
//            repo.searchForMedicineInfo(medicineName = medicineName)
//        }
//
//        Truth.assertThat(result.size).isEqualTo(4)
//        Truth.assertThat(result[0].medicineName).isEqualTo("Hitaxa (roztwór doustny)")
//        Truth.assertThat(result[0].urlString)
//            .isEqualTo("https://www.mp.pl/pacjent/leki/lek/82959,Hitaxa-roztwor-doustny")
//    }
//
//    @Test
//    fun searchMedicineInfo_NoResult() {
//        val medicineName = "dadasadggasvafvxvzcadf"
//
//        val result = runBlocking {
//            repo.searchForMedicineInfo(medicineName = medicineName)
//        }
//
//        Truth.assertThat(result.size).isEqualTo(0)
//    }
//
//    @Test
//    fun getMedicineInfo() {
//        val urlString = "https://www.mp.pl/pacjent/leki/lek/82959,Hitaxa-roztwor-doustny"
//
//        val result = runBlocking {
//            repo.getMedicineInfo(urlString = urlString)
//        }
//
//        Truth.assertThat(result.size).isEqualTo(9)
//        Truth.assertThat(result[0].header).isEqualTo("Co to jest Hitaxa?")
//    }
}