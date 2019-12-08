package com.maruchin.medihelper.presentation.feature.medikit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentAddEditMedicineBinding
import com.maruchin.medihelper.presentation.dialogs.SelectExpireDateDialog
import com.maruchin.medihelper.presentation.framework.BaseFragment
import com.maruchin.medihelper.presentation.framework.shrinkOnScroll
import com.maruchin.medihelper.presentation.utils.LoadingScreen
import kotlinx.android.synthetic.main.fragment_add_edit_medicine.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddEditMedicineFragment : BaseFragment<FragmentAddEditMedicineBinding>(R.layout.fragment_add_edit_medicine) {
    private val TAG = "AddEditMedicineFragment"

    private val viewModel: AddEditMedicineViewModel by viewModel()
    private val args: AddEditMedicineFragmentArgs by navArgs()
    private val loadingScreen: LoadingScreen by inject()
    private val directions by lazyOf(AddEditMedicineFragmentDirections)

    fun onClickTakePhoto() {
        findNavController().navigate(directions.toCaptureMedicinePictureFragment())
    }

    fun onClickSelectExpireDate() = SelectExpireDateDialog().apply {
        defaultDate = viewModel.expireDate.value
        setDateSelectedListener { viewModel.expireDate.value = it }
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
        super.setLightStatusBar(false)
        super.setupToolbarNavigation()
        setupScrollView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.actionMedicineSaved.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
        viewModel.loadingInProgress.observe(viewLifecycleOwner, Observer { inProgress ->
            if (inProgress) {
                loadingScreen.showLoadingScreen(childFragmentManager)
            } else {
                loadingScreen.closeLoadingScreen()
            }
        })
    }

    private fun setupScrollView() {
        fab_save.shrinkOnScroll(items_root)
    }
}
