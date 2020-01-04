package com.maruchin.medihelper.device.camera

import android.content.Context
import android.os.Environment
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import java.io.File

class DeviceCamera(private val context: Context) {

    val resultFile: LiveData<File>
        get() = _resultFile
    val actionPictureTaken: LiveData<Boolean>
        get() = _actionPictureTaken

    private val _resultFile = MutableLiveData<File>()
    private val _actionPictureTaken = ActionLiveData()

    private val aspectRatio = Rational(4, 3)
    private val resolution = Size(640, 480)
    private val flashMode = FlashMode.OFF

    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture

    fun bindCameraPreview(lifecycleOwner: LifecycleOwner, textureView: TextureView) {
        preview = Preview(getPreviewConfig())
        imageCapture = ImageCapture(getCaptureConfig())
        preview.setOnPreviewOutputUpdateListener { output ->
            updateTextureView(textureView, output)
        }
        CameraX.bindToLifecycle(lifecycleOwner, preview, imageCapture)
    }

    fun takePicture() {
        val tempFile = getTempImageFile()
        imageCapture.takePicture(tempFile, object : ImageCapture.OnImageSavedListener {
            override fun onImageSaved(file: File) {
                _resultFile.postValue(file)
                _actionPictureTaken.sendAction()
            }

            override fun onError(
                imageCaptureError: ImageCapture.ImageCaptureError,
                message: String,
                cause: Throwable?
            ) {
                _resultFile.postValue(null)
            }
        })
    }

    fun reset() {
        _resultFile.postValue(null)
    }

    private fun getPreviewConfig() = PreviewConfig.Builder()
        .setTargetAspectRatio(aspectRatio)
        .setTargetResolution(resolution)
        .build()

    private fun getCaptureConfig() = ImageCaptureConfig.Builder()
        .setTargetAspectRatio(aspectRatio)
        .setTargetResolution(resolution)
        .setFlashMode(flashMode)
        .build()

    private fun updateTextureView(textureView: TextureView, output: Preview.PreviewOutput) {
        val parent = textureView.parent as ViewGroup
        parent.removeView(textureView)
        parent.addView(textureView, 0)
        textureView.surfaceTexture = output.surfaceTexture
    }

    private fun getTempImageFile(): File {
        val externalPicturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("medicinePicture", ".jpg", externalPicturesDir).apply {
            deleteOnExit()
        }
    }
}