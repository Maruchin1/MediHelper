package com.example.medihelper.mainapp.addeditmedicineplan.durationtype


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.medihelper.R
import com.example.medihelper.custom.TAG
import com.example.medihelper.databinding.FragmentOnceBinding
import com.example.medihelper.dialogs.SelectDateDialog
import com.example.medihelper.mainapp.addeditmedicineplan.AddEditMedicinePlanViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class OnceFragment : Fragment() {

    private val viewModel: AddEditMedicinePlanViewModel by sharedViewModel(from = { parentFragment!! })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentOnceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_once, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun onClickSelectDate() {
        Log.d(TAG, "onClickSelectDate")
        val dialog = SelectDateDialog()
        dialog.defaultDate = viewModel.startDateLive.value
        dialog.setDateSelectedListener { date ->
            viewModel.startDateLive.value = date
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }
}
