package com.maruchin.medihelper.presentation.feature.medikit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
        deviceCamera.bindCameraPreview(viewLifecycleOwner, texture_view)
        super.setupToolbarNavigation()
        super.setStatusBarColor(R.color.colorBlack)

        deviceCamera.actionPictureTaken.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }
}