package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.Person

data class PersonItem(
    val personId: Int,
    val name: String,
    val colorId: Int,
    val mainPerson: Boolean
) {
    constructor(person: Person) : this(
        personId = person.personId,
        name = person.name,
        colorId = person.colorId,
        mainPerson = person.mainPerson
    )
}