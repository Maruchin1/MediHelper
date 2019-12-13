package com.maruchin.medihelper.presentation.feature.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCalendarBinding
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.model.CalendarEvent
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : BaseMainFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private val TAG = CalendarFragment::class.simpleName

    private val viewModel: CalendarViewModel by viewModel()
    private val directions by lazyOf(CalendarFragmentDirections)

    private lateinit var horizontalCalendar: HorizontalCalendar

    fun onClickOpenFullCalendar() {
        TransitionManager.beginDelayedTransition(root_lay)
        viewModel.changeFullCalendarMode(enabled = true)
    }

    fun onClickOpenPlansList() {
        findNavController().navigate(directions.toProfileDialog())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        super.setStatusBarColorLive(viewModel.colorPrimary)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenu()
        setupHorizontalCalendar()
        setupDatesViewPager()
        setupCalendarView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateAllStatus()
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_calendar -> {
                    onClickOpenFullCalendar()
                }
            }
            true
        }
    }

    private fun setupDatesViewPager() {
        view_pager_dates.apply {
            adapter = ScheduleDayPagerAdapter()
            offscreenPageLimit = 1
            currentItem = viewModel.initialPosition
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

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
//        horizontalCalendar.selectDate(viewModel.initialCalendar, true)
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                date?.let {
                    calendar_view.date = it.timeInMillis
                }
                view_pager_dates.currentItem = position - 2
            }
        }
    }

    private fun setupCalendarView() {
        calendar_view.date = viewModel.initialCalendar.timeInMillis
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, day)
            }
            horizontalCalendar.selectDate(selectedCalendar, true)
            TransitionManager.beginDelayedTransition(root_lay)
            viewModel.changeFullCalendarMode(enabled = false)
        }
    }

    // Inner classes
    inner class ScheduleDayPagerAdapter : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return viewModel.calendarDaysCount
        }

        override fun getItem(position: Int): Fragment {
            val fragment = CalendarDayFragment()
            fragment.date = viewModel.getDateForPosition(position)
            return fragment
        }
    }
}
