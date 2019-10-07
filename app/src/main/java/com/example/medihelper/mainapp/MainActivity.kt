package com.example.medihelper.mainapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.medihelper.R
import com.example.medihelper.services.MedicineSchedulerService
import com.example.medihelper.services.WorkerService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.dto.ConnectedPersonDto
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    companion object {
        const val EXTRA_CONNECTED_PERSON_DATA = "extra-connected-person-data"
        private const val RESTART_APP_PENDING_INTENT_ID = 1
    }

    private val medicineSchedulerService: MedicineSchedulerService by inject()
    private val workerService: WorkerService by inject()
    private val personRepository: PersonRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()

        checkConnectedPersonExtra()
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

    fun restartApplication(connectedPersonDto: ConnectedPersonDto? = null) {
        val intent = Intent(this, MainActivity::class.java)
        if (connectedPersonDto != null) {
            intent.putExtra(EXTRA_CONNECTED_PERSON_DATA, connectedPersonDto)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            RESTART_APP_PENDING_INTENT_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 10, pendingIntent)
        exitProcess(0)
    }

    fun setStatusBarColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
    }

    fun showSnackbar(message: String) = Snackbar.make(frame_fragments, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()

    private fun checkConnectedPersonExtra() {
        if (intent.hasExtra(EXTRA_CONNECTED_PERSON_DATA)) {
            Log.i(TAG, "connectedPersonDto")
            val connectedPersonDto = intent.getSerializableExtra(EXTRA_CONNECTED_PERSON_DATA) as ConnectedPersonDto

            GlobalScope.launch {
                val mainPerson = personRepository.getMainPersonEntity()
                mainPerson.apply {
                    personName = connectedPersonDto.personName
                    personColorResID = connectedPersonDto.personColorResId
                }
                personRepository.update(mainPerson)
            }
        }
    }

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
