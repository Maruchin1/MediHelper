package com.maruchin.medihelper.presentation

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.maruchin.medihelper.MainApplication
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.usecases.plannedmedicines.CheckNotTakenMedicinesUseCase
import com.maruchin.medihelper.presentation.framework.BaseActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        testNotTakenMedicinesChecking()
    }

    fun restartApp() {
        reloadDependencies()
        val launcherIntent = Intent(this, LauncherActivity::class.java)
        startActivity(launcherIntent)
        finish()
    }

    private fun reloadDependencies() {
        val app = applicationContext as MainApplication
        app.reloadDependencies()
    }

    //todo metoda testowa sprawdzajaca nieprzyjęte leki
    private fun testNotTakenMedicinesChecking() {
        val useCase: CheckNotTakenMedicinesUseCase = get()
        lifecycleScope.launch {
            useCase.execute()
        }
    }
}
