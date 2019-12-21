package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity

data class Profile(
    override val entityId: String,
    val name: String,
    val color: String,
    val mainPerson: Boolean
) : BaseEntity()