package com.example.medihelper.mainapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.R
import com.example.medihelper.services.MedicineSchedulerService
import com.example.medihelper.services.WorkerService
import com.example.medihelper.services.SharedPrefService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val medicineSchedulerService: MedicineSchedulerService by inject()
    private val workerService: WorkerService by inject()

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
        workerService.enqueueSynchronizeData()
    }

    override fun onPause() {
        super.onPause()
        workerService.enqueueSynchronizeData()
    }

    fun setStatusBarColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
    }

    fun showSnackbar(message: String) = Snackbar.make(frame_fragments, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()

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
