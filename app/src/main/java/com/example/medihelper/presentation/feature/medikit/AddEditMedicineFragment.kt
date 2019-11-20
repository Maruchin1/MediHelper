package com.example.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentAddEditMedicineBinding
import com.example.medihelper.mainapp.dialog.SelectMedicineUnitDialog
import com.example.medihelper.mainapp.dialog.SelectExpireDialog
import kotlinx.android.synthetic.main.fragment_add_edit_medicine.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddEditMedicineFragment : AppFullScreenDialog() {
    private val TAG = "AddEditMedicineFragment"

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
    }

    private val viewModel: AddEditMedicineViewModel by viewModel()
    private val args: AddEditMedicineFragmentArgs by navArgs()

    fun onClickTakePhoto() {
        if (viewModel.isCameraPermissionGranted(requireContext())) {
            viewModel.capturePhoto(this)
        } else {
            viewModel.askForCameraPermission(requireActivity(),
                PERMISSION_REQUEST_CAMERA
            )
        }
    }

    fun onClickSelectExpireDate() = SelectExpireDialog().apply {
        defaultDate = viewModel.formModel.value?.expireDate
        setDateSelectedListener { viewModel.formModel.value?.expireDate = it }
    }.show(childFragmentManager)

    fun onClickSelectMedicineUnit() = SelectMedicineUnitDialog().apply {
        setMedicineUnitSelectedListener { viewModel.formModel.value?.unit = it }
    }.show(childFragmentManager)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentAddEditMedicineBinding>(
            inflater = inflater,
            layoutResId = R.layout.fragment_add_edit_medicine,
            container = container,
            viewModel = viewModel
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args)
        setupToolbar()
        setupTextFieldsFocusListener()
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
        viewModel.formModel.value?.packageSize = etx_package_size.text?.toString()?.toFloatOrNull()
    }

    private fun passCurrStateValueToViewModel() {
        viewModel.formModel.value?.currState = etx_curr_state.text?.toString()?.toFloatOrNull()
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.btn_save -> {
                    passPackageSizeValueToViewModel()
                    passCurrStateValueToViewModel()
                    val medicineSaved = viewModel.saveMedicine()
                    if (medicineSaved) {
                        dismiss()
                    }
                }
            }
            true
        }
    }
}
