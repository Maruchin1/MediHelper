package com.maruchin.medihelper.data.repositories

import com.maruchin.medihelper.data.framework.FirestoreTypeRepo
import com.maruchin.medihelper.data.mappers.TypeContainerMapper
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo

class MedicineUnitRepoImpl(
    appFirebase: AppFirebase,
    mapper: TypeContainerMapper
) : MedicineUnitRepo, FirestoreTypeRepo(
    documentRef = appFirebase.medicineUnits,
    mapper = mapper
)