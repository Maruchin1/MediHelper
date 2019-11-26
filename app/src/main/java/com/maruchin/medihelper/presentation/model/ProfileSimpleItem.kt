package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase

data class ProfileSimpleItem(
    val profileId: String,
    val name: String,
    val color: String
) {
    constructor(data: GetMedicineDetailsUseCase.ProfileSimpleItem) : this(
        profileId = data.profileId,
        name = data.name,
        color = data.color
    )
}