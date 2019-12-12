package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicineDetailsBinding
import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_medicine_details.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicineDetailsFragment : BaseFragment<FragmentMedicineDetailsBinding>(R.layout.fragment_medicine_details) {

    companion object {
        private const val TRANSITION_DURATION = 300L
    }

    private val viewModel: MedicineDetailsViewModel by viewModel()
    private val args: MedicineDetailsFragmentArgs by navArgs()
    private val directions by lazyOf(MedicineDetailsFragmentDirections)

    fun onClickEdit() {
        viewModel.medicineId?.let { medicineId ->
            findNavController().navigate(directions.toAddEditMedicineFragment(medicineId))
        }
    }

    fun onClickDelete() {
        val dialog = ConfirmDialog().apply {
            title = "Usuń lek"
            message = "Wybrany lek zostanie usunięty. Czy chcesz kontynuować?"
            iconResId = R.drawable.round_delete_black_36
            setOnConfirmClickListener {
                viewModel.deleteMedicine()
                findNavController().popBackStack()
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    fun onClickOpenMedicineInfo() {
        viewModel.medicineName.value?.let {
            findNavController().navigate(directions.toMedicineInfo(it))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = PhotoTransition().apply {
            duration = TRANSITION_DURATION
        }
        sharedElementReturnTransition = PhotoTransition().apply {
            duration = TRANSITION_DURATION
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)

        viewModel.setArgs(args)
        super.setupToolbarNavigation()
        setupToolbarMenu()
        setupPersonRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
//        viewModel.profileSimpleItemList.observe(viewLifecycleOwner, Observer { list ->
//            val adapter = recycler_view_persons.adapter as PersonAdapter
//            adapter.updateItemsList(list)
//        })
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            (view?.parent as? ViewGroup)?.doOnPreDraw {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(100L)
                    super.setLightStatusBar(true)
                }
                startPostponedEnterTransition()
            }
        })
    }

    private fun setupPersonRecyclerView() {
//        recycler_view_persons.apply {
//            adapter = PersonAdapter()
//        }
    }

    private fun setupToolbarMenu() {
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_edit -> onClickEdit()
                R.id.btn_delete -> onClickDelete()
            }
            return@setOnMenuItemClickListener true
        }
    }

    inner class PersonAdapter : RecyclerAdapter<ProfileSimpleItem>(
        layoutResId = R.layout.recycler_item_person_taking_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.profileId == newItem.profileId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            holder.bind(personItem, this@MedicineDetailsFragment)
        }
    }
}
