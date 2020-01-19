package com.maruchin.medihelper.data.repositories

import com.maruchin.medihelper.data.framework.FirestoreTypeRepo
import com.maruchin.medihelper.data.mappers.TypeContainerMapper
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo

class MedicineTypeRepoImpl(
    appFirebase: AppFirebase,
    mapper: TypeContainerMapper
) : MedicineTypeRepo, FirestoreTypeRepo(
    documentRef = appFirebase.getMedicineTypesDocument(),
    mapper = mapper
)