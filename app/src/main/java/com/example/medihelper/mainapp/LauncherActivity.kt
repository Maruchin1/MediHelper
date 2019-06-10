package com.example.medihelper.mainapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.medihelper.R

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        val startMainActivityRunnable = Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        Handler().postDelayed(startMainActivityRunnable, LAUNCHER_TIME)
    }

    companion object {
        const val LAUNCHER_TIME = 1000L
    }
}
