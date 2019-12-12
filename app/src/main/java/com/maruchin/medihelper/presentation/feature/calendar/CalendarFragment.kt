package com.maruchin.medihelper.presentation.feature.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.viewpager.widget.ViewPager
import com.maruchin.medihelper.presentation.framework.CenterLayoutManager
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCalendarBinding
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.recycler_item_schedule_timeline.view.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class CalendarFragment : BaseMainFragment<FragmentCalendarBinding>(R.layout.fragment_calendar) {
    private val TAG = CalendarFragment::class.simpleName

    private val viewModel: CalendarViewModel by viewModel()
    private val directions by lazyOf(CalendarFragmentDirections)

    private lateinit var horizontalCalendar: HorizontalCalendar

    fun onClickSelectPerson() = findNavController().navigate(directions.toPersonDialog())

    fun onClickToday() = horizontalCalendar.goToday(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenu()
        setupHorizontalCalendar()
        setupDatesViewPager()
        setupCalendarView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateAllStatus()
    }

    private fun observeViewModel() {
        viewModel.currCalendarData.observe(viewLifecycleOwner, Observer { calendarData ->
            view_pager_dates.currentItem = calendarData.position
            horizontalCalendar.selectDate(calendarData.date,  false)
        })

//        viewModel.selectedDate.observe(viewLifecycleOwner, Observer { selectedDate ->



//            viewLifecycleOwner.lifecycleScope.launch {
//                val position = viewModel.getPositionForDate(selectedDate)
//                val timelineAdapter = recycler_view_timeline.adapter as CalendarTimeLineAdapter
//                timelineAdapter.selectDate(position)

//                val position = viewModel.getPositionForDate(selectedDate)
//                val timelineAdapter = recycler_view_timeline.adapter as ScheduleTimelineAdapter
//                timelineAdapter.selectDate(position)

//                if (view_pager_dates.currentItem != position) {
//                    view_pager_dates.currentItem = position
//                }

//                val currDate = AppDate(calendar_view.date)
//                if (currDate == selectedDate) {
//                    calendar_view.date = selectedDate.timeInMillis
//                }
//            }
//        })
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_today -> {
                    onClickToday()
                }
                R.id.btn_calendar -> {
//                    TransitionManager.beginDelayedTransition(root_lay)
//                    viewModel.calendarLayoutVisible.value = true
                }
            }
            true
        }
    }

    private fun setupDatesViewPager() {
        view_pager_dates.apply {
            adapter = ScheduleDayPagerAdapter()
            offscreenPageLimit = 1
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
                    viewModel.selectDate(position)
                }
            })
        }
    }

    private fun setupHorizontalCalendar() {
        horizontalCalendar = HorizontalCalendar.Builder(root_lay, R.id.horizontal_calendar)
            .range(viewModel.startCalendar, viewModel.endCalendar)
            .datesNumberOnScreen(5)
            .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                viewModel.selectDate(position - 2)
            }
        }
    }

    private fun setupCalendarView() {
//        calendar_view.setOnDateChangeListener { _, year, month, day ->
//            Log.d(TAG, "calendar date change")
//            viewModel.selectDate(AppDate(year, month + 1, day))
//            TransitionManager.beginDelayedTransition(root_lay)
//            viewModel.calendarLayoutVisible.value = false
//        }
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
