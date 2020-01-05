package com.maruchin.medihelper.presentation.feature.medicine_details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Transition
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
    private val loadingScreen: LoadingScreen by inject()

    fun onClickEdit() {
        val medicineId = viewModel.medicineId
        val direction = MedicineDetailsFragmentDirections.toAddEditMedicineFragment(medicineId)
        findNavController().navigate(direction)
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
        viewModel.data.value?.medicineName?.let { medicineName ->
            val direction = MedicineDetailsFragmentDirections.toMedicineInfo(medicineName)
            findNavController().navigate(direction)
        }
    }

    fun onClickScheduleMedicine() {
        SelectProfileDialog().apply {
            setOnProfileSelectedListener { profileId ->
                navigateToAddMedicinePlanFragment(profileId)
            }
        }.show(childFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSharedElementTransitions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        bindLoadingScreen()
        setupToolbarNavigation()
        setupToolbarMenu()
        observeViewModel()
    }

    private fun setupSharedElementTransitions() {
        val transition = getSharedElementTransition()
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    private fun getSharedElementTransition(): Transition {
        return PhotoTransition().apply {
            duration = TRANSITION_DURATION
        }
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun initViewModel() {
        viewModel.initViewModel(args.medicineId)
    }

    private fun bindLoadingScreen() {
        loadingScreen.bind(this, viewModel.loadingInProgress)
    }

    private fun setupToolbarNavigation() {
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
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

    private fun observeViewModel() {
        viewModel.actionDataLoaded.observe(viewLifecycleOwner, Observer {
            onDataLoaded()
        })
        viewModel.actionMedicineDeleted.observe(viewLifecycleOwner, Observer {
            onMedicineDeleted()
        })
    }

    private fun onDataLoaded() {
        (view?.parent as? ViewGroup)?.doOnPreDraw {
            setDelayedLightStatusBar()
            startPostponedEnterTransition()
        }
    }

    private fun setDelayedLightStatusBar() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(100L)
            super.setLightStatusBar(true)
        }
    }

    private fun onMedicineDeleted() {
        findNavController().popBackStack()
    }

    private fun navigateToAddMedicinePlanFragment(profileId: String) {
        val direction = MedicineDetailsFragmentDirections.toAddEditMedicinePlanFragment(
            profileId = profileId,
            medicineId = viewModel.medicineId,
            medicinePlanId = null
        )
        findNavController().navigate(direction)
    }
}
