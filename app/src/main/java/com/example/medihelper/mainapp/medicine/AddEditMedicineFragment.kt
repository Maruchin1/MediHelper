package com.example.medihelper.mainapp.medicine


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.medihelper.R
import com.example.medihelper.custom.AppFullScreenDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentAddEditMedicineBinding
import com.example.medihelper.mainapp.dialog.SelectMedicineUnitDialog
import com.example.medihelper.mainapp.dialog.SelectMonthDateDialog
import kotlinx.android.synthetic.main.fragment_add_edit_medicine.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class AddEditMedicineFragment : AppFullScreenDialog() {
    private val TAG = "AddEditMedicineFragment"

    private val viewModel: AddEditMedicineViewModel by viewModel()
    private val args: AddEditMedicineFragmentArgs by navArgs()

    fun onClickTakePhoto() {
        val intent = viewModel.takePhotoIntent(requireActivity())
        startActivity(intent)
    }

    fun onClickSelectExpireDate() = SelectMonthDateDialog().apply {
        defaultDate = viewModel.expireDateLive.value
        setDateSelectedListener { viewModel.expireDateLive.value = it }
    }.show(childFragmentManager)

    fun onClickSelectMedicineUnit() = SelectMedicineUnitDialog().apply {
        setMedicineUnitSelectedListener { viewModel.medicineUnitLive.value = it }
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
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.imageFileLive.observe(viewLifecycleOwner, Observer { photoFile ->
            Log.d(TAG, "photoFile change = $photoFile")
            setPhotoImage(photoFile)
        })
    }

    private fun setPhotoImage(photoFile: File?) {
        if (photoFile != null) {
            Glide.with(this)
                .load(photoFile)
                .centerCrop()
                .into(img_photo)
        }
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
        viewModel.packageSizeLive.value = etx_package_size.text?.toString()?.toFloatOrNull()
    }

    private fun passCurrStateValueToViewModel() {
        viewModel.currStateLive.value = etx_curr_state.text?.toString()?.toFloatOrNull()
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
