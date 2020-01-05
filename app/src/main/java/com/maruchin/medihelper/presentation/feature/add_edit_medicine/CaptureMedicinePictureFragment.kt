package com.maruchin.medihelper.presentation.feature.add_edit_medicine

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentCaptureMedicinePictureBinding
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import com.maruchin.medihelper.device.camera.DeviceCamera
import kotlinx.android.synthetic.main.fragment_capture_medicine_picture.*
import org.koin.android.ext.android.inject

class CaptureMedicinePictureFragment :
    BaseMainFragment<FragmentCaptureMedicinePictureBinding>(R.layout.fragment_capture_medicine_picture) {

    private val deviceCamera: DeviceCamera by inject()

    fun onClickTakePicture() {
        deviceCamera.takePicture()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindCameraPreview()
        setupToolbarNavigation()
        setBlackStatusBar()
        observePictureTaken()
    }

    private fun bindCameraPreview() {
        deviceCamera.bindCameraPreview(viewLifecycleOwner, texture_view)
    }

    private fun setupToolbarNavigation() {
        val navController = findNavController()
        toolbar.setupWithNavController(navController)
    }

    private fun setBlackStatusBar() {
        super.setStatusBarColor(R.color.colorBlack)
    }

    private fun observePictureTaken() {
        deviceCamera.actionPictureTaken.observe(viewLifecycleOwner, Observer {
            onPictureTaken()
        })
    }

    private fun onPictureTaken() {
        findNavController().popBackStack()
    }
}