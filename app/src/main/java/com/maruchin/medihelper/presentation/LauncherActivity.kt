package com.maruchin.medihelper.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.usecases.user.CreateUserUseCase
import com.maruchin.medihelper.presentation.framework.BaseActivity
import org.koin.android.ext.android.inject

class LauncherActivity : BaseActivity() {

    companion object {
        private const val RC_AUTH = 1
        private const val LOGO_TIME = 300L
    }

    private val createUserUseCase: CreateUserUseCase by inject()
    private val auth: FirebaseAuth by inject()

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

    fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setTransparentStatusBar() {
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorTransparent)
    }
}
