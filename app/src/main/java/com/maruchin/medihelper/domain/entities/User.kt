package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity

data class User(
    override val entityId: String,
    val email: String
) : BaseEntity()