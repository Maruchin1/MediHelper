package com.example.medihelper

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.localdatabase.LocalDatabase
import com.example.medihelper.localdatabase.PlannedMedicineScheduler
import com.example.medihelper.localdatabase.dao.MedicineDAO
import com.example.medihelper.localdatabase.dao.MedicinePlanDAO
import com.example.medihelper.localdatabase.dao.PersonDAO
import com.example.medihelper.localdatabase.dao.PlannedMedicineDAO
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object AppRepository {
    private val TAG = AppRepository::class.simpleName

    private val selectedPersonIDLive = MutableLiveData<Int>()
    private val selectedPersonItemLive: LiveData<PersonItem> = Transformations.switchMap(selectedPersonIDLive) { personID ->
        if (personID != -1) personDao.getItemLive(personID) else null
    }

    private lateinit var sharedPreferences: SharedPreferences
    private val medicineUnitListLive = MutableLiveData<List<String>>()

    private lateinit var medicineDao: MedicineDAO
    private lateinit var medicinePlanDao: MedicinePlanDAO
    private lateinit var plannedMedicineDao: PlannedMedicineDAO
    private lateinit var personDao: PersonDAO
    private var photosDir: File? = null

    fun init(app: Application) {
        Log.d(TAG, "init")
        initDatabaseData(app)
        initSharedPreferences(app)
        photosDir = app.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    }

    fun getSelectedPersonItemLive() = selectedPersonItemLive

    fun setSelectedPerson(personID: Int) {
        selectedPersonIDLive.value = personID
    }

    // SharedPreferences
    fun getMedicineUnitListLive(): LiveData<List<String>> = medicineUnitListLive

    fun getPersonColorResIDList(): List<Int> {
        return sharedPreferences.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)?.map { string ->
            string.toInt()
        } ?: emptyList()
    }

    fun getMainPersonID(): Int {
        return sharedPreferences.getInt(KEY_MAIN_PERSON_ID, -1)
    }

    // Database
    // Get

    fun getMedicineEntity(medicineID: Int) = medicineDao.getEntity(medicineID)

    fun getMedicineItemListLive() = medicineDao.getItemListLive()

    fun getFilteredMedicineItemListLive(searchQuery: String) = medicineDao.getFilteredItemListLive(searchQuery)

    fun getMedicineEditDataLive(medicineID: Int) = medicineDao.getEditDataLive(medicineID)

    fun getMedicineDetailsLive(medicineID: Int) = medicineDao.getMedicineDetailsLive(medicineID)

    fun getMedicinePlanItemListLive(personID: Int) = medicinePlanDao.getMedicinePlanItemListLive(personID)

    fun getPlannedMedicineEntity(plannedMedicineID: Int) = plannedMedicineDao.getEntity(plannedMedicineID)

    fun getPlannedMedicineDetailsLive(plannedMedicineID: Int) = plannedMedicineDao.getDetailsLive(plannedMedicineID)

    fun getPlannedMedicineItemListLiveByDate(date: Date, personID: Int) = plannedMedicineDao.getItemByDateListLive(date, personID)

    fun getPersonItemListLive() = personDao.getItemListLive()

    fun getPersonItemLive(personID: Int) = personDao.getItemLive(personID)

    // Delete
    fun deleteMedicine(medicineID: Int) = AsyncTask.execute { medicineDao.delete(medicineID) }

    fun deleteMedicinePlan(medicinePlanID: Int) = AsyncTask.execute { medicinePlanDao.delete(medicinePlanID) }

    fun deletePerson(personID: Int) = AsyncTask.execute { personDao.delete(personID) }

    // Update
    fun updateMedicine(medicineEntity: MedicineEntity) = AsyncTask.execute {
        medicineDao.update(medicineEntity)
    }

    fun updatePlannedMedicine(plannedMedicineEntity: PlannedMedicineEntity) = AsyncTask.execute {
        plannedMedicineDao.update(plannedMedicineEntity)
    }

    fun updatePerson(personEntity: PersonEntity) = AsyncTask.execute {
        personDao.update(personEntity)
    }

    // Insert
    fun insertMedicine(medicineEntity: MedicineEntity) = AsyncTask.execute {
        medicineDao.insert(medicineEntity)
    }

    fun insertMedicinePlan(medicinePlanEntity: MedicinePlanEntity) = AsyncTask.execute {
        val addedMedicinePlanID = medicinePlanDao.insert(medicinePlanEntity)
        val addedMedicinePlan = medicinePlanDao.getByID(addedMedicinePlanID.toInt())
        val plannedMedicineList = PlannedMedicineScheduler().getPlannedMedicineList(addedMedicinePlan)
        plannedMedicineDao.insert(plannedMedicineList)
        updatePlannedMedicinesStatuses()
    }

    fun insertPerson(personEntity: PersonEntity) = AsyncTask.execute {
        personDao.insert(personEntity)
    }

    // Other
    fun createTempPhotoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return File.createTempFile(
            "TMP_JPEG_${timeStamp}_",
            ".jpg",
            photosDir
        ).apply {
            deleteOnExit()
        }
    }

    fun createPhotoFileFromTemp(tmpFile: File): File {
        val fileName = tmpFile.name.replace("TMP_", "")
        val file = File(photosDir, fileName)
        tmpFile.copyTo(file)
        return file
    }

    fun updatePlannedMedicinesStatuses() = AsyncTask.execute {
        plannedMedicineDao.getEntityList().forEach { plannedMedicine ->
            plannedMedicine.updateStatusByCurrDate()
        }
    }

    private fun initDatabaseData(app: Application) {
        val database = LocalDatabase.getInstance(app.applicationContext)
        medicineDao = database.medicineDao()
        medicinePlanDao = database.medicinePlanDao()
        plannedMedicineDao = database.plannedMedicineDao()
        personDao = database.personDao()
        insertInitialPerson()
    }

    private fun initSharedPreferences(app: Application) {
        sharedPreferences = app.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                KEY_MEDICINE_UNIT_SET -> medicineUnitListLive.value = sharedPreferences.getStringSet(key, null)?.toList()
                KEY_MAIN_PERSON_ID -> selectedPersonIDLive.value = sharedPreferences.getInt(key, -1)
            }
        }

        insertInitialMedicinesTypes()
        insertInitialPersonColorResID()
        medicineUnitListLive.value = sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList()
        selectedPersonIDLive.value = sharedPreferences.getInt(KEY_MAIN_PERSON_ID, -1)
    }

    private fun insertInitialPersonColorResID() = AsyncTask.execute {
        val personColorResIDSet = sharedPreferences.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)
        if (personColorResIDSet.isNullOrEmpty()) {
            val colorSet = listOf(
                R.color.colorPersonBlue,
                R.color.colorPersonBrown,
                R.color.colorPersonCyan,
                R.color.colorPersonGray,
                R.color.colorPersonLightGreen,
                R.color.colorPersonOrange,
                R.color.colorPersonPurple,
                R.color.colorPersonRed,
                R.color.colorPersonYellow
            ).map { colorResID ->
                colorResID.toString()
            }.toMutableSet()
            sharedPreferences.edit(true) {
                putStringSet(KEY_PERSON_COLOR_RES_ID_SET, colorSet)
            }
        }
    }

    private fun insertInitialPerson() = AsyncTask.execute {
        if (personDao.getCount() == 0) {
            val mePerson = PersonEntity(
                personName = "Ja",
                personColorResID = R.color.colorPrimary
            )
            val insertedPersonID = personDao.insert(mePerson)
            sharedPreferences.edit(true) {
                putInt(KEY_MAIN_PERSON_ID, insertedPersonID.toInt())
            }
        }
    }

    private fun insertInitialMedicinesTypes() = AsyncTask.execute {
        val medicineUnitSet = sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)
        if (medicineUnitSet.isNullOrEmpty()) {
            val unitSet = mutableSetOf("pigułki", "ml", "g", "mg", "krople")
            sharedPreferences.edit {
                putStringSet(KEY_MEDICINE_UNIT_SET, unitSet)
                apply()
            }
        }
    }

    private const val APP_SHARED_PREFERENCES = "app-shared-preferences"
    private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
    private const val KEY_PERSON_COLOR_RES_ID_SET = "key-person-color-res-id-array"
    private const val KEY_MAIN_PERSON_ID = "key-main-person-id"
}