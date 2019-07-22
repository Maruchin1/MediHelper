package com.example.medihelper.mainapp.schedule

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.medihelper.CenterLayoutManager
import com.example.medihelper.DateUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*


class ScheduleFragment : Fragment() {
    private val TAG = ScheduleFragment::class.simpleName

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_back_to_menu.setOnClickListener { findNavController().popBackStack() }
        setupMainActivity()
        observeViewModel()
        setupTimelineRecyclerView()
        setupDatesViewPager()
        setCurrDate()
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentScheduleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                setTransparentStatusBar(false)
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    show()
                    shrink()
                    setIconResource(R.drawable.round_add_alert_white_48)
                    text = ""
                    setOnClickListener { openSelectMedicineDialog() }
                }
            }
        }
    }

    private fun setupDatesViewPager() {
        view_pager_dates.apply {
            adapter = ScheduleDayPagerAdapter(childFragmentManager)
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
                    viewModel.selectedDatePositionLive.postValue(position)
                }
            })
        }
    }

    private fun setupTimelineRecyclerView() {
        recycler_view_timeline.apply {
            adapter = ScheduleTimelineAdapter()
            layoutManager = CenterLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setCurrDate() {
        val currDatePosition = TIMELINE_DAYS_COUNT / 2
        viewModel.selectedDatePositionLive.value = currDatePosition
    }

    private fun openSelectMedicineDialog() {
        val dialog = SelectMedicineDialogFragment()
        dialog.show(childFragmentManager, SelectMedicineDialogFragment.TAG)
    }

    private fun observeViewModel() {
        viewModel.run {
            medicinesListLive.observe(viewLifecycleOwner, Observer { })
            medicinesTypesListLive.observe(viewLifecycleOwner, Observer { })
            selectedDatePositionLive.observe(viewLifecycleOwner, Observer { selectedDatePosition ->
                if (selectedDatePosition != null) {
                    val timelineAdapter = (recycler_view_timeline.adapter as ScheduleTimelineAdapter)
                    timelineAdapter.selectDate(selectedDatePosition)
                    view_pager_dates.currentItem = selectedDatePosition
                }
            })
        }
    }

    private fun getCalendarForPosition(position: Int): Calendar {
        val calendar = DateUtil.getCurrCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position - (TIMELINE_DAYS_COUNT / 2))
        return calendar
    }

    inner class ScheduleDayPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return TIMELINE_DAYS_COUNT
        }

        override fun getItemPosition(`object`: Any): Int {
            return FragmentStatePagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            val calendar = getCalendarForPosition(position)
            val dateString = DateUtil.dateToString(calendar.time)
            return ScheduleDayFragment.getInstance(dateString)
        }
    }

    inner class ScheduleTimelineAdapter :
        RecyclerView.Adapter<ScheduleTimelineAdapter.ScheduleTimelineViewHolder>() {

        private var selectedPosition = -1

        fun selectDate(position: Int) {
            val prevSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(prevSelectedPosition)
            notifyItemChanged(position)
            recycler_view_timeline.scrollToPosition(position)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ScheduleTimelineViewHolder {
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.recycler_item_schedule_timeline, parent, false)
            return ScheduleTimelineViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return TIMELINE_DAYS_COUNT
        }

        override fun onBindViewHolder(holder: ScheduleTimelineViewHolder, position: Int) {
            val selectedDatePosition = viewModel.selectedDatePositionLive.value
            val calendar = getCalendarForPosition(position)
            var textColorID = R.color.colorTextTertiary
            var selectedIndicatorVisibility = View.INVISIBLE

            if (selectedDatePosition == position) {
                textColorID = R.color.colorPrimary
                selectedIndicatorVisibility = View.VISIBLE
                recycler_view_timeline.smoothScrollToPosition(position)
            }

            holder.apply {
                txvDay.apply {
                    text = DateUtil.dayMonthString(calendar.time)
                    setTextColor(resources.getColor(textColorID))
                }
                txvDayOfWeek.apply {
                    text = DateUtil.dayOfWeekString(calendar.time)
                    setTextColor(resources.getColor(textColorID))
                }
                viewSelectedIndicator.visibility = selectedIndicatorVisibility
                layCLick.setOnClickListener {
                    viewModel.selectedDatePositionLive.value = position
                }
            }
        }

        inner class ScheduleTimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txvDay: TextView = itemView.findViewById(R.id.txv_day)
            val txvDayOfWeek: TextView = itemView.findViewById(R.id.txv_day_of_week)
            val viewSelectedIndicator: View = itemView.findViewById(R.id.view_selected_indicator)
            val layCLick: LinearLayout = itemView.findViewById(R.id.lay_click)
        }
    }

    companion object {
        private const val TIMELINE_DAYS_COUNT = 10000
    }
}
