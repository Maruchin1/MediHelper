package com.example.medihelper.mainapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        AppRepository.updatePlannedMedicinesStatuses()
    }

    fun setTransparentStatusBar(isTransparent: Boolean) {
        when(isTransparent) {
            true -> window.statusBarColor = Color.TRANSPARENT
            false -> window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
    }

    fun setStatusBarColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
    }

    fun showSnackBar(message: String) {
        Snackbar.make(root_lay, message, Snackbar.LENGTH_LONG).show()
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
        val mainDestinationArray = arrayOf(
            R.id.schedule_destination,
            R.id.medicine_plan_list_destination,
            R.id.kit_destination
        )
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            TransitionManager.beginDelayedTransition(bottom_nav)
            bottom_nav.visibility = if (destination.id in mainDestinationArray) View.VISIBLE else View.GONE
        }
    }
}
