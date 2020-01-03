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
import com.maruchin.medihelper.device.camera.CameraPermission
import com.maruchin.medihelper.presentation.dialogs.SelectExpireDateDialog
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import com.maruchin.medihelper.presentation.framework.shrinkOnScroll
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_add_edit_medicine.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddEditMedicineFragment : BaseMainFragment<FragmentAddEditMedicineBinding>(R.layout.fragment_add_edit_medicine) {
    private val TAG = "AddEditMedicineFragment"

    private val viewModel: AddEditMedicineViewModel by viewModel()
    private val args: AddEditMedicineFragmentArgs by navArgs()
    private val loadingScreen: LoadingScreen by inject()
    private val cameraPermission: CameraPermission by inject()

    fun onClickTakePhoto() {
        if (cameraPermission.isGranted()) {
            navigateToCaptureMedicinePictureFragment()
        } else {
            cameraPermission.askForCameraPermission()
        }
    }

    fun onClickSelectExpireDate() = SelectExpireDateDialog(
        defaultDate = viewModel.expireDate.value
    ).apply {
        setOnDateSelectedListener { selectedExpireDate ->
            viewModel.expireDate.value = selectedExpireDate
        }
    }.show(childFragmentManager)

    fun onClickSave() {
        viewModel.saveMedicine()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setBindingViewModel()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        bindLoadingScreen()
        disableLightStatusBar()
        setupToolbarNavigation()
        setupFabSaveScrollBehavior()
        observeViewModel()
    }

    private fun navigateToCaptureMedicinePictureFragment() {
        val direction = AddEditMedicineFragmentDirections.toCaptureMedicinePictureFragment()
        findNavController().navigate(direction)
    }

    private fun setBindingViewModel() {
        super.bindingViewModel = viewModel
    }

    private fun initViewModel() {
        viewModel.initViewModel(args.editMedicineId)
    }

    private fun bindLoadingScreen() {
        loadingScreen.bind(this, viewModel.loadingInProgress)
    }

    private fun disableLightStatusBar() {
        super.setLightStatusBar(false)
    }

    private fun setupToolbarNavigation() {
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
    }

    private fun setupFabSaveScrollBehavior() {
        fab_save.shrinkOnScroll(items_root)
    }

    private fun observeViewModel() {
        viewModel.actionMedicineSaved.observe(viewLifecycleOwner, Observer {
            onMedicineSaved()
        })
    }

    private fun onMedicineSaved() {
        findNavController().popBackStack()
    }
}
