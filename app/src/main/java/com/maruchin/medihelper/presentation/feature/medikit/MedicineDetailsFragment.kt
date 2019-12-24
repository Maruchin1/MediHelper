package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
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
import com.maruchin.medihelper.presentation.dialogs.SelectProfileDialog
import com.maruchin.medihelper.presentation.framework.*
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_medicine_details.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedicineDetailsFragment : BaseMainFragment<FragmentMedicineDetailsBinding>(R.layout.fragment_medicine_details) {

    companion object {
        private const val TRANSITION_DURATION = 300L
    }

    private val viewModel: MedicineDetailsViewModel by viewModel()
    private val args: MedicineDetailsFragmentArgs by navArgs()
    private val directions by lazyOf(MedicineDetailsFragmentDirections)
    private val loadingScreen: LoadingScreen by inject()

    fun onClickEdit() {
        viewModel.medicineId.let { medicineId ->
            findNavController().navigate(directions.toAddEditMedicineFragment(medicineId))
        }
    }

    fun onClickDelete() {
        ConfirmDialog(
            title = "Usuń lek",
            message = "Wybrany lek zostanie usunięty. Czy chcesz kontynuować?",
            iconResId = R.drawable.round_delete_black_36
        ).apply {
            setOnConfirmClickListener {
                viewModel.deleteMedicine()
            }
        }.show(childFragmentManager)
    }

    fun onClickOpenMedicineInfo() {
        viewModel.medicineName.value?.let {
            findNavController().navigate(directions.toMedicineInfo(it))
        }
    }

    fun onClickScheduleMedicine() {
        SelectProfileDialog().apply {
            setOnProfileSelectedListener { profileId ->
                viewModel.medicineId?.let { medicineId ->
                    findNavController().navigate(
                        directions.toAddEditMedicinePlanFragment(
                            profileId = profileId,
                            medicineId = medicineId,
                            medicinePlanId = null
                        )
                    )
                }
            }
        }.show(childFragmentManager)
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
        loadingScreen.bind(this, viewModel.loadingInProgress)
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
        viewModel.actionMedicineDeleted.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
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

//    inner class PersonAdapter : RecyclerAdapter<ProfileItem>(
//        layoutResId = R.layout.rec_item_person_taking_medicine,
//        lifecycleOwner = viewLifecycleOwner,
//        itemsSource = viewModel.
//        areItemsTheSameFun = { oldItem, newItem -> oldItem.profileId == newItem.profileId }
//    ) {
//        override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
//            val personItem = itemsList[position]
//            holder.bind(personItem, this@MedicineDetailsFragment)
//        }
//    }
}
