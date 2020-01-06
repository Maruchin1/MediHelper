package com.maruchin.medihelper.presentation.feature.options

import android.content.Context
import android.content.res.Resources

abstract class HelpGenerator(
    private val context: Context,
    private val headersIds: List<Int>,
    private val bodiesIds: List<Int>
) {

    private val res: Resources by lazy { context.resources }

    @Throws(DifferentNumberOfHeadersAndBodies::class)
    fun generate(): List<HelpItemData> {
        checkData()
        return generateItems()
    }

    private fun checkData() {
        val numOfHeaders = headersIds.size
        val numOfBodies = bodiesIds.size
        if (numOfHeaders != numOfBodies) {
            throw DifferentNumberOfHeadersAndBodies()
        }
    }

    private fun generateItems(): List<HelpItemData> {
        val items = mutableListOf<HelpItemData>()
        for ((index, headerId) in headersIds.withIndex()) {
            val bodyId = bodiesIds[index]
            val singleItem = generateSingleItem(headerId, bodyId)
            items.add(singleItem)
        }
        return items
    }

    private fun generateSingleItem(headerId: Int, bodyId: Int): HelpItemData {
        val header = res.getString(headerId)
        val body = res.getString(bodyId)
        return HelpItemData(header, body)
    }

    class DifferentNumberOfHeadersAndBodies : Exception()
}