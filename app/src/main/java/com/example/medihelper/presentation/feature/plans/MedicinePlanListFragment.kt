package com.example.medihelper.presentation.feature.plans


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.custom.RecyclerAdapter
import com.example.medihelper.custom.RecyclerItemViewHolder
import com.example.medihelper.databinding.FragmentMedicinePlanListBinding
import com.example.medihelper.domain.entities.MedicinePlanType
import com.example.medihelper.presentation.dialogs.ConfirmDialog
import com.example.medihelper.presentation.model.MedicinePlanItem
import kotlinx.android.synthetic.main.fragment_medicine_plan_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MedicinePlanListFragment : Fragment() {

    var medicinePlanType: MedicinePlanType? = null
        set(value) {
            field = value
            when (medicinePlanType) {
                MedicinePlanType.ENDED -> unavailableMessage = "Brak zakończonych planów"
                MedicinePlanType.ONGOING -> unavailableMessage = "Brak trwających planów"
            }
        }
    var unavailableMessage = ""
    val medicinePlanAvailableLive = MutableLiveData(false)
    private val viewModel: MedicinePlanListViewModel by sharedViewModel(from = { parentFragment!! })
    private val directions by lazyOf(MedicinePlanListHostFragmentDirections)

    fun onClickMedicinePlanHistory(medicinePlanID: Int) =
        findNavController().navigate(directions.toMedicinePlaHistoryDialog(medicinePlanID))

    fun onClickEditMedicinePlan(medicinePlanID: Int) =
        findNavController().navigate(directions.toAddEditMedicinePlanFragment(medicinePlanID))

    fun onClickDeleteMedicinePlan(medicinePlanID: Int) {
        val dialog = ConfirmDialog().apply {
            title = "Usuń lek z planu"
            message = "Wybrany lek zostanie usunięty z planu. Czy chcesz kontynuować?"
            iconResId = R.drawable.round_delete_black_36
            setOnConfirmClickListener {
                viewModel.deleteMedicinePlan(medicinePlanID)
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMedicinePlanListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_plan_list, container, false)
        binding.handler = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        medicinePlanType?.let {
            val medicinePlanItemList = when (medicinePlanType) {
                MedicinePlanType.ONGOING -> viewModel.medicinePlanItemOngoingList
                MedicinePlanType.ENDED -> viewModel.medicinePlanItemEndedList
                else -> null
            }
            medicinePlanItemList?.observe(viewLifecycleOwner, Observer { medicinePlanList ->
                val adapter = recycler_view_medicine_plan.adapter as MedicinePlanAdapter
                adapter.updateItemsList(medicinePlanList)
                medicinePlanAvailableLive.value = !medicinePlanList.isNullOrEmpty()
            })
        }
    }

    private fun setupRecyclerView() {
        recycler_view_medicine_plan.adapter = MedicinePlanAdapter()
    }

    // Inner classes
    inner class MedicinePlanAdapter : RecyclerAdapter<MedicinePlanItem>(
        layoutResId = R.layout.recycler_item_medicine_plan,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.medicinePlanId == newItem.medicinePlanId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val medicinePlanItem = itemsList[position]
            holder.bind(medicinePlanItem, this@MedicinePlanListFragment)
        }
    }
}
