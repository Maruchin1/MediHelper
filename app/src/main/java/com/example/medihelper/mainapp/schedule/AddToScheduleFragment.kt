package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medihelper.AppDateTimeUtil

import com.example.medihelper.R
import com.example.medihelper.SelectTimeDialog
import com.example.medihelper.databinding.FragmentAddToScheduleBinding
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.scheduledays.DaysOfWeekFragment
import com.example.medihelper.mainapp.schedule.scheduledays.IntervalOfDaysFragment
import com.example.medihelper.mainapp.schedule.scheduletype.ScheduleTypeContinuousFragment
import com.example.medihelper.mainapp.schedule.scheduletype.ScheduleTypePeriodFragment
import com.example.medihelper.mainapp.schedule.scheduletype.ScheduleTypeOnceFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_add_to_schedule.*
import java.sql.Time
import java.text.SimpleDateFormat


class AddToScheduleFragment : Fragment() {
    private val TAG = AddToScheduleFragment::class.simpleName

    private lateinit var viewModel: AddToScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(AddToScheduleViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupMainActivity()
        setupScheduleTypeChipGroup()
        setupScheduleDaysChipGroup()
        setupDoseHourRecyclerView()
        observeViewModel()
    }

    fun onClickSelectMedicine() {
        val dialog = SelectMedicineDialogFragment()
        dialog.show(childFragmentManager, SelectMedicineDialogFragment.TAG)
    }

    fun onClickAddDoseHour() {
        val adapter = recycler_view_schedule_hours.adapter as DoseHourAdapter
        adapter.addDoseHour()
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentAddToScheduleBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_to_schedule, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupMainActivity() {
        activity?.let {
            (it as MainActivity).run {
                val fab = findViewById<ExtendedFloatingActionButton>(R.id.btn_floating_action)
                fab.apply {
                    setIconResource(R.drawable.round_save_white_48)
                    text = "Zapisz"
                    extend()
                    setOnClickListener { }
                }
            }
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun observeViewModel() {
        viewModel.selectedMedicineLive.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                lay_selected_medicine.visibility = View.INVISIBLE
                txv_medicine_not_selected.visibility = View.VISIBLE
            } else {
                txv_medicine_not_selected.visibility = View.INVISIBLE
                lay_selected_medicine.visibility = View.VISIBLE
            }
        })
        val onceFragment = ScheduleTypeOnceFragment()
        val periodFragment = ScheduleTypePeriodFragment()
        val continuousFragment = ScheduleTypeContinuousFragment()
        viewModel.scheduleTypeLive.observe(viewLifecycleOwner, Observer { scheduleType ->
            if (scheduleType != null) {
                when (scheduleType) {
                    ScheduledMedicine.ScheduleType.ONCE -> changeScheduleTypeFragment(onceFragment)
                    ScheduledMedicine.ScheduleType.PERIOD -> changeScheduleTypeFragment(periodFragment)
                    ScheduledMedicine.ScheduleType.CONTINUOUS -> changeScheduleTypeFragment(continuousFragment)
                }
            }
        })
        val daysOfWeekFragment = DaysOfWeekFragment()
        val intervalOfDaysFragment = IntervalOfDaysFragment()
        viewModel.scheduleDaysLive.observe(viewLifecycleOwner, Observer { scheduleDays ->
            if (scheduleDays != null) {
                when (scheduleDays) {
                    ScheduledMedicine.ScheduleDays.EVERYDAY -> changeScheduleDaysFragment(null)
                    ScheduledMedicine.ScheduleDays.DAYS_OF_WEEK -> changeScheduleDaysFragment(daysOfWeekFragment)
                    ScheduledMedicine.ScheduleDays.INTERVAL_OF_DAYS -> changeScheduleDaysFragment(intervalOfDaysFragment)
                }
            }
        })
        viewModel.doseHourListLive.observe(viewLifecycleOwner, Observer { doseHourList ->
            Log.d(TAG, "doseHourList change = $doseHourList")
        })
    }

    private fun changeScheduleTypeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_schedule_type, fragment)
            .commit()
    }

    private fun changeScheduleDaysFragment(fragment: Fragment?) {
        if (fragment == null) {
            childFragmentManager.findFragmentById(R.id.frame_schedule_days)?.let { currFragment ->
                childFragmentManager.beginTransaction()
                    .remove(currFragment)
                    .commit()
            }
        } else {
            childFragmentManager.beginTransaction()
                .replace(R.id.frame_schedule_days, fragment)
                .commit()
        }
    }

    private fun setupScheduleTypeChipGroup() {
        chip_group_schedule_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_once -> viewModel.scheduleTypeLive.value = ScheduledMedicine.ScheduleType.ONCE
                R.id.chip_period -> viewModel.scheduleTypeLive.value = ScheduledMedicine.ScheduleType.PERIOD
                R.id.chip_continuous -> viewModel.scheduleTypeLive.value = ScheduledMedicine.ScheduleType.CONTINUOUS
            }
        }
        chip_group_schedule_type.check(R.id.chip_once)
    }

    private fun setupScheduleDaysChipGroup() {
        chip_group_schedule_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_everyday -> viewModel.scheduleDaysLive.value = ScheduledMedicine.ScheduleDays.EVERYDAY
                R.id.chip_days_of_week -> viewModel.scheduleDaysLive.value = ScheduledMedicine.ScheduleDays.DAYS_OF_WEEK
                R.id.chip_interval_of_days -> viewModel.scheduleDaysLive.value = ScheduledMedicine.ScheduleDays.INTERVAL_OF_DAYS
            }
        }
        chip_group_schedule_days.check(R.id.chip_everyday)
    }

    private fun setupDoseHourRecyclerView() {
        recycler_view_schedule_hours.adapter = DoseHourAdapter().apply {
            viewModel.doseHourListLive.value?.let { doseHoursList ->
                setDoseHourList(doseHoursList)
            }
        }
        recycler_view_schedule_hours.layoutManager = LinearLayoutManager(context)
    }

    // Inner classes
    inner class DoseHourAdapter : RecyclerView.Adapter<DoseHourAdapter.DoseHourViewHolder>() {

        private var doseHourList = ArrayList<ScheduledMedicine.DoseHour>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoseHourViewHolder {
            val itemView = LayoutInflater.from(context).inflate(R.layout.recycler_item_dose_hour, parent, false)
            return DoseHourViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return doseHourList.size
        }

        override fun onBindViewHolder(holder: DoseHourViewHolder, position: Int) {
            val doseHour = doseHourList[position]
            holder.apply {
                chipHour.text = AppDateTimeUtil.timeToString(doseHour.time)
                chipHour.setOnClickListener {
                    openSelectTimeDialog(holder, doseHour)
                }

                txvMedicineType.text = viewModel.selectedMedicineTypeLive.value?.typeName ?: "brak typu"

                if (position == 0) {
                    btnDelete.visibility = View.GONE
                } else {
                    btnDelete.setOnClickListener {
                        removeDoseHour(position)
                    }
                }

                etxNumber.addTextChangedListener { editable ->
                    editable?.let { updateDoseSize(position, it) }
                }
                etxNumber.setOnFocusChangeListener { view, focused ->
                    if (!focused) {
                        val doseSizeString = etxNumber.text.toString()
                        if (doseSizeString.isEmpty() || doseSizeString.isBlank()) {
                            etxNumber.setText("1")
                        }
                    }
                }
            }
        }

        fun setDoseHourList(list: List<ScheduledMedicine.DoseHour>) {
            doseHourList.clear()
            doseHourList.addAll(list)
            notifyDataSetChanged()
        }

        fun addDoseHour() {
            doseHourList.add(itemCount, ScheduledMedicine.DoseHour())
            notifyItemInserted(itemCount)
            notifyViewModel()
        }

        private fun openSelectTimeDialog(holder: DoseHourViewHolder, doseHour: ScheduledMedicine.DoseHour) {
            val dialog = SelectTimeDialog()
            dialog.setTimeSelectedListener { time ->
                holder.chipHour.text = AppDateTimeUtil.timeToString(time)
                doseHour.time = time
                notifyViewModel()
            }
            dialog.selectedTime = doseHour.time
            dialog.show(childFragmentManager, SelectTimeDialog.TAG)
        }

        private fun removeDoseHour(position: Int) {
            doseHourList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            notifyViewModel()
        }

        private fun updateDoseSize(position: Int, editable: Editable) {
            val doseSizeString = editable.toString()
            if (doseSizeString.isNotBlank() && doseSizeString.isNotEmpty()) {
                doseHourList[position].doseSize = doseSizeString.toInt()
                notifyViewModel()
            }
        }

        private fun notifyViewModel() {
            viewModel.doseHourListLive.value = doseHourList
        }

        inner class DoseHourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val chipHour: Chip = itemView.findViewById(R.id.chip_hour)
            val etxNumber: TextInputEditText = itemView.findViewById(R.id.etx_number)
            val txvMedicineType: TextView = itemView.findViewById(R.id.txv_medicine_type)
            val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
        }
    }
}
