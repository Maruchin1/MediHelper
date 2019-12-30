package com.maruchin.medihelper.domain.framework

abstract class BaseValidator<P, E : BaseErrors> {

    abstract fun validate(params: P): E
}