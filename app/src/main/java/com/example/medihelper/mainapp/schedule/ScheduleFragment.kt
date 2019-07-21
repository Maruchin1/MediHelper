package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupMainActivity()
        setupDatesViewPager()
        observeViewModel()
        setupToolbar()
        setupTimelineRecyclerView()
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

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupDatesViewPager() {
        view_pager_dates.adapter = ScheduleDayPagerAdapter(childFragmentManager)
        view_pager_dates.setCurrentItem(TIMELINE_DAYS_COUNT / 2, false)
        view_pager_dates.post {
            view_pager_dates.setCurrentItem(TIMELINE_DAYS_COUNT / 2, false)
        }
        view_pager_dates.adapter?.notifyDataSetChanged()
        view_pager_dates.offscreenPageLimit = 1
    }

    private fun setupTimelineRecyclerView() {
        recycler_view_timeline.adapter = ScheduleTimelineAdapter()
        recycler_view_timeline.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_timeline.scrollToPosition(TIMELINE_DAYS_COUNT / 2)
    }


    private fun openSelectMedicineDialog() {
        val dialog = SelectMedicineDialogFragment()
        dialog.show(childFragmentManager, SelectMedicineDialogFragment.TAG)
    }

    private fun observeViewModel() {
        viewModel.medicinesListLive.observe(viewLifecycleOwner, Observer { })
        viewModel.medicinesTypesListLive.observe(viewLifecycleOwner, Observer { })
    }

    private fun getDateString(position: Int): String {
        val calendar = DateUtil.getCurrCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position - (TIMELINE_DAYS_COUNT / 2))
        return DateUtil.dateToString(calendar.time)
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
            return ScheduleDayFragment.getInstance(getDateString(position))
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return getDateString(position)
        }
    }

    inner class ScheduleTimelineAdapter :
        RecyclerView.Adapter<ScheduleTimelineAdapter.ScheduleTimelineViewHolder>() {
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
            val dateString = getDateString(position)
            holder.txvDay.text = dateString
        }


        inner class ScheduleTimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val txvDay: TextView = itemView.findViewById(R.id.txv_day)
        }
    }

    companion object {
        private const val TIMELINE_DAYS_COUNT = 10000
    }
}
