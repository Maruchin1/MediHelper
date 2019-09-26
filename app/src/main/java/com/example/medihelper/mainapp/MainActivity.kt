package com.example.medihelper.mainapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.R
import com.example.medihelper.services.MedicineSchedulerService
import com.example.medihelper.services.ServerSyncService
import com.example.medihelper.services.SharedPrefService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            val medicineSchedulerService: MedicineSchedulerService = get()
            medicineSchedulerService.updatePlannedMedicinesStatuses()

            val sharedPrefService: SharedPrefService = get()
            val serverSyncService: ServerSyncService = get()
            val authToken = sharedPrefService.getLoggedUserAuthToken()
            if (!authToken.isNullOrEmpty()) {
                serverSyncService.synchronizeData(authToken)
            }
        }
    }

    fun setStatusBarColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
    }

    fun showSnackbar(message: String) = Snackbar.make(root_lay, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id in arrayOf(R.id.medicinesFragment, R.id.moreFragment)) {
                setStatusBarColor(R.color.colorPrimary)
            }
        }
    }
}
