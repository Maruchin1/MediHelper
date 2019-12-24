package com.maruchin.medihelper.presentation

import android.content.Intent
import android.os.Bundle
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun restartApp() {
        val launcherIntent = Intent(this, LauncherActivity::class.java)
        startActivity(launcherIntent)
        finish()
    }
}
