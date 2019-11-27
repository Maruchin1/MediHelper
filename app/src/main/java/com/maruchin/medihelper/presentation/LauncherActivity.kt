package com.maruchin.medihelper.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.usecases.user.CreateUserUseCase
import com.maruchin.medihelper.presentation.framework.BaseActivity
import kotlinx.coroutines.runBlocking
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

        Handler().postDelayed({
            if (isUserAuthenticated()) startMainActivity() else startAuthActivity()
        }, LOGO_TIME)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_AUTH) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                auth.currentUser?.let { currUser ->
                    val params = CreateUserUseCase.Params(
                        userId = currUser.uid,
                        userName = currUser.displayName,
                        email = currUser.email
                    )
                    runBlocking {
                        createUserUseCase.execute(params)
                    }
                    startMainActivity()
                }
            } else {
                //todo niepowodzenie uwierzytelniania
            }
        }
    }

    private fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    private fun startAuthActivity() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher_round)
                .build(),
            RC_AUTH
        )
    }

    fun startMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}
