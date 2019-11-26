package com.maruchin.medihelper.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.deviceapi.NotificationApi
import com.maruchin.medihelper.presentation.framework.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity() {

    private val notificationApi: NotificationApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNav()
        notificationApi.enablePeriodicRemindersUpdate()
    }

    fun setMainColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_selected), // unchecked
            intArrayOf(android.R.attr.state_selected)  // pressed
        )
        val colors = intArrayOf(
            ContextCompat.getColor(this, R.color.colorTextTertiary),
            ContextCompat.getColor(this, colorResID)
        )
        val colorStateList = ColorStateList(states, colors)
        bottom_nav.run {
            itemIconTintList = colorStateList
            itemTextColor = colorStateList
        }
        bottom_nav.itemIconTintList = ColorStateList(states, colors)
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment)
        bottom_nav.setupWithNavController(navController)
    }
}
