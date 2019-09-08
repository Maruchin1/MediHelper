package com.example.medihelper.mainapp.medicines


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.medihelper.dialogs.ConfirmDialog
import com.example.medihelper.R
import com.example.medihelper.custom.*
import com.example.medihelper.databinding.FragmentMedicineDetailsBinding
import com.example.medihelper.dialogs.SelectFloatNumberDialog
import com.example.medihelper.localdatabase.pojos.PersonItem
import kotlinx.android.synthetic.main.fragment_medicine_details.*


class MedicineDetailsFragment : AppFullScreenDialog() {

    private val viewModel: MedicineDetailsViewModel by activityViewModels()
    private val args: MedicineDetailsFragmentArgs by navArgs()
    private val directions by lazyOf(MedicineDetailsFragmentDirections)

    fun onClickTake() {
        val dialog = SelectFloatNumberDialog().apply {
            iconResID = R.drawable.ic_pill_black_36dp
            title = "Zażyj dawkę leku"
            setNumberSelectedListener { number ->
                viewModel.takeMedicineDose(number)
            }
        }
        dialog.show(childFragmentManager, dialog.TAG)
    }

    fun onClickEdit() {
        viewModel.selectedMedicineIDLive.value?.let { medicineID ->
            findNavController().navigate(directions.toAddEditMedicineFragment(medicineID))
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
        val binding: FragmentMedicineDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_medicine_details, container, false)
        binding.viewModel = viewModel
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setTransparentStatusBar()
        setupToolbar()
        setupPersonRecyclerView()
        observeViewModel()
    }

    private fun setTransparentStatusBar() {
        context?.run {
            dialog?.window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    private fun setupToolbar() {
       toolbar.setNavigationOnClickListener { dismiss() }
    }

    private fun observeViewModel() {
        viewModel.personItemListTakingMedicineLive.observe(viewLifecycleOwner, Observer { personItemList ->
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
        R.layout.recycler_item_person_taking_medicine,
        object : DiffCallback<PersonItem>() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].personID == newList[newItemPosition].personID
            }
        }
    ) {
        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
            val personItem = itemsList[position]
            holder.bind(personItem, this@MedicineDetailsFragment)
        }
    }
}
