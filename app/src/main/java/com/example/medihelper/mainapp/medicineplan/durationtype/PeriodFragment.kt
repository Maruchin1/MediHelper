package com.example.medihelper.mainapp.medicineplan.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentPeriodBinding
import com.example.medihelper.mainapp.dialog.SelectDateDialog
import com.example.medihelper.mainapp.medicineplan.AddEditMedicinePlanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PeriodFragment : Fragment() {

    private val viewModel: AddEditMedicinePlanViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPeriodBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_period, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun onClickSelectDate(view: View) {
        val selectedDateLive = when (view.id) {
            R.id.etx_start_date -> viewModel.startDateLive
            R.id.etx_end_date -> viewModel.endDateLive
            else -> null
        }
        SelectDateDialog().apply {
            selectedDateLive?.let { defaultDate = it.value }
            setDateSelectedListener { date ->
                selectedDateLive?.value = date
            }
            viewModel.colorPrimaryLive.value?.let { setColorPrimary(it) }
        }.show(requireParentFragment().childFragmentManager)
    }
}
