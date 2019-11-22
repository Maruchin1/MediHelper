package com.example.medihelper.presentation.framework

import androidx.appcompat.app.AppCompatActivity
import com.example.medihelper.MainApplication

abstract class BaseActivity : AppCompatActivity() {

    private val mainApp: MainApplication by lazy { applicationContext as MainApplication }

    override fun onResume() {
        super.onResume()
        mainApp.currActivity = this
    }

    override fun onPause() {
        clearReferences()
        super.onPause()
    }

    override fun onDestroy() {
        clearReferences()
        super.onDestroy()
    }

    private fun clearReferences() {
        if (mainApp.currActivity == this) {
            mainApp.currActivity = null
        }
    }
}