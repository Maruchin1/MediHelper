package com.maruchin.medihelper.presentation.feature.medikit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentFiltersBinding
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import com.maruchin.medihelper.presentation.utils.MedicinesSorter
import kotlinx.android.synthetic.main.fragment_filters.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FiltersFragment : BaseMainFragment<FragmentFiltersBinding>(R.layout.fragment_filters) {

    private val viewModel: MedicinesListViewModel by sharedViewModel(from = { requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChipGroupSortingParam()
        setupChipGroupSortingOrder()
    }

    private fun setupChipGroupSortingParam() {
        chip_group_sorting_order.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_alphabetical -> setSortingParam(MedicinesSorter.Param.ALPHABETICAL)
                R.id.chip_by_state -> setSortingParam(MedicinesSorter.Param.BY_STATE)
                R.id.chip_by_expire_date -> setSortingParam(MedicinesSorter.Param.BY_EXPIRE_DATE)
            }
        }
    }

    private fun setupChipGroupSortingOrder() {
        chip_group_sorting_order.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_asc -> setSortingOrder(MedicinesSorter.Order.ASC)
                R.id.chip_des -> setSortingOrder(MedicinesSorter.Order.DES)
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