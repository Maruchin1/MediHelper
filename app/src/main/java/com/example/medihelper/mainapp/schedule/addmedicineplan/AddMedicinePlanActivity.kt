package com.example.medihelper.mainapp.schedule.addmedicineplan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.medihelper.R

class AddMedicinePlanActivity : AppCompatActivity() {

    fun setStatusBarColor(colorResID: Int) {
        window.statusBarColor = ContextCompat.getColor(this, colorResID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicine_plan)
    }
}
