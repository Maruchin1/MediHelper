package com.maruchin.medihelper.presentation.feature.medicines_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicinesFiltersBinding
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import kotlinx.android.synthetic.main.fragment_medicines_filters.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MedicinesFiltersFragment : BaseMainFragment<FragmentMedicinesFiltersBinding>(R.layout.fragment_medicines_filters) {

    private val viewModel: MedicinesListViewModel by sharedViewModel(from = { requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSortingParamSelection()
        setupSortingOrderSelection()
        setupFilterStateSelection()
    }

    private fun setupSortingParamSelection() {
        chip_group_sorting_order.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_alphabetical -> setSortingParam(MedicinesSorter.Param.ALPHABETICAL)
                R.id.chip_by_state -> setSortingParam(MedicinesSorter.Param.BY_STATE)
                R.id.chip_by_expire_date -> setSortingParam(MedicinesSorter.Param.BY_EXPIRE_DATE)
            }
        }
    }

    private fun setupSortingOrderSelection() {
        chip_group_sorting_order.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_asc -> setSortingOrder(MedicinesSorter.Order.ASC)
                R.id.chip_des -> setSortingOrder(MedicinesSorter.Order.DES)
            }
        }
    }

    private fun setupFilterStateSelection() {
        listOf(
            chip_state_empty,
            chip_state_small,
            chip_state_medium,
            chip_state_good
        ).forEach { chip ->
            chip.setOnCheckedChangeListener { _, _ ->
                val checkedIds = chip_group_filter_state.checkedChipIds
                viewModel.setFilterStateByChipsIds(checkedIds)
            }
        }
    }

    private fun setSortingParam(param: MedicinesSorter.Param) {
        viewModel.sortingParam.value = param
    }

    private fun setSortingOrder(order: MedicinesSorter.Order) {
        viewModel.sortingOrder.value = order
    }
}