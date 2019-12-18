package com.maruchin.medihelper.presentation.feature.mediplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinePlanDetailsBinding
import com.maruchin.medihelper.presentation.framework.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicinePlanDetailsFragment :
    BaseFragment<FragmentMedicinePlanDetailsBinding>(R.layout.fragment_medicine_plan_details) {

    private val viewModel: MedicinePlanDetailViewModel by viewModel()
    private val args: MedicinePlanDetailsFragmentArgs by navArgs()
    private val directions by lazy { MedicinePlanDetailsFragmentDirections }

    fun onClickOpenMedicineDetails() {
        findNavController().navigate(directions.toMedicineDetailsFragment(viewModel.medicineId))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        super.setupToolbarNavigation()
        viewModel.setArgs(args)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            startPostponedEnterTransition()
            super.setLightStatusBar(true)
        })
    }
}