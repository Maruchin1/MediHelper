package com.maruchin.medihelper.presentation.framework

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseRecyclerLiveAdapter<T>(
    layoutResId: Int,
    lifecycleOwner: LifecycleOwner,
    itemsSource: LiveData<List<T>>,
    areItemsTheSameFun: ((oldItem: T, newItem: T) -> Boolean)? = null
) : BaseRecyclerAdapter<T>(layoutResId, areItemsTheSameFun) {

    init {
        itemsSource.observe(lifecycleOwner, Observer { list ->
            super.updateItemsList(list)
        })
    }
}