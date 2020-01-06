package com.maruchin.medihelper.presentation.framework

abstract class BaseRecyclerConstAdapter<T>(
    layoutResId: Int,
    items: List<T>,
    areItemsTheSameFun: ((oldItem: T, newItem: T) -> Boolean)? = null
) : BaseRecyclerAdapter<T>(layoutResId, areItemsTheSameFun) {

    init {
        super.updateItemsList(items)
    }
}