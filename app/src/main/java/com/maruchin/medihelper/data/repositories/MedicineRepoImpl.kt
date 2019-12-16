package com.maruchin.medihelper.data.repositories

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maruchin.medihelper.data.SharedPref
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.framework.getDocumentLive
import com.maruchin.medihelper.data.framework.getDocumentsLive
import com.maruchin.medihelper.data.model.MedicineDb
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.File

class MedicineRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val sharedPref: SharedPref
) : MedicineRepo {

    private val medicinesCollRef: CollectionReference
        get() = db.collection("users").document(auth.getCurrUserId()).collection("medicines")

    init {
        checkDefaultMedicineUnits()
    }

    override suspend fun addNew(entity: Medicine) = withContext(Dispatchers.IO) {
        val medicineDb = MedicineDb(entity)
        medicinesCollRef.add(medicineDb)
        return@withContext
    }

    override suspend fun update(entity: Medicine) = withContext(Dispatchers.IO) {
        val medicineDb = MedicineDb(entity)
        medicinesCollRef.document(entity.medicineId).set(medicineDb)
        return@withContext
    }

    override suspend fun deleteById(id: String) = withContext(Dispatchers.IO) {
        medicinesCollRef.document(id).delete()
        return@withContext
    }

    override suspend fun getById(id: String): Medicine? = withContext(Dispatchers.IO) {
        val doc = medicinesCollRef.document(id).get().await()
        val medicineDb = doc.toObject(MedicineDb::class.java)
        val medicine = medicineDb?.toEntity(doc.id)
        return@withContext medicine
    }

    override suspend fun getLiveById(id: String): LiveData<Medicine?> = withContext(Dispatchers.IO) {
        val docLive = medicinesCollRef.document(id).getDocumentLive()
        return@withContext Transformations.map(docLive) {
            val medicineDb = it.toObject(MedicineDb::class.java)
            val medicine = medicineDb?.toEntity(it.id)
            return@map medicine
        }
    }

    override suspend fun getAllList(): List<Medicine> = withContext(Dispatchers.IO) {
        val docsQuery = medicinesCollRef.get().await()
        val medicinesList = mutableListOf<Medicine>()
        docsQuery.forEach {
            val medicineDb = it.toObject(MedicineDb::class.java)
            val medicine = medicineDb.toEntity(it.id)
            medicinesList.add(medicine)
        }
        return@withContext medicinesList
    }

    override suspend fun getAllListLive(): LiveData<List<Medicine>> = withContext(Dispatchers.IO) {
        val docsLive = medicinesCollRef.getDocumentsLive()
        return@withContext Transformations.map(docsLive) { snapshotList ->
            val medicinesList = mutableListOf<Medicine>()
            snapshotList.forEach {
                val medicineDb = it.toObject(MedicineDb::class.java)
                val medicine = medicineDb?.toEntity(it.id)
                if (medicine != null) {
                    medicinesList.add(medicine)
                }
            }
            return@map medicinesList.toList()
        }
    }

    override suspend fun saveMedicinePicture(pictureFile: File) {
        val pictureFileRef = storage.reference.child(pictureFile.name)
        val fileUri = Uri.fromFile(pictureFile)
        pictureFileRef.putFile(fileUri).await()
    }

    override suspend fun deleteMedicinePicture(pictureName: String) {
        val pictureFileRef = storage.reference.child(pictureName)
        pictureFileRef.delete().await()
    }

    override suspend fun getMedicineUnits(): List<String> = withContext(Dispatchers.IO) {
        sharedPref.getMedicineUnitList()
    }

    override suspend fun searchForMedicineInfo(medicineName: String): List<MedicineInfoSearchResult> =
        withContext(Dispatchers.IO) {
            val searchResults = mutableListOf<MedicineInfoSearchResult>()

            val urlString = "https://www.mp.pl/pacjent/leki/szukaj.html?item_name=$medicineName"
            val doc = Jsoup.connect(urlString).get()

            val itemFoundLists = doc.getElementsByClass("item-found")
            if (itemFoundLists.isNotEmpty()) {
                val linksList = itemFoundLists[0].getElementsByTag("a")
                linksList.forEach { link ->
                    val searchResult = MedicineInfoSearchResult(
                        medicineName = link.text(),
                        urlString = link.attr("href")
                    )
                    searchResults.add(searchResult)
                }
            }

            return@withContext searchResults
        }

    override suspend fun getMedicineInfo(urlString: String): List<MedicineInfo> = withContext(Dispatchers.IO) {
        val infoResults = mutableListOf<MedicineInfo>()

        val doc = Jsoup.connect(urlString).get()

        val infoHeaders = doc.getElementsByTag("h2")
        infoHeaders.forEach { header ->
            val itemContent = header.nextElementSibling()

            if (itemContent?.className() == "item-content") {
                val info = MedicineInfo(
                    header = header.text(),
                    body = itemContent.text()
                )
                infoResults.add(info)
            }
        }

        return@withContext infoResults
    }

    private fun checkDefaultMedicineUnits() {
        val medicineUnits = sharedPref.getMedicineUnitList()
        if (medicineUnits.isNullOrEmpty()) {
            sharedPref.saveMedicineUnitList(getDefaultMedicineUnits())
        }
    }

    private fun getDefaultMedicineUnits() = listOf("dawki", "tabletki", "ml", "g", "mg", "pastylki")
}