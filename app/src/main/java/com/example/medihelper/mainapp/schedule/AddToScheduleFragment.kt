package com.example.medihelper.mainapp.schedule


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentAddToScheduleBinding
import com.example.medihelper.mainapp.MainActivity
import com.example.medihelper.mainapp.schedule.dosetimeOptions.DoseTimeEverydayFragment
import com.example.medihelper.mainapp.schedule.dosetimeOptions.DoseTimeIntervalFragment
import com.example.medihelper.mainapp.schedule.dosetimeOptions.DoseTimeSingleFragment
import com.example.medihelper.mainapp.schedule.dosetimeOptions.DoseTimeWeekDaysFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_to_schedule.*


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
        setupDoseTimeChipGroup()
        setupDoseHourChipGroup()
        setupDoseHourRecyclerView()
        setupDoseHourEditText()
        observeViewModel()
    }

    fun onClickSelectMedicine(view: View) {
        val dialog = SelectMedicineDialogFragment()
        dialog.show(childFragmentManager, SelectMedicineDialogFragment.TAG)
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
                    hide(true)
                }
            }
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.option_confirm) {
                viewModel.saveToSchedule(
                    chip_group_dose_days.checkedChipId,
                    chip_group_dose_hours.checkedChipId
                )
                navController.popBackStack()
                true
            } else {
                false
            }
        }
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
        viewModel.doseHoursListLive.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (recycler_view_dose_hours.adapter as DoseHourAdapter).setDoseHoursList(it)
            }
        })
    }

    private fun setupDoseHourRecyclerView() {
        context?.run {
            recycler_view_dose_hours.adapter = DoseHourAdapter(this, viewModel)
            recycler_view_dose_hours.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun setupDoseTimeChipGroup() {
        val singleFragment = DoseTimeSingleFragment()
        val everydayFragment = DoseTimeEverydayFragment()
        val weekDaysFragment = DoseTimeWeekDaysFragment()
        val intervalFragment = DoseTimeIntervalFragment()
        chip_group_dose_days.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_single -> changeDoseTimeFragment(singleFragment)
                R.id.chip_everyday -> changeDoseTimeFragment(everydayFragment)
                R.id.chip_week_days -> changeDoseTimeFragment(weekDaysFragment)
                R.id.chip_interval -> changeDoseTimeFragment(intervalFragment)
            }
        }
        chip_group_dose_days.check(R.id.chip_single)
    }

    private fun setupDoseHourChipGroup() {
        chip_group_dose_hours.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip_several_times -> {
                    txv_before_number.visibility = View.GONE
                    txv_after_number.text = "razy dziennie"
                }
                R.id.chip_hours_interval -> {
                    txv_before_number.visibility = View.VISIBLE
                    txv_after_number.text = "godzin"
                }
            }
        }
        chip_group_dose_hours.check(R.id.chip_several_times)
    }

    private fun setupDoseHourEditText() {
        etx_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "onTextChanged")
                var number = 0
                try {
                    number = s.toString().toInt()
                } catch (e: Exception) {
                    Toast.makeText(context, "Zła wartość", Toast.LENGTH_LONG).show()
                }
                when (chip_group_dose_hours.checkedChipId) {
                    R.id.chip_several_times -> viewModel.changeDoseHoursListSeveralTimes(number)
                    R.id.chip_hours_interval -> viewModel.changeDoseHoursListHoursInterval(
                        number
                    )
                }
            }
        })
    }

    private fun changeDoseTimeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_dose_time_options, fragment)
            .commit()
    }
}
