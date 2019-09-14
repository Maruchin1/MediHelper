package com.example.medihelper.mainapp.addeditmedicineplan.durationtype


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.medihelper.R
import com.example.medihelper.databinding.FragmentPeriodBinding
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.mainapp.addeditmedicineplan.AddEditMedicinePlanViewModel
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
        val dialog = SelectDateDialog()
        selectedDateLive?.let { dialog.defaultDate = it.value }
        dialog.setDateSelectedListener { date ->
            selectedDateLive?.value = date
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }
}
