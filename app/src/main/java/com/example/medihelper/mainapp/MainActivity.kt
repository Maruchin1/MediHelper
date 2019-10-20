package com.example.medihelper.mainapp

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.R
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.services.AlarmService
import com.example.medihelper.services.MedicineSchedulerService
import com.example.medihelper.services.WorkerService
import com.example.medihelper.services.SharedPrefService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import java.io.File


class MainActivity : AppCompatActivity() {

    private val medicineSchedulerService: MedicineSchedulerService by inject()
    private val workerService: WorkerService by inject()
    private val sharedPrefService: SharedPrefService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            medicineSchedulerService.updatePlannedMedicinesStatuses()
        }
        checkAppModeAndSync()

        GlobalScope.launch {
//            val medicineRepository = get<MedicineRepository>()
//
//            println("Medicines")
//            medicineRepository.getEntityList().forEach { println(it.toString()) }
//            println("Images")
//            get<File>().list().forEach { println(it) }

//            val med1 = medicineRepository.getEntity(1)
//            medicineRepository.update(med1.copy(imageName = "Ashwagandha_20191013_1529558666985904986001246.jpg"))
//
//            val med2 = medicineRepository.getEntity(2)
//            medicineRepository.update(med2.copy(imageName = "Apap_20191013_1531277997978762543349790.jpg"))
//
//            val med3 = medicineRepository.getEntity(3)
//            medicineRepository.update(med3.copy(imageName = "Strepsils_20191013_1532263874212922618123730.jpg"))
//
//            val med4 = medicineRepository.getEntity(4)
//            medicineRepository.update(med4.copy(imageName = "Gripactive_20191013_1533532141783522762861255.jpg"))
//
//            medicineRepository.update(medicineRepository.getEntity(5).copy(imageName = "Tantum Verde_20191013_153514710673242282302129.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(6).copy(imageName = "Ibum_20191013_1536467449879569146255279.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(7).copy(imageName = "Żeń-szeń_20191013_1538021814111555819355412.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(8).copy(imageName = "No-spa Max_20191013_1539265631687407010602517.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(9).copy(imageName = "Cirrus duo_20191013_1540136627178333947751025.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(10).copy(imageName = "Nasonex_20191013_1541127379973368517063892.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(11).copy(imageName = "Witamina C_20191013_1542268303180432066800638.jpg"))
//            medicineRepository.update(medicineRepository.getEntity(12).copy(imageName = "Otrivin_20191013_1543424701815135639149280.jpg"))
        }
    }

    override fun onPause() {
        super.onPause()
        checkAppModeAndSync()
    }

    fun setMainColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_selected), // unchecked
            intArrayOf(android.R.attr.state_selected)  // pressed
        )
        val colors = intArrayOf(
            ContextCompat.getColor(this, R.color.colorTextTertiary),
            ContextCompat.getColor(this, colorResID)
        )
        val colorStateList = ColorStateList(states, colors)
        bottom_nav.run {
            itemIconTintList = colorStateList
            itemTextColor = colorStateList
        }
        bottom_nav.itemIconTintList = ColorStateList(states, colors)
    }

    fun showSnackbar(message: String) = Snackbar.make(frame_fragments, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()

    fun restartActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
    }

    private fun checkAppModeAndSync() {
        when (sharedPrefService.getAppMode()) {
            SharedPrefService.AppMode.LOGGED -> workerService.enqueueLoggedUserSync()
            SharedPrefService.AppMode.CONNECTED -> workerService.enqueueConnectedPersonSync()
        }
    }
}
