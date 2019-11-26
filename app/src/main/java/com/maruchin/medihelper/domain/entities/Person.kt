package com.maruchin.medihelper.domain.entities

data class Person(
    val personId: Int,
    val name: String,
    val colorId: Int,
    val mainPerson: Boolean,
    val connectionKey: String?
)