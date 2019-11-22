package com.example.medihelper.data.remote

import com.example.medihelper.domain.entities.ApiResponse
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ApiResponseMapper {

    fun getError(ex: Exception) = when (ex) {
        is SocketTimeoutException -> ApiResponse.TIMEOUT
        is HttpException -> when (ex.code()) {
            422 -> ApiResponse.INCORRECT_DATA
            409 -> ApiResponse.ALREADY_EXISTS
            404 -> ApiResponse.NOT_FOUND
            else -> ApiResponse.ERROR

        }
        else -> ApiResponse.ERROR
    }
}