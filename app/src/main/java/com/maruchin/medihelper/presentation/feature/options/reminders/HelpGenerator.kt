package com.maruchin.medihelper.presentation.feature.options.reminders

import android.content.Context
import android.content.res.Resources

abstract class HelpGenerator(
    private val context: Context
) {

    private val res: Resources by lazy { context.resources }
    private val headers: List<String> by lazy {
        val headersIds = generateHeaders()
        getStringFromResources(headersIds)
    }
    private val bodiesIds: List<String> by lazy {
        val bodiesIds = generateBodies()
        getStringFromResources(bodiesIds)
    }

    protected abstract fun generateHeaders(): List<Int>

    protected abstract fun generateBodies(): List<Int>

    @Throws(DifferentNumberOfHeadersAndBodies::class)
    fun generate(): List<HelpItemData> {
        checkData()
        return generateItems()
    }

    private fun getStringFromResources(ids: List<Int>): List<String> {
        return ids.map { res.getString(it) }
    }

    private fun checkData() {
        val numOfHeaders = headers.size
        val numOfBodies = bodiesIds.size
        if (numOfHeaders != numOfBodies) {
            throw DifferentNumberOfHeadersAndBodies()
        }
    }

    private fun generateItems(): List<HelpItemData> {
        val items = mutableListOf<HelpItemData>()
        for ((index, header) in headers.withIndex()) {
            val body = bodiesIds[index]
            val singleItem =
                HelpItemData(
                    header,
                    body
                )
            items.add(singleItem)
        }
        return items
    }

    class DifferentNumberOfHeadersAndBodies : Exception()
}