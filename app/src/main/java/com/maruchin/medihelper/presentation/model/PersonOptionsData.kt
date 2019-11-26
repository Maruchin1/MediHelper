package com.maruchin.medihelper.presentation.model

import android.graphics.Bitmap

data class PersonOptionsData(
    val name: String,
    val colorId: Int,
    val connectionKey: String?,
    val connectionKeyQrCode: Bitmap?
)
