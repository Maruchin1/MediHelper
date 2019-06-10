package com.example.medihelper.mainapp.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.medihelper.DateUtil
import java.util.*
import kotlin.collections.ArrayList

class ScheduleDayAdapter(fm: FragmentManager, private val viewModel: ScheduleViewModel) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val TAG = ScheduleDayAdapter::class.simpleName

    val datesList = getInitialDatesList()

    override fun getItem(position: Int): Fragment {
        val date = datesList[position]
        return ScheduleDayFragment.getInstance(DateUtil.dateToString(date))
    }

    override fun getCount(): Int {
        return datesList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val date = datesList[position]
        return DateUtil.dayAndMonthName(date)
    }

    private fun getInitialDatesList(): List<Date> {
        val resultList = ArrayList<Date>()
        val date = Calendar.getInstance().apply {
            val negativeDayOffset = -((ITEMS_COUNT / 2) + 1)
            add(Calendar.DAY_OF_YEAR, negativeDayOffset)
        }
        for (i in 0 until ITEMS_COUNT) {
            date.add(Calendar.DAY_OF_YEAR, 1)
            resultList.add(date.time)
        }
        return resultList
    }

    companion object {
        const val ITEMS_COUNT = 11
        const val TODAY_INDEX = 5
    }
}