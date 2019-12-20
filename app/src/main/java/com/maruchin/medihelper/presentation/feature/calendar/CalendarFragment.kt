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
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : BaseMainFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private val TAG = "CalendarFragment"

    private val viewModel: CalendarViewModel by viewModel()

    private lateinit var horizontalCalendar: HorizontalCalendar

    fun onClickOpenProfileData() {
        ProfileDialog().show(childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        super.setStatusBarColorLive(viewModel.colorPrimary)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setLightStatusBar(false)
        setupToolbarMenu()
        setupHorizontalCalendar()
        setupDatesViewPager()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateAllStatus()
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_today -> view_pager_dates.currentItem = viewModel.initialPosition
            }
            true
        }
    }

    private fun setupDatesViewPager() {
        view_pager_dates.apply {
            adapter = CalendarDayAdapter()
            currentItem = viewModel.initialPosition
            offscreenPageLimit = 1
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val selectedCalendar = viewModel.getCalendarForPosition(position)
                    horizontalCalendar.selectDate(selectedCalendar, false)
                }
            })
        }
    }

    private fun setupHorizontalCalendar() {
        horizontalCalendar = HorizontalCalendar.Builder(root_lay, R.id.horizontal_calendar)
            .range(viewModel.startCalendar, viewModel.endCalendar)
            .datesNumberOnScreen(5)
            .defaultSelectedDate(viewModel.initialCalendar)
            .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                view_pager_dates.currentItem = position - 2
            }
        }
    }

    // Inner classes
    private inner class CalendarDayAdapter : FragmentStateAdapter(this) {

        override fun getItemCount(): Int {
            return viewModel.calendarDaysCount
        }

        override fun createFragment(position: Int): Fragment {
            return CalendarDayFragment().apply {
                date = viewModel.getDateForPosition(position)
            }
        }

    }
}
