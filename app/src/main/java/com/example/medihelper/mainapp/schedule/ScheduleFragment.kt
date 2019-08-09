package com.example.medihelper.mainapp.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.medihelper.custom.CenterLayoutManager
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentScheduleBinding
import com.example.medihelper.mainapp.MainActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.recycler_item_schedule_timeline.view.*


class ScheduleFragment : Fragment() {
    private val TAG = ScheduleFragment::class.simpleName

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentScheduleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        setupMainActivity()
        setupTimelineRecyclerView()
        setupDatesViewPager()
        setInitialDate()
        observeViewModel()
    }

    fun onClickNavigateMenu() = findNavController().popBackStack()

    fun onClickNavigateList() {
        val direction = ScheduleFragmentDirections.actionScheduleDestinationToScheduleListDestination()
        findNavController().navigate(direction)
    }

    private fun observeViewModel() {
//        viewModel.run {
//            medicineListLive.observe(viewLifecycleOwner, Observer { })
//            medicineTypeListLive.observe(viewLifecycleOwner, Observer { })
//            medicinePlanListLive.observe(viewLifecycleOwner, Observer {  })
//        }
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
                    (recycler_view_timeline.adapter as ScheduleTimelineAdapter).selectDate(position)
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

    private fun setInitialDate() {
        (recycler_view_timeline.adapter as ScheduleTimelineAdapter).setInitialDate(viewModel.initialDatePosition)
        view_pager_dates.currentItem = viewModel.initialDatePosition
    }

    private fun openSelectMedicineDialog() {
        val dialog = SelectMedicineDialog()
        dialog.show(childFragmentManager, SelectMedicineDialog.TAG)
    }

    inner class ScheduleDayPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return viewModel.timelineDaysCount
        }

        override fun getItemPosition(`object`: Any): Int {
            return FragmentStatePagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            val fragment = ScheduleDayFragment()
            fragment.date = viewModel.getDateForPosition(position)
            return fragment
        }
    }

    inner class ScheduleTimelineAdapter :
        RecyclerView.Adapter<ScheduleTimelineAdapter.ScheduleTimelineViewHolder>() {

        private var selectedPosition = -1

        fun setInitialDate(position: Int) {
            selectedPosition = position
            recycler_view_timeline.scrollToPosition(position)
            notifyItemChanged(position)
        }

        fun selectDate(position: Int) {
            val prevSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(prevSelectedPosition)
            notifyItemChanged(position)
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
            return viewModel.timelineDaysCount
        }

        override fun onBindViewHolder(holder: ScheduleTimelineViewHolder, position: Int) {
            val date = viewModel.getDateForPosition(position)
            var textColorID = R.color.colorTextTertiary
            var selectedIndicatorVisibility = View.INVISIBLE

            if (selectedPosition == position) {
                textColorID = R.color.colorPrimary
                selectedIndicatorVisibility = View.VISIBLE
                recycler_view_timeline.smoothScrollToPosition(position)
            }

            holder.view.apply {
                txv_day.apply {
                    text = AppDateTimeUtil.dayMonthString(date)
                    setTextColor(resources.getColor(textColorID))
                }
                txv_day_of_week.apply {
                    text = AppDateTimeUtil.dayOfWeekString(date)
                    setTextColor(resources.getColor(textColorID))
                }
                view_selected_indicator.visibility = selectedIndicatorVisibility
                lay_click.setOnClickListener {
                    selectDate(position)
                    view_pager_dates.currentItem = position
                }
            }
        }

        inner class ScheduleTimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view = itemView
        }
    }
}
