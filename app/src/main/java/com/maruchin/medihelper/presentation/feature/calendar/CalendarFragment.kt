package com.maruchin.medihelper.presentation.feature.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCalendarBinding
import com.maruchin.medihelper.presentation.feature.profiles_menu.ProfileDialog
import com.maruchin.medihelper.presentation.framework.BaseHomeFragment
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : BaseHomeFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private val TAG = "CalendarFragment"

    companion object {
        private const val HORIZONTAL_CALENDAR_VISIBLE_ITEMS = 5
        private const val HORIZONTAL_CALENDAR_MARGIN_ITEMS = 2
    }

    private val viewModel: CalendarViewModel by viewModel()
    private lateinit var horizontalCalendar: HorizontalCalendar

    fun onClickOpenProfileData() {
        ProfileDialog().show(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        setStatusBarLiveColor()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableLightStatusBar()
        setupToolbarMenu()
        setupHorizontalCalendar()
        setupDatesViewPager()
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun setStatusBarLiveColor() {
        super.setStatusBarColorLive(viewModel.colorPrimary)
    }

    private fun disableLightStatusBar() {
        super.setLightStatusBar(false)
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_today -> view_pager_dates.currentItem = viewModel.data.initialPosition
            }
            true
        }
    }

    private fun setupHorizontalCalendar() {
        horizontalCalendar = HorizontalCalendar.Builder(root_lay, R.id.horizontal_calendar)
            .range(viewModel.data.startCalendar, viewModel.data.endCalendar)
            .datesNumberOnScreen(HORIZONTAL_CALENDAR_VISIBLE_ITEMS)
            .defaultSelectedDate(viewModel.data.initialCalendar)
            .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                view_pager_dates.currentItem = position - HORIZONTAL_CALENDAR_MARGIN_ITEMS
            }
        }
    }

    private fun setupDatesViewPager() {
        view_pager_dates.apply {
            adapter = CalendarDayAdapter()
            setCurrentItem(viewModel.data.initialPosition, false)
            registerOnPageChangeCallback(getOnPageChangeCallback())
        }
    }

    private fun getOnPageChangeCallback(): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                scrollHorizontalCalendarToPosition(position)
            }
        }
    }

    private fun scrollHorizontalCalendarToPosition(position: Int) {
        val selectedCalendar = viewModel.getCalendarForPosition(position)
        horizontalCalendar.selectDate(selectedCalendar, false)
    }

    // Inner classes
    private inner class CalendarDayAdapter : FragmentStateAdapter(this) {

        override fun getItemCount(): Int {
            return viewModel.data.daysCount
        }

        override fun createFragment(position: Int): Fragment {
            return getCalendarDayFragmentForPosition(position)
        }

        private fun getCalendarDayFragmentForPosition(position: Int): CalendarDayFragment {
            val dateForPosition = viewModel.getDateForPosition(position)
            return CalendarDayFragment().apply {
                itemsLive = viewModel.getLivePlannedMedicinesForDate(dateForPosition)
            }
        }
    }
}
