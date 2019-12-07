package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.presentation.dialogs.ConfirmDialog
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentMedicineDetailsBinding
import com.maruchin.medihelper.domain.model.ProfileSimpleItem
import com.maruchin.medihelper.presentation.framework.*
import kotlinx.android.synthetic.main.fragment_medicine_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicineDetailsFragment : AppFullScreenDialog() {

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
                this@MedicineDetailsFragment.dismiss()
            }
        }
        dialog.show(childFragmentManager, ConfirmDialog.TAG)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentMedicineDetailsBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_medicine_details,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setTransparentStatusBar()
        setupPersonRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.profileSimpleItemList.observe(viewLifecycleOwner, Observer { list ->
            val adapter = recycler_view_persons.adapter as PersonAdapter
            adapter.updateItemsList(list)
        })
    }

    private fun setupPersonRecyclerView() {
        recycler_view_persons.apply {
            adapter = PersonAdapter()
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
