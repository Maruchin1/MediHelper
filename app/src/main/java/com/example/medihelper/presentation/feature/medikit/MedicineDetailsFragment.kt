package com.example.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.medihelper.presentation.dialogs.ConfirmDialog
import com.example.medihelper.R
import com.example.medihelper.custom.*
import com.example.medihelper.databinding.FragmentMedicineDetailsBinding
import com.example.medihelper.presentation.dialogs.SelectFloatNumberDialog
import com.example.medihelper.presentation.model.PersonItem
import kotlinx.android.synthetic.main.fragment_medicine_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicineDetailsFragment : AppFullScreenDialog() {

    private val viewModel: MedicineDetailsViewModel by viewModel()
    private val args: MedicineDetailsFragmentArgs by navArgs()
    private val directions by lazyOf(MedicineDetailsFragmentDirections)

    fun onClickTake() {
        val dialog = SelectFloatNumberDialog().apply {
            iconResID = R.drawable.ic_pill_black_36dp
            title = "Przyjmij dawkę leku"
            setNumberSelectedListener { number ->
                viewModel.takeMedicineDose(number)
            }
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }

    fun onClickEdit() {
        viewModel.selectedMedicineId.value?.let { medicineID ->
            findNavController().navigate(
                MedicineDetailsFragmentDirections.toAddEditMedicineFragment(
                    medicineID
                )
            )
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
        setupToolbar()
        setupPersonRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
       toolbar.setNavigationOnClickListener { dismiss() }
    }

    private fun observeViewModel() {
        viewModel.personItemListTakingMedicine.observe(viewLifecycleOwner, Observer { personItemList ->
            val adapter = recycler_view_persons.adapter as PersonAdapter
            adapter.updateItemsList(personItemList)
        })
    }

    private fun setupPersonRecyclerView() {
        recycler_view_persons.apply {
            adapter = PersonAdapter()
        }
    }

    inner class PersonAdapter : RecyclerAdapter<PersonItem>(
        layoutResId = R.layout.recycler_item_person_taking_medicine,
        areItemsTheSameFun = { oldItem, newItem -> oldItem.personId == newItem.personId }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            holder.bind(personItem, this@MedicineDetailsFragment)
        }
    }
}
