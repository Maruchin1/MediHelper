package com.maruchin.medihelper.data.repositories

import com.maruchin.medihelper.data.framework.FirestoreEntityRepo
import com.maruchin.medihelper.data.mappers.MedicineMapper
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MedicineRepoImpl(
    private val appFirebase: AppFirebase,
    private val mapper: MedicineMapper
) : FirestoreEntityRepo<Medicine>(
    entityMapper = mapper,
    getCollection = appFirebase::getMedicinesCollection
), MedicineRepo {

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
}