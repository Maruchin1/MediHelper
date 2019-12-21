package com.maruchin.medihelper.data.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maruchin.medihelper.data.SharedPref
import com.maruchin.medihelper.data.framework.FirestoreRepo
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.mappers.MedicineMapper
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
    private val sharedPref: SharedPref,
    private val mapper: MedicineMapper
) : FirestoreRepo<Medicine>(
    collectionRef = db.collection("users").document(auth.getCurrUserId()).collection("medicines"),
    mapper = mapper
), MedicineRepo {

    init {
        checkDefaultMedicineUnits()
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