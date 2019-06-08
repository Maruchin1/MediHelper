package com.example.medihelper.mainapp.schedule

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DaySchedulePagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val datesList = getInitialDatesList()
    val dayScheduleFragment = DayScheduleFragment()

    override fun getItem(position: Int): Fragment {
        return DayScheduleFragment.getInstance(datesList[position])
    }

    override fun getCount(): Int {
        return datesList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return datesList[position]
    }

    private fun getInitialDatesList(): List<String> {
        val resultList = ArrayList<String>()
        val day = Calendar.getInstance().apply {
            val negativeDayOffset = -((ITEMS_COUNT / 2) + 1)
            add(Calendar.DAY_OF_YEAR, negativeDayOffset)
        }
        for (i in 0 until ITEMS_COUNT) {
            day.add(Calendar.DAY_OF_YEAR, 1)
            resultList.add(getFormattedDate(day))
        }
        return resultList
    }

    private fun getFormattedDate(day: Calendar): String {
        val pattern = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        return simpleDateFormat.format(Date(day.timeInMillis))
    }

    companion object {
        const val ITEMS_COUNT = 11
        const val TODAY_INDEX = 5
    }
}