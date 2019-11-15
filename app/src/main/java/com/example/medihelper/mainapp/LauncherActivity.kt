package com.example.medihelper.mainapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.*
import com.example.medihelper.R
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.mainapp.launcher.LogoFragment
import com.example.medihelper.mainapp.launcher.MainPersonFragment
import kotlinx.android.synthetic.main.activity_launcher.*
import kotlinx.android.synthetic.main.fragment_logo.*
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {

    companion object {
        private const val LOGO_TIME = 500L
        private const val ANIM_TIME = 1000L
    }

    private val appSharedPref: AppSharedPref by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        setTransparentStatusBar()
        loadLogoFragment()
        checkFirstStart()
    }

    fun startMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

    private fun checkFirstStart() {
        if (appSharedPref.getMainPersonId() == null) {
            Handler().postDelayed({
                setLightStatusBar()
                circularHideBackground()
            }, LOGO_TIME)
            Handler().postDelayed({
                loadMainPersonFragment()
            }, LOGO_TIME + ANIM_TIME)
        } else {
            Handler().postDelayed({
                startMainActivity()
            }, LOGO_TIME)
        }
    }

    private fun loadLogoFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_fragments, LogoFragment())
            .commit()
    }

    private fun loadMainPersonFragment() {
        val mainPersonFragment = MainPersonFragment()
        supportFragmentManager.beginTransaction()
            .addSharedElement(card_logo, "card_logo_main_person")
            .replace(R.id.frame_fragments, mainPersonFragment)
            .commit()
    }

    private fun setTransparentStatusBar() {
        window?.statusBarColor = ContextCompat.getColor(this, R.color.colorTransparent)
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun circularHideBackground() {
        val cx = lay_background.width / 2
        val cy = lay_background.height / 2

        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(lay_background, cx, cy, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                lay_background.visibility = View.INVISIBLE
            }
        })
        anim.interpolator = FastOutSlowInInterpolator()
        anim.duration = ANIM_TIME
        anim.start()
    }
}
