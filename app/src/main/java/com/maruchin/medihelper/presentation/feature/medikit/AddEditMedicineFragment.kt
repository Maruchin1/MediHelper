package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditMedicineBinding
import com.maruchin.medihelper.presentation.dialogs.SelectMedicineUnitDialog
import com.maruchin.medihelper.presentation.dialogs.SelectExpireDateDialog
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.framework.shrinkOnScroll
import kotlinx.android.synthetic.main.fragment_add_edit_medicine.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddEditMedicineFragment : BaseFragment<FragmentAddEditMedicineBinding>(R.layout.fragment_add_edit_medicine) {
    private val TAG = "AddEditMedicineFragment"

    private val viewModel: AddEditMedicineViewModel by viewModel()
    private val args: AddEditMedicineFragmentArgs by navArgs()

    fun onClickTakePhoto() {
        viewModel.capturePhoto()
    }

    fun onClickSelectExpireDate() = SelectExpireDateDialog().apply {
        defaultDate = viewModel.expireDate.value
        setDateSelectedListener { viewModel.expireDate.value = it }
    }.show(childFragmentManager)

    fun onClickSelectMedicineUnit() = SelectMedicineUnitDialog().apply {
        setMedicineUnitSelectedListener { viewModel.medicineUnit.value = it }
    }.show(childFragmentManager)

    fun onClickSave() {
        viewModel.saveMedicine()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.bindingViewModel = viewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupToolbar()
        setupTextFieldsFocusListener()
        setupScrollView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionMedicineSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private fun setupScrollView() {
        fab_save.shrinkOnScroll(items_root)
    }

    private fun setupTextFieldsFocusListener() {
        etx_package_size.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                passPackageSizeValueToViewModel()
            }
        }
        etx_curr_state.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                passCurrStateValueToViewModel()
            }
        }
    }

    private fun passPackageSizeValueToViewModel() {
        viewModel.packageSize.value = etx_package_size.text?.toString()?.toFloatOrNull()
    }

    private fun passCurrStateValueToViewModel() {
        viewModel.currState.value = etx_curr_state.text?.toString()?.toFloatOrNull()
    }

    private fun setupToolbar() {
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
    }
}
