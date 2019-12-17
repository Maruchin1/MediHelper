package com.maruchin.medihelper.presentation.feature.mediplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinePlanDetailsBinding
import com.maruchin.medihelper.presentation.framework.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicinePlanDetailsFragment :
    BaseFragment<FragmentMedicinePlanDetailsBinding>(R.layout.fragment_medicine_plan_details) {

    private val viewModel: MedicinePlanDetailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.setupToolbarNavigation()
        super.setLightStatusBar(true)
    }
}