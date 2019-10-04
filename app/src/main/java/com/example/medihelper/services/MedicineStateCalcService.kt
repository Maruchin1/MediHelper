package com.example.medihelper.services

import com.example.medihelper.R

//class MedicineStateCalcService {
//
//    fun state(packageSize: Float?, currState: Float?) = packageSize?.let { currState?.div(it) }
//
//    fun numberText(packageSize: Float?, currState: Float?) = "${currState}/${packageSize}"
//
//    fun stateAvailable(packageSize: Float?, currState: Float?): Boolean {
//        return packageSize != null && currState != null
//    }
//
//    fun stateWeight(packageSize: Float?, currState: Float?) = state(packageSize, currState)
//
//    fun emptyWeight(packageSize: Float?, currState: Float?) = state(packageSize, currState)?.let { 1 - it }
//
//    fun colorId(packageSize: Float?, currState: Float?) = state(packageSize, currState)?.let {
//        when {
//            it >= STATE_GOOD_LIMIT -> R.color.colorStateGood
//            it > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
//            else -> R.color.colorStateSmall
//        }
//    }
//
//    fun text(packageSize: Float?, curState: Float?) = state(packageSize, curState)?.let {
//        when {
//            it >= STATE_GOOD_LIMIT -> TEXT_STATE_GOOD
//            it > STATE_MEDIUM_LIMIT -> TEXT_STATE_MEDIUM
//            else -> TEXT_STATE_SMALL
//        }
//    }
//
//    companion object {
//        private const val STATE_GOOD_LIMIT = 0.75f
//        private const val STATE_MEDIUM_LIMIT = 0.4f
//        private const val TEXT_STATE_GOOD = "Duży zapas"
//        private const val TEXT_STATE_MEDIUM = "Średnia ilość"
//        private const val TEXT_STATE_SMALL = "Blisko końca"
//    }
//}