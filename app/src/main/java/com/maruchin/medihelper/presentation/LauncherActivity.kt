package com.maruchin.medihelper.presentation

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.BaseActivity

class LauncherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        setTransparentStatusBar()
    }

    fun startMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun setTransparentStatusBar() {
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorTransparent)
    }
}
