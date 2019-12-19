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
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.framework.RecyclerAdapter
import com.maruchin.medihelper.presentation.framework.RecyclerItemViewHolder
import kotlinx.android.synthetic.main.fragment_medicine_plan_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicinePlanDetailsFragment :
    BaseFragment<FragmentMedicinePlanDetailsBinding>(R.layout.fragment_medicine_plan_details) {

    private val viewModel: MedicinePlanDetailsViewModel by viewModel()
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

        setupTimeDoseRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            startPostponedEnterTransition()
            super.setLightStatusBar(true)
        })
    }

    private fun setupTimeDoseRecyclerView() {
        recycler_view_time_dose.apply {
            adapter = TimeDoseAdapter()
        }
    }

    private inner class TimeDoseAdapter : RecyclerAdapter<TimeDose>(
        layoutResId = R.layout.rec_item_time_dose,
        itemsSource = viewModel.timesDoses,
        lifecycleOwner = viewLifecycleOwner
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val item = itemsList[position]
            holder.bind(displayData = item, handler = this@MedicinePlanDetailsFragment, viewModel = viewModel)
        }
    }
}